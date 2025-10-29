package ute.controllers.admin;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.configs.GsonConfig;
import ute.dto.UpdateUserDTO;
import ute.dto.UserDTO;
import ute.entities.Users;
import ute.service.impl.UserServiceImpl;
import ute.utils.Constant;

@WebServlet(urlPatterns = {
    "/api/admin/users",
    "/api/admin/user/list",
    "/api/admin/user/insert",
    "/api/admin/user/edit",
    "/api/admin/user/update",
    "/api/admin/user/delete"
})
public class UserManagementController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private Gson gson = GsonConfig.getGson();
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
            } else if (uri.contains("/api/admin/user/list")) {
                searchUserAPI(req, resp);
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
        	if (uri.contains("/api/admin/user/insert")) {
        		insertUser(req, resp);
        	} else if (uri.contains("/api/admin/user/update")) {
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
        List<String> roles = Arrays.asList(Constant.ROLE_ADMIN, Constant.ROLE_MANAGER, Constant.ROLE_SHIPPER, Constant.ROLE_USER);
        List<String> statuses = Arrays.asList(Constant.STATUS_ACTIVE, Constant.STATUS_INACTIVE, Constant.ORDER_PENDING);
        
        req.setAttribute("users", users);
        req.setAttribute("totalUsers", totalUsers);
        req.setAttribute("adminCount", adminCount);
        req.setAttribute("managerCount", managerCount);
        req.setAttribute("shipperCount", shipperCount);
        req.setAttribute("userCount", userCount);
        req.setAttribute("roles", roles);
        req.setAttribute("statuses", statuses);
        
        req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
    }

    private void searchUserAPI(HttpServletRequest req, HttpServletResponse resp) throws IOException  {
        resp.setContentType("application/json;charset=UTF-8");

        String search = req.getParameter("search");
		String role = req.getParameter("role");
		String status = req.getParameter("status");

		List<Users> users = userService.search(search, role, status);
		List<UserDTO> userDTOs = users.stream().map(UserDTO::fromEntity).collect(Collectors.toList());
		resp.getWriter().write(gson.toJson(userDTOs));
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
    
    
    private void insertUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	HttpSession session = req.getSession();
        
        try {
            // Lấy dữ liệu từ form
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String role = req.getParameter("role");
            String status = req.getParameter("status");
            String fullname = req.getParameter("fullname");
            String email = req.getParameter("email");
            
            Users newUser = Users.builder()
            	.username(username)
            	.password(password)
            	.role(role)
            	.status(status)
            	.fullname(fullname)
            	.email(email)
            	.avatar("default-avatar.png")
            	.createAt(LocalDateTime.now())
            	.build();
            
            // Create user
            if (userService.create(newUser))
	            session.setAttribute("success", "Thêm người dùng thành công!");
            
        } catch (Exception e) {
            // Lỗi khác
            e.printStackTrace();
            session.setAttribute("error", "Lỗi khi cập nhật người dùng: " + e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/api/admin/users");
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

