package ute.controllers.shipper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.dao.impl.OrderDaoImpl;
import ute.dto.DeliveryDTO;
import ute.entities.Orders;
import ute.entities.Users;
import ute.service.impl.DeliveryServiceImpl;

@WebServlet(urlPatterns = {
    "/api/shipper/dashboard",
    "/api/shipper/deliveries",
    "/api/shipper/delivery/detail",
    "/api/shipper/delivery/complete",
    "/api/shipper/delivery/fail",
    "/api/shipper/order/accept"
})
public class ShipperController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    private final DeliveryServiceImpl deliveryService;
    private final OrderDaoImpl orderDao;
    
    public ShipperController() {
        this.deliveryService = new DeliveryServiceImpl();
        this.orderDao = new OrderDaoImpl();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        String uri = req.getRequestURI();
        HttpSession session = req.getSession();
        
        // Filter đã kiểm tra authentication và role, chỉ cần lấy user
        Users currentUser = (Users) session.getAttribute("currentUser");
        
        Long shipperId = currentUser.getUserID();
        
        try {
            if (uri.contains("/api/shipper/dashboard")) {
                showDashboard(req, resp, shipperId);
            } else if (uri.contains("/api/shipper/deliveries")) {
                showDeliveries(req, resp, shipperId);
            } else if (uri.contains("/api/shipper/delivery/detail")) {
                showDeliveryDetail(req, resp, shipperId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/shipper/error.jsp").forward(req, resp);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        
        String uri = req.getRequestURI();
        HttpSession session = req.getSession();
        
        // Filter đã kiểm tra authentication và role, chỉ cần lấy user
        Users currentUser = (Users) session.getAttribute("currentUser");
        Long shipperId = currentUser.getUserID();
        
        try {
            if (uri.contains("/api/shipper/delivery/complete")) {
                completeDelivery(req, resp);
            } else if (uri.contains("/api/shipper/delivery/fail")) {
                failDelivery(req, resp);
            } else if (uri.contains("/api/shipper/order/accept")) {
                acceptOrder(req, resp, shipperId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/api/shipper/dashboard");
        }
    }
    
    private void showDashboard(HttpServletRequest req, HttpServletResponse resp, Long shipperId) 
            throws ServletException, IOException {
        
        // Thống kê số lượng đơn hàng theo trạng thái
        long totalDeliveries = deliveryService.countByShipperId(shipperId);
        long deliveringCount = deliveryService.countByShipperIdAndStatus(shipperId, "Đang giao hàng");
        long completedCount = deliveryService.countByShipperIdAndStatus(shipperId, "Đã giao hàng");
        
        // Lấy đơn hàng chưa có shipper (available orders)
        List<Orders> confirmedOrders = orderDao.findConfirmedOrders();
        long confirmedCount = orderDao.countConfirmedOrders();
        
        // Lấy danh sách đơn hàng đang giao của shipper này
        List<DeliveryDTO> activeDeliveries = deliveryService.findByShipperIdAndStatus(shipperId, "Đang giao hàng");
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDeliveries", totalDeliveries);
        stats.put("deliveringCount", deliveringCount);
        stats.put("completedCount", completedCount);
        stats.put("confirmedCount", confirmedCount);
        
        req.setAttribute("stats", stats);
        req.setAttribute("confirmedOrders", confirmedOrders);
        req.setAttribute("activeDeliveries", activeDeliveries);
        
        req.getRequestDispatcher("/WEB-INF/views/shipper/dashboard.jsp").forward(req, resp);
    }
    
    private void showDeliveries(HttpServletRequest req, HttpServletResponse resp, Long shipperId) 
            throws ServletException, IOException {
        
        String statusFilter = req.getParameter("status");
        List<DeliveryDTO> deliveries;
        
        if (statusFilter != null && !statusFilter.trim().isEmpty()) {
            deliveries = deliveryService.findByShipperIdAndStatus(shipperId, statusFilter);
        } else {
            deliveries = deliveryService.findByShipperId(shipperId);
        }
        
        req.setAttribute("deliveries", deliveries);
        req.setAttribute("currentStatus", statusFilter);
        
        req.getRequestDispatcher("/WEB-INF/views/shipper/deliveries.jsp").forward(req, resp);
    }
    
    private void showDeliveryDetail(HttpServletRequest req, HttpServletResponse resp, Long shipperId) 
            throws ServletException, IOException {
        
        String deliveryIdParam = req.getParameter("id");
        if (deliveryIdParam == null || deliveryIdParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/api/shipper/deliveries");
            return;
        }
        
        Long deliveryId = Long.parseLong(deliveryIdParam);
        DeliveryDTO delivery = deliveryService.findById(deliveryId);
        
        if (delivery == null) {
            req.setAttribute("error", "Đơn giao hàng không tồn tại!");
            req.getRequestDispatcher("/WEB-INF/views/shipper/error.jsp").forward(req, resp);
            return;
        }
        
        // Kiểm tra quyền truy cập
        if (!delivery.getShipperID().equals(shipperId)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền xem đơn hàng này!");
            return;
        }
        
        req.setAttribute("delivery", delivery);
        req.getRequestDispatcher("/WEB-INF/views/shipper/delivery-detail.jsp").forward(req, resp);
    }
    
    private void completeDelivery(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        Long deliveryId = Long.parseLong(req.getParameter("deliveryId"));
        deliveryService.completeDelivery(deliveryId);
        
        HttpSession session = req.getSession();
        session.setAttribute("success", "Đã hoàn thành giao hàng!");
        
        resp.sendRedirect(req.getContextPath() + "/api/shipper/delivery/detail?id=" + deliveryId);
    }
    
    private void failDelivery(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        Long deliveryId = Long.parseLong(req.getParameter("deliveryId"));
        String failureReason = req.getParameter("failureReason");
        
        if (failureReason == null || failureReason.trim().isEmpty()) {
            failureReason = "Không liên lạc được với khách hàng";
        }
        
        deliveryService.failDelivery(deliveryId, failureReason);
        
        HttpSession session = req.getSession();
        session.setAttribute("warning", "Đã báo giao hàng thất bại!");
        
        resp.sendRedirect(req.getContextPath() + "/api/shipper/delivery/detail?id=" + deliveryId);
    }
    
    /**
     * Shipper nhận đơn hàng
     */
    private void acceptOrder(HttpServletRequest req, HttpServletResponse resp, Long shipperId) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        
        try {
            String orderIdStr = req.getParameter("orderId");
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Order ID không hợp lệ");
            }
            
            Long orderId = Long.parseLong(orderIdStr);
            
            // Gán đơn hàng cho shipper
            deliveryService.assignOrderToShipper(orderId, shipperId, "Shipper đã nhận đơn");
            
            session.setAttribute("success", "Đã nhận đơn hàng thành công!");
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi khi nhận đơn: " + e.getMessage());
        }
        
        resp.sendRedirect(req.getContextPath() + "/api/shipper/dashboard");
    }
}
