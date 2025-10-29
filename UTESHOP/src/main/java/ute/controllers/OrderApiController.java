package ute.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ute.configs.JPAConfig;
import ute.entities.OrderDetail;
import ute.entities.Orders;
import ute.entities.Users;
import ute.service.impl.OrderServiceImpl;
import ute.service.inter.OrderService;

// Changed mapping to a simple prefix mapping so containers reliably match URLs like /api/order/{id}/products
@WebServlet(urlPatterns = { "/api/order/*" })
public class OrderApiController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        this.orderService = new OrderServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            HttpSession session = req.getSession();
            Users user = (Users) session.getAttribute("user");
            if (user == null) {
                user = (Users) session.getAttribute("currentUser");
            }

            if (user == null) {
                out.print("{\"success\":false,\"message\":\"Vui lòng đăng nhập để thực hiện hành động này\"}");
                return;
            }

            // Extract order ID from URL. Expecting paths like: /{orderId}/cancel
            String pathInfo = req.getPathInfo(); // e.g. "/123/cancel"
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.trim().isEmpty()) {
                out.print("{\"success\":false,\"message\":\"URL không hợp lệ\"}");
                return;
            }

            String[] parts = pathInfo.split("/");
            if (parts.length < 3 || parts[1].trim().isEmpty()) {
                out.print("{\"success\":false,\"message\":\"ID đơn hàng không hợp lệ\"}");
                return;
            }

            String orderIdStr = parts[1];
            String action = parts[2];

            if (!"cancel".equalsIgnoreCase(action)) {
                out.print("{\"success\":false,\"message\":\"Hành động không được hỗ trợ\"}");
                return;
            }

            Long orderId;
            try {
                orderId = Long.parseLong(orderIdStr);
            } catch (NumberFormatException nfe) {
                out.print("{\"success\":false,\"message\":\"ID đơn hàng không hợp lệ\"}");
                return;
            }

            // Get the order and verify ownership
            Orders order = orderService.findById(orderId);
            if (order == null) {
                out.print("{\"success\":false,\"message\":\"Không tìm thấy đơn hàng\"}");
                return;
            }

            if (order.getUser() == null || !order.getUser().getUserID().equals(user.getUserID())) {
                out.print("{\"success\":false,\"message\":\"Bạn không có quyền hủy đơn hàng này\"}");
                return;
            }

            // Get cancel reason from request
            String reason = req.getParameter("reason");
            if (reason == null || reason.trim().isEmpty()) {
                reason = "Khách hàng yêu cầu hủy";
            }

            // Cancel the order
            orderService.cancelOrder(orderId, reason);
            
            out.print("{\"success\":true,\"message\":\"Đơn hàng đã được hủy thành công\"}");

        } catch (RuntimeException e) {
            out.print("{\"success\":false,\"message\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Lỗi hệ thống. Vui lòng thử lại sau\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            HttpSession session = req.getSession();
            Users user = (Users) session.getAttribute("user");
            if (user == null) {
                user = (Users) session.getAttribute("currentUser");
            }

            if (user == null) {
                out.print("{\"error\":\"Not authenticated\"}");
                return;
            }

            // Extract order ID from URL. Expecting paths like: /{orderId}/products
            String pathInfo = req.getPathInfo(); // e.g. "/123/products"
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.trim().isEmpty()) {
                out.print("{\"error\":\"Invalid URL\"}");
                return;
            }

            String[] parts = pathInfo.split("/");
            // parts[0] is empty because path starts with '/'
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                out.print("{\"error\":\"Invalid order ID\"}");
                return;
            }

            String orderIdStr = parts[1];
            Long orderId;
            try {
                orderId = Long.parseLong(orderIdStr);
            } catch (NumberFormatException nfe) {
                out.print("{\"error\":\"Invalid order ID format\"}");
                return;
            }

            // Optionally ensure the request was for products (client uses /{id}/products)
            if (parts.length < 3 || !"products".equalsIgnoreCase(parts[2])) {
                // If it's not the expected /{id}/products endpoint, return a helpful message
                out.print("{\"error\":\"Expected '/{orderId}/products' path\"}");
                return;
            }

            // Get order details
            Orders order = orderService.findById(orderId);
            if (order == null) {
                System.out.println("Order not found for ID: " + orderId);
                out.print("{\"error\":\"Order not found\"}");
                return;
            }

            // Fallback: if orderDetails is null/empty, try to explicitly load them via JPA
            if (order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
                System.out.println("Order details empty from service for ID " + orderId + ", attempting explicit query...");
                EntityManager em = JPAConfig.getEntityManager();
                try {
                    TypedQuery<OrderDetail> q = em.createQuery(
                        "SELECT od FROM OrderDetail od " +
                        "JOIN FETCH od.product p " +
                        "LEFT JOIN FETCH p.images imgs " +
                        "WHERE od.order.orderID = :id",
                        OrderDetail.class
                    );
                    q.setParameter("id", orderId);
                    List<OrderDetail> details = q.getResultList();
                    if (details != null && !details.isEmpty()) {
                        order.setOrderDetails(details);
                        System.out.println("Loaded " + details.size() + " orderDetails via fallback query for order " + orderId);
                    } else {
                        System.out.println("Fallback query returned no details for order " + orderId);
                    }
                } catch (Exception e) {
                    System.out.println("Fallback query failed: " + e.getMessage());
                } finally {
                    em.close();
                }
            }

            // Check if user owns this order
            if (order.getUser() == null || !order.getUser().getUserID().equals(user.getUserID())) {
                System.out.println("Access denied for user: " + user.getUserID() + " on order: " + orderId);
                out.print("{\"error\":\"Access denied\"}");
                return;
            }

            System.out.println("Found order: " + order.getOrderID() + " with " + 
                (order.getOrderDetails() != null ? order.getOrderDetails().size() : 0) + " details");

            // Create response object
            OrderProductsResponse response = new OrderProductsResponse();
            response.orderID = order.getOrderID();

            if (order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
                System.out.println("Order has no order details!");
                response.orderDetails = new java.util.ArrayList<>();
            } else {
                response.orderDetails = order.getOrderDetails().stream().map(detail -> {
                    OrderDetailResponse detailResp = new OrderDetailResponse();
                    detailResp.productId = detail.getProduct().getProductID();
                    detailResp.productName = detail.getProduct().getProductName();
                    detailResp.productImage = (detail.getProduct().getImages() == null || detail.getProduct().getImages().isEmpty()) ? 
                        "" : detail.getProduct().getImages().get(0).getDirImage();
                    detailResp.quantity = detail.getQuantity();
                    System.out.println("Added product: " + detailResp.productName + " (ID: " + detailResp.productId + ")");
                    return detailResp;
                }).toList();
            }

            Gson gson = new GsonBuilder().create();
            String jsonResponse = gson.toJson(response);
            System.out.println("Sending response: " + jsonResponse);
            out.print(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"Server error\"}");
        }
    }

    // Response classes
    public static class OrderProductsResponse {
        public Long orderID;
        public List<OrderDetailResponse> orderDetails;
    }

    public static class OrderDetailResponse {
        public Long productId;
        public String productName;
        public String productImage;
        public Integer quantity;
    }
}