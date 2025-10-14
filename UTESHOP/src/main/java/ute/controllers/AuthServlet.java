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
import ute.utils.JwtUtil;

import org.mindrot.jbcrypt.BCrypt;

@WebServlet(urlPatterns = { "/auth/register", "/auth/login", "/auth/logout" })
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
	        req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
	    } else if ("/auth/logout".equals(path)) {
	        // Hủy session khi đăng xuất
	        HttpSession session = req.getSession(false);
	        if (session != null) {
	            session.invalidate();
	        }
	        // Chuyển hướng về trang chủ
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

	// ===================== REGISTER =====================
	private void register(HttpServletRequest req, HttpServletResponse resp, PrintWriter out)
			throws IOException, ServletException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String fullname = req.getParameter("fullname");
		String email = req.getParameter("email");
		String phone = req.getParameter("phone");
		Part avatarPart = req.getPart("avatar");
		String avatarFileName = null;

		if (userDAO.existsByUsername(username)) {
			resp.setStatus(HttpServletResponse.SC_CONFLICT);
			out.print("{\"error\":\"Tên đăng nhập đã tồn tại\"}");
			return;
		}
		if (userDAO.existsByEmail(email)) {
			resp.setStatus(HttpServletResponse.SC_CONFLICT);
			out.print("{\"error\":\"Email đã tồn tại\"}");
			return;
		}
		// Xử lý upload ảnh đại diện
		if (avatarPart != null && avatarPart.getSize() > 0) {
			String uploadDir = req.getServletContext().getRealPath("/uploads/avatar/");
			java.nio.file.Files.createDirectories(java.nio.file.Paths.get(uploadDir));

			avatarFileName = java.nio.file.Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
			avatarPart.write(uploadDir + avatarFileName);
		}

		// Mã hóa mật khẩu trước khi lưu
		String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

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
		resp.sendRedirect(req.getContextPath() + "/auth/login");
	}

	// ===================== LOGIN =====================
	private void login(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) throws IOException {
		String username = req.getParameter("username");
		String password = req.getParameter("password");

		Users user = userDAO.findByUsername(username);

		if (user == null) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			req.setAttribute("error", "Tài khoản không tồn tại");
			try {
				req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
			} catch (ServletException e) {
				throw new IOException(e);
			}
			return;
		}

		// Kiểm tra mật khẩu mã hóa
		if (!BCrypt.checkpw(password, user.getPassword())) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			req.setAttribute("error", "Sai mật khẩu");
			try {
				req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);
			} catch (ServletException e) {
				throw new IOException(e);
			}
			return;
		}

		// Cập nhật thời gian đăng nhập
		user.setLastLoginAt(LocalDateTime.now());
		userDAO.update(user);

		// Lưu session
		HttpSession session = req.getSession();
		session.setAttribute("currentUser", user);

		// Tạo token JWT (tùy chọn)
		String token = JwtUtil.generateToken(user.getUsername(), user.getRole(), user.getUserID());
		session.setAttribute("token", token);

		// Chuyển hướng về trang chủ
		resp.sendRedirect(req.getContextPath() + "/home");
	}
}
