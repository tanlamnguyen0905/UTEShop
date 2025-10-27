package ute.controllers.shipper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.dao.impl.OrderDaoImpl;
import ute.entities.Delivery;
import ute.entities.Orders;
import ute.entities.Users;
import ute.service.impl.DeliveryServiceImpl;

@WebServlet(urlPatterns = {
    "/api/shipper/feed",
    "/api/shipper/my-deliveries",
    "/api/shipper/delivery-detail",
    "/api/shipper/delivery-action"
})
public class ShipperController extends HttpServlet {
    
    private OrderDaoImpl orderDao = new OrderDaoImpl();
    private DeliveryServiceImpl deliveryService = new DeliveryServiceImpl();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        
        // Kiểm tra session
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }
        
        Users currentUser = (Users) session.getAttribute("currentUser");
        
        // Kiểm tra đăng nhập
        if (currentUser == null) {
            session.setAttribute("error", "Vui lòng đăng nhập!");
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }
        
        // Kiểm tra quyền shipper (case-insensitive)
        if (currentUser.getRole() == null || 
            !currentUser.getRole().equalsIgnoreCase("shipper")) {
            session.setAttribute("error", "Bạn không có quyền truy cập trang này!");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }
        
        String path = req.getServletPath();
        
        try {
            switch (path) {
                case "/api/shipper/feed":
                    handleFeed(req, resp, currentUser);
                    break;
                    
                case "/api/shipper/my-deliveries":
                    handleMyDeliveries(req, resp, currentUser);
                    break;
                    
                case "/api/shipper/delivery-detail":
                    handleDeliveryDetail(req, resp, currentUser, session);
                    break;
                    
                default:
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/api/shipper/feed");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        
        // Kiểm tra session
        if (session == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }
        
        Users currentUser = (Users) session.getAttribute("currentUser");
        
        // Kiểm tra đăng nhập
        if (currentUser == null) {
            session.setAttribute("error", "Vui lòng đăng nhập!");
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }
        
        // Kiểm tra quyền shipper (case-insensitive)
        if (currentUser.getRole() == null || 
            !currentUser.getRole().equalsIgnoreCase("shipper")) {
            session.setAttribute("error", "Bạn không có quyền truy cập trang này!");
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }
        
        String path = req.getServletPath();
        
        if ("/api/shipper/delivery-action".equals(path)) {
            handleDeliveryAction(req, resp, currentUser, session);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    /**
     * Xử lý trang feed - danh sách đơn hàng có sẵn
     */
    private void handleFeed(HttpServletRequest req, HttpServletResponse resp, Users currentUser) 
            throws ServletException, IOException {
        
        // Lấy các đơn hàng "Đã xác nhận" chưa có shipper nhận
        List<Orders> availableOrders = orderDao.findConfirmedOrders();
        long count = orderDao.countConfirmedOrders();
        
        // Set attributes
        req.setAttribute("availableOrders", availableOrders);
        req.setAttribute("count", count);
        req.setAttribute("pageTitle", "Đơn hàng có sẵn");
        req.setAttribute("currentPage", "feed");
        
        // Forward tới JSP
        req.getRequestDispatcher("/WEB-INF/views/shipper/feed.jsp").forward(req, resp);
    }
    
    /**
     * Xử lý trang my-deliveries - đơn giao hàng của shipper
     */
    private void handleMyDeliveries(HttpServletRequest req, HttpServletResponse resp, Users currentUser) 
            throws ServletException, IOException {
        
        Long shipperId = currentUser.getUserID();
        String statusFilter = req.getParameter("status");
        
        // Lấy danh sách delivery
        List<Delivery> deliveries;
        if (statusFilter != null && !statusFilter.isEmpty()) {
            deliveries = deliveryService.findByShipperIdAndStatus(shipperId, statusFilter);
        } else {
            deliveries = deliveryService.findByShipperId(shipperId);
        }
        
        // Lấy thống kê
        Map<String, Long> stats = deliveryService.getShipperStats(shipperId);
        
        // Set attributes
        req.setAttribute("deliveries", deliveries);
        req.setAttribute("stats", stats);
        req.setAttribute("statusFilter", statusFilter);
        req.setAttribute("pageTitle", statusFilter != null ? statusFilter : "Đơn của tôi");
        req.setAttribute("currentPage", "my-deliveries");
        
        // Forward tới JSP
        req.getRequestDispatcher("/WEB-INF/views/shipper/my-deliveries.jsp").forward(req, resp);
    }
    
    /**
     * Xử lý trang delivery-detail - chi tiết đơn giao hàng
     */
    private void handleDeliveryDetail(HttpServletRequest req, HttpServletResponse resp, 
                                      Users currentUser, HttpSession session) 
            throws ServletException, IOException {
        
        try {
            Long deliveryId = Long.parseLong(req.getParameter("id"));
            Delivery delivery = deliveryService.findById(deliveryId);
            
            if (delivery == null) {
                session.setAttribute("error", "Không tìm thấy đơn giao hàng!");
                resp.sendRedirect(req.getContextPath() + "/api/shipper/my-deliveries");
                return;
            }
            
            // Kiểm tra xem delivery có thuộc về shipper này không
            if (!delivery.getShipper().getUserID().equals(currentUser.getUserID())) {
                session.setAttribute("error", "Bạn không có quyền xem đơn giao hàng này!");
                resp.sendRedirect(req.getContextPath() + "/api/shipper/my-deliveries");
                return;
            }
            
            req.setAttribute("delivery", delivery);
            req.setAttribute("pageTitle", "Chi tiết đơn #" + delivery.getDeliveryID());
            req.setAttribute("currentPage", "delivery-detail");
            req.getRequestDispatcher("/WEB-INF/views/shipper/delivery-detail.jsp").forward(req, resp);
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/api/shipper/my-deliveries");
        }
    }
    
    /**
     * Xử lý các actions POST - nhận đơn, cập nhật trạng thái
     */
    private void handleDeliveryAction(HttpServletRequest req, HttpServletResponse resp, 
                                      Users currentUser, HttpSession session) 
            throws ServletException, IOException {
        
        String action = req.getParameter("action");
        
        try {
            if ("accept".equals(action)) {
                // Nhận đơn hàng
                Long orderId = Long.parseLong(req.getParameter("orderId"));
                Long shipperId = currentUser.getUserID();
                
                Delivery delivery = deliveryService.acceptOrder(orderId, shipperId);
                
                session.setAttribute("success", "Đã nhận đơn hàng thành công!");
                resp.sendRedirect(req.getContextPath() + "/api/shipper/my-deliveries");
                
            } else if ("update-status".equals(action)) {
                // Cập nhật trạng thái
                Long deliveryId = Long.parseLong(req.getParameter("deliveryId"));
                String newStatus = req.getParameter("status");
                String notes = req.getParameter("notes");
                
                deliveryService.updateDeliveryStatus(deliveryId, newStatus, notes);
                
                session.setAttribute("success", "Đã cập nhật trạng thái thành công!");
                resp.sendRedirect(req.getContextPath() + "/api/shipper/my-deliveries");
                
            } else if ("complete".equals(action)) {
                // Đánh dấu hoàn thành
                Long deliveryId = Long.parseLong(req.getParameter("deliveryId"));
                
                deliveryService.markAsCompleted(deliveryId);
                
                session.setAttribute("success", "Đã hoàn thành giao hàng!");
                resp.sendRedirect(req.getContextPath() + "/api/shipper/my-deliveries");
                
            } else if ("fail".equals(action)) {
                // Đánh dấu thất bại
                Long deliveryId = Long.parseLong(req.getParameter("deliveryId"));
                String failureReason = req.getParameter("failureReason");
                
                deliveryService.markAsFailed(deliveryId, failureReason);
                
                session.setAttribute("success", "Đã đánh dấu giao hàng thất bại!");
                resp.sendRedirect(req.getContextPath() + "/api/shipper/my-deliveries");
                
            } else {
                session.setAttribute("error", "Hành động không hợp lệ!");
                resp.sendRedirect(req.getContextPath() + "/api/shipper/feed");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            
            String referer = req.getHeader("Referer");
            if (referer != null && !referer.isEmpty()) {
                resp.sendRedirect(referer);
            } else {
                resp.sendRedirect(req.getContextPath() + "/api/shipper/feed");
            }
        }
    }
}
