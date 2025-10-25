package ute.controllers.admin;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.dao.impl.OrderDaoImpl;
import ute.entities.Orders;

@WebServlet(urlPatterns = {
    "/api/admin/orders",
    "/api/admin/order/confirm",
    "/api/admin/order/cancel",
    "/api/admin/order/detail"
})
public class OrderManagementController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private final OrderDaoImpl orderDao;
    
    public OrderManagementController() {
        this.orderDao = new OrderDaoImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        String uri = req.getRequestURI();
        
        try {
            if (uri.contains("/api/admin/orders")) {
                showOrders(req, resp);
            } else if (uri.contains("/api/admin/order/detail")) {
                showOrderDetail(req, resp);
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
            if (uri.contains("/api/admin/order/confirm")) {
                confirmOrder(req, resp);
            } else if (uri.contains("/api/admin/order/cancel")) {
                cancelOrder(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/api/admin/orders");
        }
    }
    
    /**
     * Hiển thị danh sách đơn hàng
     */
    private void showOrders(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String statusFilter = req.getParameter("status");
        List<Orders> orders;
        
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            orders = orderDao.findByStatus(statusFilter);
        } else {
            orders = orderDao.findAll();
        }
        
        // Thống kê
        long totalOrders = orderDao.count();
        long pendingCount = orderDao.findByStatus("Đang chờ").size();
        long confirmedCount = orderDao.findByStatus("Đã xác nhận").size();
        long deliveringCount = orderDao.findByStatus("Đang giao hàng").size();
        long deliveredCount = orderDao.findByStatus("Đã giao hàng").size();
        long cancelledCount = orderDao.findByStatus("Đã hủy").size();
        
        req.setAttribute("orders", orders);
        req.setAttribute("currentStatus", statusFilter);
        req.setAttribute("totalOrders", totalOrders);
        req.setAttribute("pendingCount", pendingCount);
        req.setAttribute("confirmedCount", confirmedCount);
        req.setAttribute("deliveringCount", deliveringCount);
        req.setAttribute("deliveredCount", deliveredCount);
        req.setAttribute("cancelledCount", cancelledCount);
        
        req.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(req, resp);
    }
    
    /**
     * Hiển thị chi tiết đơn hàng
     */
    private void showOrderDetail(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        String orderIdStr = req.getParameter("id");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/api/admin/orders");
            return;
        }
        
        Long orderId = Long.parseLong(orderIdStr);
        Orders order = orderDao.findById(orderId);
        
        if (order == null) {
            HttpSession session = req.getSession();
            session.setAttribute("error", "Không tìm thấy đơn hàng");
            resp.sendRedirect(req.getContextPath() + "/api/admin/orders");
            return;
        }
        
        req.setAttribute("order", order);
        req.getRequestDispatcher("/WEB-INF/views/admin/order-detail.jsp").forward(req, resp);
    }
    
    /**
     * Xác nhận đơn hàng
     */
    private void confirmOrder(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        
        try {
            String orderIdStr = req.getParameter("orderId");
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Order ID không hợp lệ");
            }
            
            Long orderId = Long.parseLong(orderIdStr);
            Orders order = orderDao.findById(orderId);
            
            if (order == null) {
                throw new IllegalArgumentException("Không tìm thấy đơn hàng");
            }
            
            if (!"Đang chờ".equals(order.getOrderStatus())) {
                throw new IllegalArgumentException("Chỉ có thể xác nhận đơn hàng đang chờ");
            }
            
            // Cập nhật trạng thái
            order.setOrderStatus("Đã xác nhận");
            orderDao.update(order);
            
            session.setAttribute("success", "Đã xác nhận đơn hàng #" + orderId);
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi khi xác nhận đơn hàng: " + e.getMessage());
        }
        
        resp.sendRedirect(req.getContextPath() + "/api/admin/orders");
    }
    
    /**
     * Hủy đơn hàng
     */
    private void cancelOrder(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        
        try {
            String orderIdStr = req.getParameter("orderId");
            String reason = req.getParameter("reason");
            
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Order ID không hợp lệ");
            }
            
            Long orderId = Long.parseLong(orderIdStr);
            Orders order = orderDao.findById(orderId);
            
            if (order == null) {
                throw new IllegalArgumentException("Không tìm thấy đơn hàng");
            }
            
            if ("Đã giao hàng".equals(order.getOrderStatus())) {
                throw new IllegalArgumentException("Không thể hủy đơn hàng đã giao");
            }
            
            // Cập nhật trạng thái
            order.setOrderStatus("Đã hủy");
            if (reason != null && !reason.trim().isEmpty()) {
                String currentNotes = order.getNotes() != null ? order.getNotes() : "";
                order.setNotes(currentNotes + "\nLý do hủy: " + reason);
            }
            orderDao.update(order);
            
            session.setAttribute("warning", "Đã hủy đơn hàng #" + orderId);
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi khi hủy đơn hàng: " + e.getMessage());
        }
        
        resp.sendRedirect(req.getContextPath() + "/api/admin/orders");
    }
}

