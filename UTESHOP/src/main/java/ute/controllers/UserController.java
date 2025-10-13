package ute.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

import org.hibernate.type.descriptor.java.LocalDateTimeJavaType;

import ute.entities.Users;
import ute.service.impl.UserServiceImpl;
import ute.service.inter.UserService;


@WebServlet({ "/user/register", "/user/login", "/user/logout", "/user/profile", "/user/delete",
		"/user/forgot-password" })
public class UserController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final UserService userService = new UserServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();

		switch (path) {
		case "/user/register":
			req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
			break;
		case "/user/login":
			req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
			break;
		case "/user/forgot-password":
			req.getRequestDispatcher("/WEB-INF/views/auth/forgot-password.jsp").forward(req, resp);
			break;
		case "/user/profile":
			req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
			break;
		case "/user/logout":
			handleLogout(req, resp);
			break;
		default:
			resp.sendRedirect(req.getContextPath() + "/user/login");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = req.getServletPath();

		switch (path) {
		case "/user/register":
			handleRegister(req, resp);
			break;
		case "/user/login":
			handleLogin(req, resp);
			break;
		case "/user/forgot-password":
			handleForgotPassword(req, resp);
			break;
		case "/user/profile":
			handleUpdateProfile(req, resp);
			break;
		case "/user/delete":
			handleDeleteAccount(req, resp);
			break;
		default:
			resp.sendRedirect(req.getContextPath() + "/user/login");
		}
	}

	// ------------------ Đăng ký ------------------
	private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String password = req.getParameter("password");
			String confirm = req.getParameter("confirmPassword");

			if (!password.equals(confirm)) {
				req.setAttribute("error", "Mật khẩu xác nhận không khớp");
				req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
				return;
			}

			Users user = new Users();
			user.setFullname(req.getParameter("fullName"));
			user.setUsername(req.getParameter("username"));
			user.setEmail(req.getParameter("email"));
			user.setPassword(password);
			user.setPhone(req.getParameter("phone"));
			user.setAvatar("");
			user.setRole("USER");
			user.setStatus("ACTIVE");
			user.setCreateAt(LocalDateTimeJavaType.INSTANCE.getJavaType().cast(java.time.LocalDateTime.now()));
			user.setLastLoginAt(null);

			if (userService.register(user)) {
				req.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
				req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
			} else {
				req.setAttribute("error", "Tên đăng nhập hoặc email/phone đã tồn tại!");
				req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại!");
			req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
		}
	}

	// ------------------ Đăng nhập ------------------
	private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");

		Optional<Users> userOpt = userService.login(username, password);
		if (userOpt.isPresent()) {
			HttpSession session = req.getSession();
			session.setAttribute("currentUser", userOpt.get());
			resp.sendRedirect(req.getContextPath() + "/user/profile");
		} else {
			req.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
			req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
		}
	}

	// ------------------ Đăng xuất ------------------
	private void handleLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		resp.sendRedirect(req.getContextPath() + "/user/login");
	}

	// ------------------ Cập nhật thông tin ------------------
	private void handleUpdateProfile(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession(false);
		if (session == null || session.getAttribute("currentUser") == null) {
			resp.sendRedirect(req.getContextPath() + "/user/login");
			return;
		}

		Users user = (Users) session.getAttribute("currentUser");
		user.setFullname(req.getParameter("fullName"));
		user.setPhone(req.getParameter("phone"));
		userService.update(user);
		session.setAttribute("currentUser", user);

		req.setAttribute("success", "Cập nhật thông tin thành công!");
		req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
	}

	// ------------------ Quên mật khẩu ------------------
	private void handleForgotPassword(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String email = req.getParameter("email");

		if (email == null || email.isEmpty()) {
			req.setAttribute("error", "Vui lòng nhập email đã đăng ký");
			req.getRequestDispatcher("/WEB-INF/views/auth/forgot-password.jsp").forward(req, resp);
			return;
		}

		Optional<Users> userOpt = userService.findByEmail(email);
		if (userOpt.isPresent()) {
			// Reset mật khẩu tạm thời
			String newPass = "123456";
			userService.updatePassword(email, newPass);

			req.setAttribute("success", "Mật khẩu mới đã được đặt lại là: " + newPass);
			req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
		} else {
			req.setAttribute("error", "Không tìm thấy tài khoản với email này!");
			req.getRequestDispatcher("/WEB-INF/views/auth/forgot-password.jsp").forward(req, resp);
		}
	}

	// ------------------ Xóa tài khoản ------------------
	private void handleDeleteAccount(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		HttpSession session = req.getSession(false);
		if (session != null && session.getAttribute("currentUser") != null) {
			Users user = (Users) session.getAttribute("currentUser");
			userService.findByEmail(user.getEmail()).ifPresent(u -> {
				userService.delete(u.getUserID());
			});
			session.invalidate();
		}
		resp.sendRedirect(req.getContextPath() + "/user/login");
	}
}
