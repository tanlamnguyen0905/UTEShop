package ute.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

import ute.dao.UserDao;
import ute.entities.Users;
import ute.services.IUserService;
import ute.services.impl.UserServiceImpl;

@WebServlet({"/user/register", "/user/login", "/user/logout", "/user/profile", "/user/change-password", "/user/delete"})
public class UserController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private IUserService userService = new UserServiceImpl();

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
            case "/user/profile":
                req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
                break;
            case "/user/change-password":
                req.getRequestDispatcher("/WEB-INF/views/user/change-password.jsp").forward(req, resp);
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
            case "/user/change-password":
                handleChangePassword(req, resp);
                break;
            case "/user/delete":
                handleDeleteAccount(req, resp);
                break;
            case "/user/profile":
                handleUpdateProfile(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/user/login");
        }
    }

    // ------------------ Đăng ký ------------------
    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String fullName = req.getParameter("fullName");
            String username = req.getParameter("username");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String confirm = req.getParameter("confirmPassword");

            if (!password.equals(confirm)) {
                req.setAttribute("error", "Mật khẩu xác nhận không khớp");
                req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, resp);
                return;
            }

            Users user = new Users();
            user.setFullName(fullName);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setPhone(req.getParameter("phone"));
            user.setSex(req.getParameter("sex"));
            userService.register(user);

            req.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, resp);

        } catch (RuntimeException e) {
            req.setAttribute("error", e.getMessage());
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

    // ------------------ Cập nhật thông tin ------------------
    private void handleUpdateProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        Users user = (Users) session.getAttribute("currentUser");
        user.setFullName(req.getParameter("fullName"));
        user.setPhone(req.getParameter("phone"));
        user.setSex(req.getParameter("sex"));
        user.setDob(req.getParameter("dob") != null ? java.sql.Date.valueOf(req.getParameter("dob")) : null);

        userService.register(user); // save lại (vì cùng dùng persist hoặc merge)
        session.setAttribute("currentUser", user);

        req.setAttribute("success", "Cập nhật thông tin thành công!");
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }

    // ------------------ Đổi mật khẩu ------------------
    private void handleChangePassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login");
            return;
        }

        Users user = (Users) session.getAttribute("currentUser");
        String oldPwd = req.getParameter("oldPassword");
        String newPwd = req.getParameter("newPassword");
        String confirmPwd = req.getParameter("confirmPassword");

        if (!newPwd.equals(confirmPwd)) {
            req.setAttribute("error", "Mật khẩu xác nhận không khớp");
            req.getRequestDispatcher("/WEB-INF/views/user/change-password.jsp").forward(req, resp);
            return;
        }

        Optional<Users> userOpt = userService.login(user.getUsername(), oldPwd);
        if (userOpt.isPresent()) {
            userService.updatePassword(user.getEmail(), newPwd);
            req.setAttribute("success", "Đổi mật khẩu thành công!");
        } else {
            req.setAttribute("error", "Mật khẩu cũ không đúng!");
        }

        req.getRequestDispatcher("/WEB-INF/views/user/change-password.jsp").forward(req, resp);
    }

    // ------------------ Xóa tài khoản ------------------
    private void handleDeleteAccount(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            Users user = (Users) session.getAttribute("currentUser");
            userService.findByEmail(user.getEmail()).ifPresent(u -> {
                new UserDao().delete(u);
            });
            session.invalidate();
        }
        resp.sendRedirect(req.getContextPath() + "/user/login");
    }
}
