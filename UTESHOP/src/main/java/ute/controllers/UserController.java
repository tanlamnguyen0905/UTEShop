package ute.controllers;

import java.io.IOException;
import java.time.LocalDateTime;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ute.dao.impl.UserDaoImpl;
import ute.dao.inter.UserDao;
import ute.entities.Users;

@WebServlet({ "/user/profile", "/user/update-profile", "/user/change-password" })
@MultipartConfig
public class UserController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final UserDao userDao = new UserDaoImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String path = req.getServletPath();

		switch (path) {
		case "/user/update-profile":
			req.getRequestDispatcher("/WEB-INF/views/user/edit-profile.jsp").forward(req, resp);
			break;

		case "/user/change-password":
			req.getRequestDispatcher("/WEB-INF/views/user/change-password.jsp").forward(req, resp);
			break;

		default: // /user/profile
			req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String path = req.getServletPath();

		switch (path) {
		case "/user/update-profile":
			updateProfile(req, resp);
			break;

		case "/user/change-password":
			changePassword(req, resp);
			break;
		}
	}

	// ======================= CẬP NHẬT HỒ SƠ =======================
	private void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		HttpSession session = req.getSession();
		Users currentUser = (Users) session.getAttribute("currentUser");

		if (currentUser == null) {
			resp.sendRedirect(req.getContextPath() + "/auth/login");
			return;
		}

		String fullname = req.getParameter("fullname");
		String email = req.getParameter("email");
		String phone = req.getParameter("phone");
		Part avatarPart = req.getPart("avatar");

		// upload ảnh mới (nếu có)
		String avatarFileName = currentUser.getAvatar();
		if (avatarPart != null && avatarPart.getSize() > 0) {
			String uploadDir = req.getServletContext().getRealPath("/uploads/avatar/");
			java.nio.file.Files.createDirectories(java.nio.file.Paths.get(uploadDir));
			avatarFileName = java.nio.file.Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
			avatarPart.write(uploadDir + avatarFileName);
		}

		// cập nhật dữ liệu
		currentUser.setFullname(fullname);
		currentUser.setEmail(email);
		currentUser.setPhone(phone);
		currentUser.setAvatar(avatarFileName);

		userDao.update(currentUser);
		session.setAttribute("currentUser", currentUser);

		resp.sendRedirect(req.getContextPath() + "/user/profile");
	}

	// ======================= ĐỔI MẬT KHẨU =======================
	private void changePassword(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		HttpSession session = req.getSession();
		Users currentUser = (Users) session.getAttribute("currentUser");

		if (currentUser == null) {
			resp.sendRedirect(req.getContextPath() + "/auth/login");
			return;
		}

		String currentPassword = req.getParameter("currentPassword");
		String newPassword = req.getParameter("newPassword");

		if (!BCrypt.checkpw(currentPassword, currentUser.getPassword())) {
			req.setAttribute("error", "❌ Mật khẩu hiện tại không chính xác!");
			req.getRequestDispatcher("/WEB-INF/views/user/change-password.jsp").forward(req, resp);
			return;
		}

		// Cập nhật mật khẩu mới
		String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		currentUser.setPassword(hashed);
		userDao.update(currentUser);
		session.setAttribute("currentUser", currentUser);

		// Gửi thông báo thành công
		req.setAttribute("success", "✅ Đổi mật khẩu thành công! Hệ thống sẽ quay lại hồ sơ sau ít giây...");
		req.getRequestDispatcher("/WEB-INF/views/user/change-password.jsp").forward(req, resp);
	}
}
