package ute.controllers.shipper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.entities.Users;

import java.io.IOException;

@WebServlet(urlPatterns = {"/api/shipper/chat", "/api/shipper/chat/room"})
public class ShipperChatController extends HttpServlet {
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
        
        // Kiểm tra role - chỉ cho phép shipper
        if (currentUser.getRole() == null || 
            !currentUser.getRole().equalsIgnoreCase("shipper")) {
            session.setAttribute("error", "Bạn không có quyền truy cập trang này!");
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        
        String path = request.getServletPath();
        
        if ("/api/shipper/chat/room".equals(path)) {
            // Chat với một đơn hàng cụ thể
            String roomId = request.getParameter("roomId");
            String orderId = request.getParameter("orderId");
            
            if (roomId == null || roomId.isEmpty()) {
                // Nếu có orderId, tạo roomId từ orderId
                if (orderId != null && !orderId.isEmpty()) {
                    roomId = "order-" + orderId;
                } else {
                    session.setAttribute("error", "Vui lòng chọn đơn hàng để chat!");
                    response.sendRedirect(request.getContextPath() + "/api/shipper/my-deliveries");
                    return;
                }
            }
            
            request.setAttribute("roomId", roomId);
            request.setAttribute("orderId", orderId);
            request.setAttribute("pageTitle", "Chat đơn hàng #" + orderId);
            request.setAttribute("chatType", "shipper-order");
            request.getRequestDispatcher("/WEB-INF/views/shipper/chat-room.jsp").forward(request, response);
        } else {
            // Danh sách các cuộc trò chuyện
            request.setAttribute("pageTitle", "Tin nhắn");
            request.getRequestDispatcher("/WEB-INF/views/shipper/chat-list.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

