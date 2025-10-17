package ute.controllers;

import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import ute.dao.inter.UserDao;
import ute.dao.impl.UserDaoImpl;
import ute.entities.Users;
import ute.service.impl.MailServiceImpl;
import ute.service.inter.MailService;
import ute.utils.JwtUtil;
import ute.utils.OtpUtil;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(urlPatterns = { "/auth/register", "/auth/login", "/auth/logout", "/auth/verify-otp" })
@MultipartConfig
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final UserDao userDAO = new UserDaoImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();

		if ("/auth/login".equals(path)) {
			req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
		} else if ("/auth/register".equals(path)) {
			// 📨 Trường hợp chỉ gửi OTP
			if (req.getParameter("sendOtp") != null) {
				sendOtp(req, resp);
				return;
			}
			req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
		} else if ("/auth/logout".equals(path)) {
			HttpSession session = req.getSession(false);
			if (session != null)
				session.invalidate();
			resp.sendRedirect(req.getContextPath() + "/home");
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
		}
	}

	// ===================== 1️⃣ GỬI OTP =====================
	private void sendOtp(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		String email = req.getParameter("email");
		if (email == null || email.isEmpty()) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			out.print("{\"success\":false, \"error\":\"Email không hợp lệ!\"}");
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

	// ===================== 2️⃣ ĐĂNG KÝ + XÁC THỰC OTP =====================
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

			// 🧩 Kiểm tra mã OTP
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

			// 🧩 Kiểm tra username/email trùng
			if (userDAO.existsByUsername(username)) {
				out.print("{\"success\":false, \"error\":\"Tên đăng nhập đã tồn tại!\"}");
				return;
			}
			if (userDAO.existsByEmail(email)) {
				out.print("{\"success\":false, \"error\":\"Email đã tồn tại!\"}");
				return;
			}

			// 🖼 Upload ảnh
			if (avatarPart != null && avatarPart.getSize() > 0) {
				String uploadDir = req.getServletContext().getRealPath("/uploads/avatar/");
				java.nio.file.Files.createDirectories(java.nio.file.Paths.get(uploadDir));
				avatarFileName = java.nio.file.Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
				avatarPart.write(uploadDir + avatarFileName);
			} else {
				avatarFileName = "default-avatar.png";
			}

			// 🔐 Mã hóa mật khẩu
			String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

			// 🧍‍♂️ Lưu user mới (ACTIVE)
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

			// ✅ Dọn session
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

	// ===================== 3️⃣ LOGIN =====================
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
			if (!BCrypt.checkpw(password, user.getPassword())) {
				out.print("{\"success\":false, \"error\":\"Sai mật khẩu!\"}");
				return;
			}

			user.setLastLoginAt(LocalDateTime.now());
			userDAO.update(user);

			HttpSession session = req.getSession();
			session.setAttribute("currentUser", user);

			String token = JwtUtil.generateToken(user.getUsername(), user.getRole(), user.getUserID());
			session.setAttribute("token", token);

			out.print(String.format("{\"success\":true, \"username\":\"%s\", \"role\":\"%s\"}", user.getUsername(),
					user.getRole()));
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{\"success\":false, \"error\":\"Lỗi máy chủ: " + e.getMessage() + "\"}");
		}
	}

	// ===================== 4️⃣ VERIFY OTP (DÙNG RIÊNG, TUỲ OPTION)
	// =====================
	private void verifyOtp(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws IOException {
		HttpSession session = req.getSession();
		String otpInput = req.getParameter("otp");
		String otpSession = (String) session.getAttribute("otp_code");
		String email = (String) session.getAttribute("otp_email");

		if (otpSession == null || email == null) {
			out.print("{\"success\":false, \"error\":\"Chưa có mã OTP trong phiên làm việc!\"}");
			return;
		}
		if (!otpInput.equals(otpSession)) {
			out.print("{\"success\":false, \"error\":\"Mã OTP không chính xác!\"}");
			return;
		}

		boolean updated = userDAO.activateUserByEmail(email);
		if (updated) {
			session.removeAttribute("otp_code");
			session.removeAttribute("otp_email");
			out.print("{\"success\":true, \"message\":\"Xác thực thành công!\"}");
		} else {
			out.print("{\"success\":false, \"error\":\"Không thể kích hoạt tài khoản!\"}");
		}
	}
}
