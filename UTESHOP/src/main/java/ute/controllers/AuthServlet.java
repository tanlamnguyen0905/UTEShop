package ute.controllers;

import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import ute.dao.inter.UserDao;
import ute.dao.impl.UserDaoImpl;
import ute.entities.Users;
import ute.service.impl.MailServiceImpl;
import ute.service.inter.MailService;
import ute.utils.JwtUtil;
import ute.utils.OtpUtil;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(urlPatterns = { "/auth/register", "/auth/login", "/auth/logout", "/auth/verify-otp",
		"/auth/forgot-password", "/auth/reset-password", "/auth/check-exist" })
@MultipartConfig
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final UserDao userDAO = new UserDaoImpl();
	private final Gson gson = new Gson();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();

		if ("/auth/login".equals(path)) {
			req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
		} else if ("/auth/register".equals(path)) {
			// Trường hợp chỉ gửi OTP
			if (req.getParameter("sendOtp") != null) {
				sendOtp(req, resp);
				return;
			}
			req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
		} else if ("/auth/logout".equals(path)) {
			// Xóa session
			HttpSession session = req.getSession(false);
			if (session != null)
				session.invalidate();

			// Chuyển đến trang logout để xóa token trong localStorage
			req.getRequestDispatcher("/WEB-INF/views/auth/logout.jsp").forward(req, resp);
			return;
		} else if ("/auth/forgot-password".equals(path)) {
			req.getRequestDispatcher("/WEB-INF/views/auth/forgot-password.jsp").forward(req, resp);
		} else if ("/auth/reset-password".equals(path)) {
			req.getRequestDispatcher("/WEB-INF/views/auth/reset-password.jsp").forward(req, resp);
		} else if ("/auth/check-exist".equals(path)) {
			checkUserExist(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		String path = req.getServletPath();

		if ("/auth/register".equals(path)) {
			register(req, resp, out);
		} else if ("/auth/login".equals(path)) {
			login(req, resp, out);
		} else if ("/auth/forgot-password".equals(path)) {
			forgotPassword(req, resp);
		} else if ("/auth/reset-password".equals(path)) {
			resetPassword(req, resp);
		}
	}

	// ===================== KIỂM TRA USERNAME/EMAIL ĐÃ TỒN TẠI
	private void checkUserExist(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		String username = req.getParameter("username");
		String email = req.getParameter("email");

		try {
			boolean usernameExists = (username != null && !username.isEmpty()) && userDAO.existsByUsername(username);
			boolean emailExists = (email != null && !email.isEmpty()) && userDAO.existsByEmail(email);

			if (usernameExists && emailExists) {
				out.print("{\"success\":false, \"error\":\"Tên đăng nhập và email đã tồn tại!\"}");
			} else if (usernameExists) {
				out.print("{\"success\":false, \"error\":\"Tên đăng nhập đã tồn tại!\"}");
			} else if (emailExists) {
				out.print("{\"success\":false, \"error\":\"Email đã tồn tại!\"}");
			} else {
				out.print("{\"success\":true}");
			}
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"success\":false, \"error\":\"Lỗi hệ thống!\"}");
		}
	}

	// ===================== GỬI OTP =====================
	private void sendOtp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		String email = req.getParameter("email");
		String username = req.getParameter("username");

		if (email == null || email.isEmpty()) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.print("{\"success\":false, \"error\":\"Email không hợp lệ!\"}");
			return;
		}

		// Kiểm tra username/email trước khi gửi OTP
		if (username != null && !username.isEmpty() && userDAO.existsByUsername(username)) {
			out.print("{\"success\":false, \"error\":\"Tên đăng nhập đã tồn tại!\"}");
			return;
		}
		if (userDAO.existsByEmail(email)) {
			out.print("{\"success\":false, \"error\":\"Email đã tồn tại!\"}");
			return;
		}

		String otp = OtpUtil.generateOtp();
		HttpSession session = req.getSession();
		session.setAttribute("otp_code", otp);
		session.setAttribute("otp_email", email);
		session.setAttribute("otp_time", System.currentTimeMillis());

		MailService mailService = new MailServiceImpl();
		mailService.send(email, "UTESHOP - Mã xác thực tài khoản",
				"Mã OTP của bạn là: " + otp + "\nMã có hiệu lực trong 5 phút.\n\nUTESHOP Team.");

		resp.setStatus(HttpServletResponse.SC_OK);
		out.print("{\"success\":true, \"message\":\"Đã gửi mã OTP đến email!\"}");
	}

	// ===================== ĐĂNG KÝ + XÁC THỰC OTP =====================
	private void register(HttpServletRequest req, HttpServletResponse resp, PrintWriter out)
			throws IOException, ServletException {
		resp.setContentType("application/json;charset=UTF-8");

		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String fullname = req.getParameter("fullname");
		String email = req.getParameter("email");
		String phone = req.getParameter("phone");
		String otpInput = req.getParameter("otp");
		Part avatarPart = req.getPart("avatar");
		String avatarFileName = null;

		try {
			HttpSession session = req.getSession();
			String otpSession = (String) session.getAttribute("otp_code");
			String otpEmail = (String) session.getAttribute("otp_email");
			Long otpTime = (Long) session.getAttribute("otp_time");

			// Kiểm tra mã OTP
			if (otpSession == null || otpEmail == null || otpTime == null) {
				out.print("{\"success\":false, \"error\":\"Vui lòng nhận mã OTP trước!\"}");
				return;
			}
			if (!email.equals(otpEmail)) {
				out.print("{\"success\":false, \"error\":\"Email không trùng với email đã gửi OTP!\"}");
				return;
			}
			if (!otpInput.equals(otpSession)) {
				out.print("{\"success\":false, \"error\":\"Mã OTP không đúng!\"}");
				return;
			}
			if (System.currentTimeMillis() - otpTime > 5 * 60 * 1000) {
				out.print("{\"success\":false, \"error\":\"Mã OTP đã hết hạn!\"}");
				return;
			}

			// Kiểm tra username/email trùng
			if (userDAO.existsByUsername(username)) {
				out.print("{\"success\":false, \"error\":\"Tên đăng nhập đã tồn tại!\"}");
				return;
			}
			if (userDAO.existsByEmail(email)) {
				out.print("{\"success\":false, \"error\":\"Email đã tồn tại!\"}");
				return;
			}

		// Upload ảnh vào D:\images\avatar\
		if (avatarPart != null && avatarPart.getSize() > 0) {
			try {
				// Validate file type
				String contentType = avatarPart.getContentType();
				if (!contentType.startsWith("image/")) {
					out.print("{\"success\":false, \"error\":\"Chỉ chấp nhận file hình ảnh!\"}");
					return;
				}
				
				// Validate file size (max 5MB)
				if (avatarPart.getSize() > 5 * 1024 * 1024) {
					out.print("{\"success\":false, \"error\":\"Kích thước ảnh tối đa 5MB!\"}");
					return;
				}
				
				// Lưu vào D:\images\avatar\
				String uploadDir = ute.utils.Constant.Dir + java.io.File.separator + "avatar";
				java.nio.file.Files.createDirectories(java.nio.file.Paths.get(uploadDir));
				
				// Tạo tên file unique
				String originalFileName = java.nio.file.Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
				String fileExtension = "";
				int lastDot = originalFileName.lastIndexOf('.');
				if (lastDot > 0) {
					fileExtension = originalFileName.substring(lastDot);
				}
				String uniqueFileName = System.currentTimeMillis() + "_" + username + fileExtension;
				String filePath = uploadDir + java.io.File.separator + uniqueFileName;
				
				avatarPart.write(filePath);
				
				// Kiểm tra file đã lưu thành công
				java.io.File savedFile = new java.io.File(filePath);
				if (savedFile.exists() && savedFile.length() > 0) {
					avatarFileName = "avatar/" + uniqueFileName;
				} else {
					if (savedFile.exists()) savedFile.delete();
					avatarFileName = "default-avatar.png";
				}
			} catch (Exception e) {
				e.printStackTrace();
				// Nếu upload lỗi, dùng avatar mặc định
				avatarFileName = "default-avatar.png";
			}
		} else {
			avatarFileName = "default-avatar.png";
		}

			// Mã hóa mật khẩu
			String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

			// Lưu user mới (ACTIVE)
			Users u = new Users();
			u.setUsername(username);
			u.setPassword(hashedPassword);
			u.setFullname(fullname);
			u.setEmail(email);
			u.setPhone(phone);
			u.setAvatar(avatarFileName);
			u.setRole("USER");
			u.setStatus("ACTIVE");
			u.setCreateAt(LocalDateTime.now());
			userDAO.insert(u);

			// Dọn session
			session.removeAttribute("otp_code");
			session.removeAttribute("otp_email");
			session.removeAttribute("otp_time");

			out.print("{\"success\":true, \"message\":\"Đăng ký & xác thực thành công!\"}");

		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			out.print("{\"success\":false, \"error\":\"Lỗi hệ thống: " + e.getMessage() + "\"}");
		}
	}

	// ===================== LOGIN =====================
	private void login(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws IOException {
		resp.setContentType("application/json;charset=UTF-8");

		String username = req.getParameter("username");
		String password = req.getParameter("password");

		try {
			Users user = userDAO.findByUsername(username);
			if (user == null) {
				out.print("{\"success\":false, \"error\":\"Tài khoản không tồn tại!\"}");
				return;
			}
			
			// Chỉ cho phép tài khoản ACTIVE đăng nhập
			if (!"ACTIVE".equals(user.getStatus())) {
				String errorMsg = "Tài khoản không thể đăng nhập!";
				if ("DELETED".equals(user.getStatus())) {
					errorMsg = "Tài khoản đã bị xóa!";
				} else if ("INACTIVE".equals(user.getStatus())) {
					errorMsg = "Tài khoản đã bị vô hiệu hóa!";
				} else if ("PENDING".equals(user.getStatus())) {
					errorMsg = "Tài khoản chưa được kích hoạt!";
				}
				out.print("{\"success\":false, \"error\":\"" + errorMsg + "\"}");
				return;
			}
			
			if (!BCrypt.checkpw(password, user.getPassword())) {
				out.print("{\"success\":false, \"error\":\"Sai mật khẩu!\"}");
				return;
			}

			user.setLastLoginAt(LocalDateTime.now());
			userDAO.update(user);

			HttpSession session = req.getSession();
			session.setAttribute("currentUser", user);

			// Generate JWT token
			String token = JwtUtil.generateToken(user.getUsername(), user.getRole(), user.getUserID());
			// session.setAttribute("token", token);

			// Lưu token vào cookie (1 giờ)
			Cookie tokenCookie = new Cookie("authToken", token);
			tokenCookie.setHttpOnly(false); // Cho phép JS đọc
			tokenCookie.setPath("/");
			tokenCookie.setMaxAge(3600); // 1 giờ
			resp.addCookie(tokenCookie);

			// Return token to client
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("username", user.getUsername());
			response.put("role", user.getRole());
			response.put("token", token); // Trả token về client
			out.print(gson.toJson(response));
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"success\":false, \"error\":\"Lỗi máy chủ: " + e.getMessage() + "\"}");
		}
	}

	// ===================== VERIFY OTP (DÙNG RIÊNG, TUỲ OPTION)

	private void verifyOtp(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws IOException {
		HttpSession session = req.getSession();
		String otpInput = req.getParameter("otp");
		String otpSession = (String) session.getAttribute("otp_code");
		String email = (String) session.getAttribute("otp_email");

		if (otpSession == null || email == null) {
			out.print("{\"success\":false, \"error\":\"Chưa có mã OTP trong phiên làm việc!\"}");
			return;
		}
	}

	// ===================== FORGOT PASSWORD (GỬI OTP) =====================
	private void forgotPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		String email = req.getParameter("email");
		if (email == null || email.isBlank()) {
			out.print("{\"success\":false, \"error\":\"Email không hợp lệ!\"}");
			return;
		}

		if (!userDAO.existsByEmail(email)) {
			out.print("{\"success\":false, \"error\":\"Email không tồn tại!\"}");
			return;
		}

		String otp = OtpUtil.generateOtp();
		HttpSession session = req.getSession();
		session.setAttribute("otp_code", otp);
		session.setAttribute("otp_email", email);
		session.setAttribute("otp_time", System.currentTimeMillis());

		MailService mailService = new MailServiceImpl();
		mailService.send(email, "UTESHOP - OTP đặt lại mật khẩu",
				"Mã OTP của bạn là: " + otp + "\nMã có hiệu lực trong 5 phút.\n\nUTESHOP Team.");

		out.print("{\"success\":true, \"message\":\"Đã gửi OTP đến email!\"}");
	}

	// ===================== RESET PASSWORD (XÁC THỰC OTP) =====================
	private void resetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		String email = req.getParameter("email");
		String otpInput = req.getParameter("otp");
		String newPassword = req.getParameter("newPassword");
		String confirmPassword = req.getParameter("confirmPassword");

		if (email == null || otpInput == null || newPassword == null || confirmPassword == null ||
				email.isBlank() || otpInput.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
			out.print("{\"success\":false, \"error\":\"Thiếu thông tin!\"}");
			return;
		}

		if (!newPassword.equals(confirmPassword)) {
			out.print("{\"success\":false, \"error\":\"Mật khẩu xác nhận không khớp!\"}");
			return;
		}

		HttpSession session = req.getSession();
		String otpSession = (String) session.getAttribute("otp_code");
		String otpEmail = (String) session.getAttribute("otp_email");
		Long otpTime = (Long) session.getAttribute("otp_time");

		if (otpSession == null || otpEmail == null || otpTime == null) {
			out.print("{\"success\":false, \"error\":\"OTP chưa được gửi hoặc đã hết hạn!\"}");
			return;
		}
		if (!email.equals(otpEmail)) {
			out.print("{\"success\":false, \"error\":\"Email không trùng với email nhận OTP!\"}");
			return;
		}
		if (!otpInput.equals(otpSession)) {
			out.print("{\"success\":false, \"error\":\"Mã OTP không đúng!\"}");
			return;
		}
		if (System.currentTimeMillis() - otpTime > 5 * 60 * 1000) {
			out.print("{\"success\":false, \"error\":\"OTP đã hết hạn!\"}");
			return;
		}

		Users user = userDAO.findByEmail(email);
		if (user == null) {
			out.print("{\"success\":false, \"error\":\"Email không tồn tại!\"}");
			return;
		}

		String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		user.setPassword(hashed);
		userDAO.update(user);

		session.removeAttribute("otp_code");
		session.removeAttribute("otp_email");
		session.removeAttribute("otp_time");

		// Gửi kèm email để frontend điền sẵn vào form login
		out.print("{\"success\":true, \"message\":\"Đổi mật khẩu thành công!\", \"email\":\"" + email + "\"}");
	}

}
