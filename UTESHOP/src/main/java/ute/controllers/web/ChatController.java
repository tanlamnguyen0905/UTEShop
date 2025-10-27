package ute.controllers.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.entities.Users;

import java.io.IOException;

@WebServlet(urlPatterns = {"/chat", "/chat/support"})
public class ChatController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        // Kiểm tra đăng nhập
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        
        Users currentUser = (Users) session.getAttribute("currentUser");
        String role = currentUser.getRole() != null ? currentUser.getRole().toLowerCase() : "user";
        
        // Tạo Room ID dựa trên role và userId
        // Format: {role}-{userId}
        String roomId = role + "-" + currentUser.getUserID();
        
        // Tùy chỉnh title theo role
        String pageTitle;
        switch (role) {
            case "admin":
            case "manager":
                pageTitle = "Chat quản lý";
                break;
            case "shipper":
                pageTitle = "Chat shipper";
                break;
            default:
                pageTitle = "Hỗ trợ khách hàng";
        }
        
        request.setAttribute("roomId", roomId);
        request.setAttribute("pageTitle", pageTitle);
        request.setAttribute("chatType", "general");
        
        request.getRequestDispatcher("/WEB-INF/views/web/chat.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

