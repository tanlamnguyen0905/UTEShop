package ute.controllers.manager.Order;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.entities.Orders;
import ute.service.impl.OrderServiceImpl;
import ute.service.inter.OrderService;

@WebServlet(urlPatterns = {
        "/api/manager/orders",  // List
        "/api/manager/orders/*"  // Detail
})
public class OrderManagerController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderService orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        String uri = req.getRequestURI();

        if (pathInfo == null || "/".equals(pathInfo)) {
            // List đơn hàng (paginated, filter status)
            int page = 1;
            int pageSize = 10;
            String pageStr = req.getParameter("page");
            String sizeStr = req.getParameter("size");
            String orderStatus = req.getParameter("orderStatus");
            String paymentStatus = req.getParameter("paymentStatus");

            if (pageStr != null && !pageStr.trim().isEmpty()) page = Integer.parseInt(pageStr);
            if (sizeStr != null && !sizeStr.trim().isEmpty()) pageSize = Integer.parseInt(sizeStr);

            List<Orders> orders = orderService.findByStatusPaginated(orderStatus, paymentStatus, page, pageSize);
            long totalOrders = orderService.countByStatus(orderStatus, paymentStatus);
            int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

            req.setAttribute("orders", orders);
            req.setAttribute("totalOrders", totalOrders);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("pageSize", pageSize);
            req.setAttribute("orderStatus", orderStatus);
            req.setAttribute("paymentStatus", paymentStatus);

            req.getRequestDispatcher("/WEB-INF/views/manager/orders/orders-list.jsp").forward(req, resp);

        } else {
            // Chi tiết đơn hàng theo ID
            String idStr = pathInfo.substring(1);  // Bỏ "/"
            if (idStr != null && !idStr.trim().isEmpty()) {
                try {
                    Long id = Long.parseLong(idStr.trim());
                    Orders order = orderService.findById(id);
                    if (order != null) {
                        Double total = orderService.calculateOrderTotal(order);
                        req.setAttribute("order", order);
                        req.setAttribute("total", total);
                        req.getRequestDispatcher("/WEB-INF/views/manager/orders/order-detail.jsp").forward(req, resp);
                        return;
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "ID đơn hàng không hợp lệ!");
                }
            }
            // Nếu không tìm thấy, redirect về list
            resp.sendRedirect(req.getContextPath() + "/api/manager/orders");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);  // Reuse cho filter
    }
}