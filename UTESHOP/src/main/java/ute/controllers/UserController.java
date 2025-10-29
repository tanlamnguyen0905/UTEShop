package ute.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import ute.dao.impl.UserDaoImpl;
import ute.dao.inter.UserDao;
import ute.dto.UserDTO;
import ute.entities.Addresses;
import ute.entities.Users;
import ute.service.impl.AddressServiceImpl;
import ute.service.impl.UserServiceImpl;
import ute.service.inter.AddressService;
import ute.service.inter.UserService;

/**
 * Unified User Controller - xử lý cả API endpoints và View endpoints
 * - API endpoints: /api/user/* - trả về JSON
 * - View endpoints: /user/* - forward tới JSP
 */
@WebServlet({
        // View endpoints
        "/user/profile", "/user/update-profile", "/user/change-password", "/user/delete-account",
        // API endpoints
        "/api/user/profile", "/api/user/password", "/api/user/account"
})
@MultipartConfig
public class UserController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();
    private final UserDao userDao = new UserDaoImpl();
    private final AddressService addressService = new AddressServiceImpl();
    private final ute.service.impl.OrderServiceImpl orderService = new ute.service.impl.OrderServiceImpl();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();
        HttpSession session = req.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");

        // Xử lý API endpoints
        if (path.startsWith("/api/")) {
            handleApiGet(req, resp, path, currentUser);
            return;
        }

        // Xử lý View endpoints
        handleViewGet(req, resp, path, currentUser);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();
        HttpSession session = req.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");

        // Kiểm tra đăng nhập cho tất cả POST requests
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        // Xử lý View POST endpoints
        switch (path) {
            case "/user/update-profile":
                updateProfileForm(req, resp, currentUser);
                break;
            case "/user/change-password":
                changePasswordForm(req, resp, currentUser);
                break;
            case "/user/delete-account":
                deleteAccountForm(req, resp, currentUser);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();
        HttpSession session = req.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");

        // API PUT endpoints
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        if (currentUser == null) {
            sendError(resp, out, 401, "Unauthorized - Please login");
            return;
        }

        try {
            switch (path) {
                case "/api/user/profile":
                    updateProfileApi(req, resp, out, currentUser);
                    break;
                case "/api/user/password":
                    updatePasswordApi(req, resp, out, currentUser);
                    break;
                default:
                    sendError(resp, out, 400, "Invalid path");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, out, 500, "Internal server error: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();
        HttpSession session = req.getSession();
        Users currentUser = (Users) session.getAttribute("currentUser");

        // API DELETE endpoints
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        if (currentUser == null) {
            sendError(resp, out, 401, "Unauthorized - Please login");
            return;
        }

        try {
            if ("/api/user/account".equals(path)) {
                deleteAccountApi(req, resp, out, currentUser);
            } else {
                sendError(resp, out, 400, "Invalid path");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, out, 500, "Internal server error: " + e.getMessage());
        }
    }

    // ==================== VIEW HANDLERS ====================

    private void handleViewGet(HttpServletRequest req, HttpServletResponse resp,
            String path, Users currentUser)
            throws ServletException, IOException {

        // Kiểm tra đăng nhập
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        switch (path) {
            case "/user/update-profile":
                req.getRequestDispatcher("/WEB-INF/views/user/edit-profile.jsp").forward(req, resp);
                break;
            case "/user/change-password":
                req.getRequestDispatcher("/WEB-INF/views/user/change-password.jsp").forward(req, resp);
                break;
            default: // /user/profile
                List<Addresses> addresses = addressService.getAddressesByUserId(currentUser.getUserID());
                List<ute.entities.Orders> orders = orderService.findByUserId(currentUser.getUserID());
                req.setAttribute("addresses", addresses);
                req.setAttribute("orders", orders);
                req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
                break;
        }
    }

    private void updateProfileForm(HttpServletRequest req, HttpServletResponse resp, Users currentUser)
            throws IOException, ServletException {

        // Kiểm tra xem có phải AJAX request không
        boolean isAjax = "XMLHttpRequest".equals(req.getHeader("X-Requested-With")) 
                         || req.getContentType() != null && req.getContentType().contains("multipart/form-data")
                         && req.getParameter("ajax") != null;

        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        Part avatarPart = req.getPart("avatar");

        // Kiểm tra email đã tồn tại
        if (email != null && !email.isEmpty() && !email.equals(currentUser.getEmail())) {
            if (userDao.existsByEmail(email)) {
                if (isAjax) {
                    sendJsonResponse(resp, false, "Email đã được sử dụng bởi tài khoản khác!");
                } else {
                    req.setAttribute("error", "Email đã được sử dụng bởi tài khoản khác!");
                    req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
                }
                return;
            }
        }

        // Upload avatar (nếu có)
        String avatarFileName = currentUser.getAvatar();
        if (avatarPart != null && avatarPart.getSize() > 0) {
            try {
                // Validate file type
                String contentType = avatarPart.getContentType();
                if (!contentType.startsWith("image/")) {
                    if (isAjax) {
                        sendJsonResponse(resp, false, "Chỉ chấp nhận file hình ảnh!");
                    } else {
                        req.setAttribute("error", "Chỉ chấp nhận file hình ảnh!");
                        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
                    }
                    return;
                }

                // Validate file size (max 5MB)
                if (avatarPart.getSize() > 5 * 1024 * 1024) {
                    if (isAjax) {
                        sendJsonResponse(resp, false, "Kích thước file tối đa 5MB!");
                    } else {
                        req.setAttribute("error", "Kích thước file tối đa 5MB!");
                        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
                    }
                    return;
                }

                String uploadDir = req.getServletContext().getRealPath("/uploads/avatar/");
                java.nio.file.Files.createDirectories(java.nio.file.Paths.get(uploadDir));
                
                // Tạo tên file unique
                String originalFileName = java.nio.file.Paths.get(avatarPart.getSubmittedFileName()).getFileName().toString();
                String fileExtension = "";
                int lastDot = originalFileName.lastIndexOf('.');
                if (lastDot > 0) {
                    fileExtension = originalFileName.substring(lastDot);
                }
                avatarFileName = System.currentTimeMillis() + "_" + currentUser.getUserID() + fileExtension;
                
                avatarPart.write(uploadDir + avatarFileName);
                
                // Kiểm tra file đã lưu thành công
                java.io.File savedFile = new java.io.File(uploadDir + avatarFileName);
                if (!savedFile.exists() || savedFile.length() == 0) {
                    if (savedFile.exists()) savedFile.delete();
                    if (isAjax) {
                        sendJsonResponse(resp, false, "Lỗi khi lưu file!");
                    } else {
                        req.setAttribute("error", "Lỗi khi lưu file!");
                        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
                    }
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (isAjax) {
                    sendJsonResponse(resp, false, "Lỗi upload ảnh: " + e.getMessage());
                } else {
                    req.setAttribute("error", "Lỗi upload ảnh: " + e.getMessage());
                    req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
                }
                return;
            }
        }

        // Cập nhật dữ liệu
        if (fullname != null && !fullname.isEmpty()) {
            currentUser.setFullname(fullname);
        }
        if (email != null && !email.isEmpty()) {
            currentUser.setEmail(email);
        }
        if (phone != null && !phone.isEmpty()) {
            currentUser.setPhone(phone);
        }
        currentUser.setAvatar(avatarFileName);

        userDao.update(currentUser);
        req.getSession().setAttribute("currentUser", currentUser);

        // Trả về response phù hợp
        if (isAjax) {
            sendJsonResponse(resp, true, "Cập nhật thông tin thành công!");
        } else {
            req.setAttribute("success", "Cập nhật thông tin thành công!");
            req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
        }
    }
    
    private void sendJsonResponse(HttpServletResponse resp, boolean success, String message) 
            throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        JsonObject json = new JsonObject();
        json.addProperty("success", success);
        json.addProperty("message", message);
        resp.getWriter().write(json.toString());
    }

    private void changePasswordForm(HttpServletRequest req, HttpServletResponse resp, Users currentUser)
            throws IOException, ServletException {

        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");

        // Kiểm tra mật khẩu hiện tại
        if (!BCrypt.checkpw(currentPassword, currentUser.getPassword())) {
            req.setAttribute("error", "Mật khẩu hiện tại không chính xác!");
            req.getRequestDispatcher("/WEB-INF/views/user/change-password.jsp").forward(req, resp);
            return;
        }

        // Cập nhật mật khẩu mới
        String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        currentUser.setPassword(hashed);
        userDao.update(currentUser);
        req.getSession().setAttribute("currentUser", currentUser);

        req.setAttribute("success", "Đổi mật khẩu thành công! Hệ thống sẽ quay lại hồ sơ sau ít giây...");
        req.getRequestDispatcher("/WEB-INF/views/user/change-password.jsp").forward(req, resp);
    }

    private void deleteAccountForm(HttpServletRequest req, HttpServletResponse resp, Users currentUser)
            throws IOException {

        HttpSession session = req.getSession();
        String confirmText = req.getParameter("confirmText");

        // Kiểm tra text confirmation
        if (confirmText == null || !confirmText.equals("XÓA TÀI KHOẢN")) {
            session.setAttribute("error", "Vui lòng nhập đúng 'XÓA TÀI KHOẢN' để xác nhận!");
            resp.sendRedirect(req.getContextPath() + "/user/profile#settings");
            return;
        }

        try {
            userDao.delete(currentUser.getUserID());
            session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi khi xóa tài khoản: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/user/profile#settings");
        }
    }

    // ==================== API HANDLERS ====================

    private void handleApiGet(HttpServletRequest req, HttpServletResponse resp,
            String path, Users currentUser)
            throws IOException {

        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        if (currentUser == null) {
            sendError(resp, out, 401, "Unauthorized - Please login");
            return;
        }

        try {
            if ("/api/user/profile".equals(path)) {
                getUserProfileApi(resp, out, currentUser);
            } else {
                sendError(resp, out, 400, "Invalid path");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, out, 500, "Internal server error: " + e.getMessage());
        }
    }

    private void getUserProfileApi(HttpServletResponse resp, PrintWriter out, Users currentUser) {
        Users user = userService.findByUsername(currentUser.getUsername());

        if (user == null) {
            sendError(resp, out, 404, "User not found");
            return;
        }

        UserDTO dto = UserDTO.fromEntity(user);
        sendSuccess(resp, out, dto, "Success");
    }

    private void updateProfileApi(HttpServletRequest req, HttpServletResponse resp,
            PrintWriter out, Users currentUser)
            throws IOException {

        HttpSession session = req.getSession();
        JsonObject json = gson.fromJson(req.getReader(), JsonObject.class);

        Users user = userService.findByUsername(currentUser.getUsername());
        if (user == null) {
            sendError(resp, out, 404, "User not found");
            return;
        }

        // Update fullname
        if (json.has("fullname") && !json.get("fullname").isJsonNull()) {
            user.setFullname(json.get("fullname").getAsString());
        }

        // Update email
        if (json.has("email") && !json.get("email").isJsonNull()) {
            String newEmail = json.get("email").getAsString();
            if (!newEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                sendError(resp, out, 400, "Invalid email format");
                return;
            }
            if (!newEmail.equals(user.getEmail()) && userService.existsByEmail(newEmail)) {
                sendError(resp, out, 400, "Email already exists");
                return;
            }
            user.setEmail(newEmail);
        }

        // Update phone
        if (json.has("phone") && !json.get("phone").isJsonNull()) {
            String phone = json.get("phone").getAsString();
            if (!phone.matches("^[0-9]{10,11}$")) {
                sendError(resp, out, 400, "Invalid phone number format");
                return;
            }
            user.setPhone(phone);
        }

        // Update avatar
        if (json.has("avatar") && !json.get("avatar").isJsonNull()) {
            user.setAvatar(json.get("avatar").getAsString());
        }

        userService.update(user);
        session.setAttribute("currentUser", user);

        UserDTO result = UserDTO.fromEntity(user);
        sendSuccess(resp, out, result, "Profile updated successfully");
    }

    private void updatePasswordApi(HttpServletRequest req, HttpServletResponse resp,
            PrintWriter out, Users currentUser)
            throws IOException {

        HttpSession session = req.getSession();
        JsonObject json = gson.fromJson(req.getReader(), JsonObject.class);

        // Validation
        if (!json.has("currentPassword") || !json.has("newPassword")) {
            sendError(resp, out, 400, "Missing required fields: currentPassword, newPassword");
            return;
        }

        String currentPassword = json.get("currentPassword").getAsString();
        String newPassword = json.get("newPassword").getAsString();

        Users user = userService.findByUsername(currentUser.getUsername());
        if (user == null) {
            sendError(resp, out, 404, "User not found");
            return;
        }

        // Kiểm tra mật khẩu hiện tại
        if (!BCrypt.checkpw(currentPassword, user.getPassword())) {
            sendError(resp, out, 400, "Current password is incorrect");
            return;
        }

        // Validation mật khẩu mới
        if (newPassword.length() < 6) {
            sendError(resp, out, 400, "New password must be at least 6 characters");
            return;
        }

        if (BCrypt.checkpw(newPassword, user.getPassword())) {
            sendError(resp, out, 400, "New password must be different from current password");
            return;
        }

        // Update mật khẩu
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(hashedPassword);
        userService.update(user);
        session.setAttribute("currentUser", user);

        sendSuccess(resp, out, null, "Password updated successfully");
    }

    private void deleteAccountApi(HttpServletRequest req, HttpServletResponse resp,
            PrintWriter out, Users currentUser)
            throws IOException {

        HttpSession session = req.getSession();
        JsonObject json = gson.fromJson(req.getReader(), JsonObject.class);

        // Validation
        if (!json.has("confirmText")) {
            sendError(resp, out, 400, "Missing required field: confirmText");
            return;
        }

        String confirmText = json.get("confirmText").getAsString();
        if (!"XÓA TÀI KHOẢN".equals(confirmText)) {
            sendError(resp, out, 400, "Please enter exactly 'XÓA TÀI KHOẢN' to confirm");
            return;
        }

        try {
            userDao.delete(currentUser.getUserID());
            session.invalidate();
            sendSuccess(resp, out, null, "Account deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, out, 500, "Error deleting account: " + e.getMessage());
        }
    }

    // ==================== HELPER METHODS ====================

    private void sendSuccess(HttpServletResponse resp, PrintWriter out,
            Object data, String message) {
        resp.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        out.print(gson.toJson(response));
    }

    private void sendError(HttpServletResponse resp, PrintWriter out,
            int statusCode, String message) {
        resp.setStatus(statusCode);
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        out.print(gson.toJson(response));
    }
}
