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

import ute.entities.Users;
import ute.service.impl.MailServiceImpl;
import ute.service.impl.UserServiceImpl;
import ute.service.inter.MailService;
import ute.service.inter.UserService;
import ute.utils.Constant;
import ute.utils.FileStorage;
import ute.utils.JwtUtil;
import ute.utils.OtpUtil;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(urlPatterns = { "/auth/register", "/auth/login", "/auth/logout", "/auth/verify-otp",
		"/auth/forgot-password", "/auth/reset-password", "/auth/check-exist" })
@MultipartConfig
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final UserService userService = new UserServiceImpl();
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
	// =====================
	private void checkUserExist(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		String username = req.getParameter("username");
		String email = req.getParameter("email");

		try {
			boolean usernameExists = (username != null && !username.isEmpty()) && userService.existsByUsername(username);
			boolean emailExists = (email != null && !email.isEmpty()) && userService.existsByEmail(email);

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
		if (username != null && !username.isEmpty() && userService.existsByUsername(username)) {
			out.print("{\"success\":false, \"error\":\"Tên đăng nhập đã tồn tại!\"}");
			return;
		}
		if (userService.existsByEmail(email)) {
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
		String avatarFileName = Constant.DEFAULT_AVATAR;

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
			if (userService.existsByUsername(username)) {
				out.print("{\"success\":false, \"error\":\"Tên đăng nhập đã tồn tại!\"}");
				return;
			}
			if (userService.existsByEmail(email)) {
				out.print("{\"success\":false, \"error\":\"Email đã tồn tại!\"}");
				return;
			}

			// Upload ảnh
			if (avatarPart != null && avatarPart.getSize() > 0) {
				FileStorage avatarStorage = new FileStorage(req.getServletContext(), Constant.UPLOAD_DIR_AVATAR);
				avatarFileName = avatarStorage.save(avatarPart);
			}

			Users u = Users.builder()
			.username(username)
			.password(password)
			.fullname(fullname)
			.email(email)
			.phone(phone)
			.avatar(avatarFileName)
			.role("USER")
			.status("ACTIVE")
			.createAt(LocalDateTime.now())
			.build();

			if (!userService.create(u))
				throw new RuntimeException("Không thể đăng ký người dùng");

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
			Users user = userService.findByUsername(username);
			System.out.println(user.getStatus());
			if (user == null || !BCrypt.checkpw(password, user.getPassword()) || !user.getStatus().equals(Constant.STATUS_ACTIVE)) {
				out.print("{\"success\":false, \"error\":\"Tài khoản hoặc mật khẩu không chính xác!\"}");
				return;
			}

			user.setLastLoginAt(LocalDateTime.now());
			userService.update(user);

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

	// ===================== FORGOT PASSWORD (GỬI OTP) =====================
	private void forgotPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		String email = req.getParameter("email");
		if (email == null || email.isBlank()) {
			out.print("{\"success\":false, \"error\":\"Email không hợp lệ!\"}");
			return;
		}

		if (!userService.existsByEmail(email)) {
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

		Users user = userService.findByEmail(email);
		if (user == null) {
			out.print("{\"success\":false, \"error\":\"Email không tồn tại!\"}");
			return;
		}

		user.setPassword(newPassword);
		userService.update(user);

		session.removeAttribute("otp_code");
		session.removeAttribute("otp_email");
		session.removeAttribute("otp_time");

		// Gửi kèm email để frontend điền sẵn vào form login
		out.print("{\"success\":true, \"message\":\"Đổi mật khẩu thành công!\", \"email\":\"" + email + "\"}");
	}

}
