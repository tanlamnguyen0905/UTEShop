package ute.controllers.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.dao.impl.MessageDaoImpl;
import ute.dao.inter.MessageDao;
import ute.entities.Users;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/api/admin/chat", "/api/admin/chat/room"})
public class ChatManagementController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private MessageDao messageDao = new MessageDaoImpl();

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
        
        // Kiểm tra role - chỉ cho phép admin/manager
        if (currentUser.getRole() == null || 
            (!currentUser.getRole().equalsIgnoreCase("admin") && 
             !currentUser.getRole().equalsIgnoreCase("manager"))) {
            session.setAttribute("error", "Bạn không có quyền truy cập trang này!");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        
        String path = request.getServletPath();
        
        if ("/api/admin/chat/room".equals(path)) {
            // Chat với một user cụ thể
            String roomId = request.getParameter("roomId");
            if (roomId == null || roomId.isEmpty()) {
                session.setAttribute("error", "Vui lòng chọn phòng chat!");
                response.sendRedirect(request.getContextPath() + "/api/admin/chat");
                return;
            }
            
            request.setAttribute("roomId", roomId);
            request.setAttribute("pageTitle", "Chat - " + roomId);
            request.setAttribute("chatType", "admin-support");
            request.getRequestDispatcher("/WEB-INF/views/admin/chat-room.jsp").forward(request, response);
        } else {
            // Danh sách các phòng chat - lấy từ database
            try {
                List<Map<String, Object>> rooms = messageDao.findAllRoomsWithLastMessage();
                request.setAttribute("rooms", rooms);
                System.out.println("Found " + rooms.size() + " chat rooms");
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Lỗi khi tải danh sách phòng chat: " + e.getMessage());
            }
            
            request.setAttribute("pageTitle", "Quản lý Chat");
            request.getRequestDispatcher("/WEB-INF/views/admin/chat-list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

