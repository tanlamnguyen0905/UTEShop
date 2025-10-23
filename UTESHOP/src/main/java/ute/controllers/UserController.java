package ute.controllers;

import java.io.IOException;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import ute.dao.impl.UserDaoImpl;
import ute.dao.inter.UserDao;
import ute.entities.Addresses;
import ute.entities.Users;
import ute.service.impl.AddressServiceImpl;
import ute.service.inter.AddressService;

@WebServlet({ "/user/profile", "/user/update-profile", "/user/change-password", "/user/delete-account" })
@MultipartConfig
public class UserController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final UserDao userDao = new UserDaoImpl();
	private final AddressService addressService = new AddressServiceImpl();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String path = req.getServletPath();
		HttpSession session = req.getSession();
		Users currentUser = (Users) session.getAttribute("currentUser");

		switch (path) {
			case "/user/update-profile":
				req.getRequestDispatcher("/WEB-INF/views/user/edit-profile.jsp").forward(req, resp);
				break;

			case "/user/change-password":
				req.getRequestDispatcher("/WEB-INF/views/user/change-password.jsp").forward(req, resp);
				break;

			default: // /user/profile
				// Load danh sách địa chỉ
				if (currentUser != null) {
					List<Addresses> addresses = addressService.getAddressesByUserId(currentUser.getUserID());
					req.setAttribute("addresses", addresses);
				}
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

			case "/user/delete-account":
				deleteAccount(req, resp);
				break;
		}
	}

	// ======================= CẬP NHẬT HỒ SƠ =======================
	private void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		HttpSession session = req.getSession();
		Users currentUser = (Users) session.getAttribute("currentUser");

		if (currentUser == null) {
			resp.sendRedirect(req.getContextPath() + "/home");
			return;
		}

		String fullname = req.getParameter("fullname");
		String email = req.getParameter("email");
		String phone = req.getParameter("phone");
		Part avatarPart = req.getPart("avatar");

		// ✅ Kiểm tra email mới có trùng với user khác không
		if (email != null && !email.equals(currentUser.getEmail())) {
			if (userDao.existsByEmail(email)) {
				req.setAttribute("error", "❌ Email đã được sử dụng bởi tài khoản khác!");
				req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
				return;
			}
		}

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

		req.setAttribute("success", "✅ Cập nhật thông tin thành công!");
		req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
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

	// ======================= XÓA TÀI KHOẢN =======================
	private void deleteAccount(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		HttpSession session = req.getSession();
		Users currentUser = (Users) session.getAttribute("currentUser");

		// Kiểm tra đăng nhập
		if (currentUser == null) {
			resp.sendRedirect(req.getContextPath() + "/auth/login");
			return;
		}

		// Kiểm tra text confirmation
		String confirmText = req.getParameter("confirmText");
		if (confirmText == null || !confirmText.equals("XÓA TÀI KHOẢN")) {
			session.setAttribute("error", "Vui lòng nhập đúng 'XÓA TÀI KHOẢN' để xác nhận!");
			resp.sendRedirect(req.getContextPath() + "/user/profile#settings");
			return;
		}

		try {
			// Xóa user khỏi database
			userDao.delete(currentUser.getUserID());

			// Xóa session
			session.invalidate();

			// Redirect về trang home
			resp.sendRedirect(req.getContextPath() + "/home");

		} catch (Exception e) {
			e.printStackTrace();
			// Nếu có lỗi, quay lại profile với thông báo lỗi
			session.setAttribute("error", "Lỗi khi xóa tài khoản: " + e.getMessage());
			resp.sendRedirect(req.getContextPath() + "/user/profile#settings");
		}
	}
}
