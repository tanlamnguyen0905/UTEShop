package ute.controllers.admin;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.dto.UpdateUserDTO;
import ute.dto.UserDTO;
import ute.service.impl.UserServiceImpl;

@WebServlet(urlPatterns = {
    "/api/admin/users",
    "/api/admin/user/edit",
    "/api/admin/user/update",
    "/api/admin/user/delete"
})
public class UserManagementController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private final UserServiceImpl userService;
    
    public UserManagementController() {
        this.userService = new UserServiceImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        String uri = req.getRequestURI();
        
        try {
            if (uri.contains("/api/admin/users")) {
                showUsersManagement(req, resp);
            } else if (uri.contains("/api/admin/user/edit")) {
                showEditUserForm(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi: " + e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        String uri = req.getRequestURI();
        HttpSession session = req.getSession();
        
        try {
            if (uri.contains("/api/admin/user/update")) {
                updateUser(req, resp);
            } else if (uri.contains("/api/admin/user/delete")) {
                deleteUser(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/api/admin/users");
        }
    }
    
    /**
     * Hiển thị danh sách user
     */
    private void showUsersManagement(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        // Lấy danh sách tất cả users (sử dụng DTO - không có password)
        List<UserDTO> users = userService.findAllUsers();
        
        // Thống kê
        long totalUsers = userService.countAllUsers();
        long adminCount = userService.countUsersByRole("ADMIN");
        long managerCount = userService.countUsersByRole("MANAGER");
        long shipperCount = userService.countUsersByRole("SHIPPER");
        long userCount = userService.countUsersByRole("USER");
        
        req.setAttribute("users", users);
        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("adminCount", adminCount);
        req.setAttribute("managerCount", managerCount);
        req.setAttribute("shipperCount", shipperCount);
        req.setAttribute("userCount", userCount);
        
        req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
    }
    
    /**
     * Hiển thị form chỉnh sửa user
     */
    private void showEditUserForm(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String userIdStr = req.getParameter("id");
        if (userIdStr == null || userIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/api/admin/users");
            return;
        }
        
        Long userId = Long.parseLong(userIdStr);
        UserDTO user = userService.findUserById(userId);
        
        if (user == null) {
            HttpSession session = req.getSession();
            session.setAttribute("error", "Không tìm thấy người dùng");
            resp.sendRedirect(req.getContextPath() + "/api/admin/users");
            return;
        }
        
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/admin/user-edit.jsp").forward(req, resp);
    }
    
    /**
     * Xử lý cập nhật user
     */
    private void updateUser(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        
        try {
            // Lấy dữ liệu từ form
            String userIdStr = req.getParameter("userID");
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("ID người dùng không hợp lệ");
            }
            
            UpdateUserDTO dto = UpdateUserDTO.builder()
                .userID(Long.parseLong(userIdStr))
                .fullname(req.getParameter("fullname"))
                .email(req.getParameter("email"))
                .phone(req.getParameter("phone"))
                .role(req.getParameter("role"))
                .status(req.getParameter("status"))
                .build();
            
            // Validate và update user
            userService.updateUser(dto);
            
            session.setAttribute("success", "Cập nhật người dùng thành công!");
            resp.sendRedirect(req.getContextPath() + "/api/admin/users");
            
        } catch (IllegalArgumentException e) {
            // Lỗi validation
            session.setAttribute("error", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/api/admin/user/edit?id=" + req.getParameter("userID"));
            
        } catch (Exception e) {
            // Lỗi khác
            e.printStackTrace();
            session.setAttribute("error", "Lỗi khi cập nhật người dùng: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/api/admin/users");
        }
    }
    
    /**
     * Xử lý xóa user
     */
    private void deleteUser(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        
        try {
            String userIdStr = req.getParameter("id");
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("ID người dùng không hợp lệ");
            }
            
            Long userId = Long.parseLong(userIdStr);
            
            // Không cho phép xóa chính mình (lấy từ JWT token)
            Long currentUserId = (Long) req.getAttribute("tokenUserId");
            if (currentUserId != null && currentUserId.equals(userId)) {
                throw new IllegalArgumentException("Không thể xóa tài khoản của chính bạn");
            }
            
            // Xóa user
            userService.deleteUser(userId);
            
            session.setAttribute("success", "Xóa người dùng thành công!");
            
        } catch (IllegalArgumentException e) {
            session.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi khi xóa người dùng: " + e.getMessage());
        }
        
        resp.sendRedirect(req.getContextPath() + "/api/admin/users");
    }
}

