package ute.controllers.manager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ute.entities.Orders;
import ute.service.impl.OrderServiceImpl;
import ute.service.inter.OrderService;
import ute.utils.RevenueStats;

@WebServlet(urlPatterns = {"/api/manager/dashboard"})
public class ManagerController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderService orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try {
            LocalDate now = LocalDate.now();
            LocalDate monthStart = now.withDayOfMonth(1);
            LocalDate monthEnd = now;

            // Tổng đơn hàng
            List<Orders> allOrders = orderService.findAll();
            long totalOrders = allOrders.size();

            // Doanh thu tháng hiện tại
            RevenueStats monthlyRevenue = orderService.getTotalRevenueStats(monthStart, monthEnd);

            // Đơn hàng đang chờ (sử dụng orderStatus thay vì status)
            List<Orders> pendingOrdersList = orderService.findByStatus("Đang chờ");
            long pendingOrders = pendingOrdersList.size();

            // Tăng trưởng (so sánh với tháng trước)
            LocalDate prevMonthStart = monthStart.minusMonths(1);
            LocalDate prevMonthEnd = monthStart.minusDays(1);
            RevenueStats prevMonthRevenue = orderService.getTotalRevenueStats(prevMonthStart, prevMonthEnd);
            double growthRate = prevMonthRevenue.getRevenue() > 0
                    ? ((monthlyRevenue.getRevenue() - prevMonthRevenue.getRevenue()) / prevMonthRevenue.getRevenue() * 100)
                    : 0.0;

            // Đơn hàng gần đây (5 đơn mới nhất)
            List<Orders> recentOrders = allOrders.stream()
                    .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                    .limit(5)
                    .toList();

            // Doanh thu theo tháng (cho chart, 12 tháng gần nhất)
            List<RevenueStats> monthlyRevenues = getMonthlyRevenues(now);

            // Set attributes
            req.setAttribute("totalOrders", totalOrders);
            req.setAttribute("totalRevenue", monthlyRevenue.getRevenue());
            req.setAttribute("pendingOrders", pendingOrders);
            req.setAttribute("growthRate", growthRate);
            req.setAttribute("recentOrders", recentOrders);
            req.setAttribute("monthlyRevenues", monthlyRevenues);

            // Forward to dashboard JSP
            req.getRequestDispatcher("/WEB-INF/views/manager/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi tải dashboard: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
        }
    }

    // Helper method: Lấy doanh thu 12 tháng gần nhất
    private List<RevenueStats> getMonthlyRevenues(LocalDate now) {
        List<RevenueStats> revenues = new ArrayList<>();
        for (int i = 11; i >= 0; i--) {
            LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            RevenueStats stats = orderService.getTotalRevenueStats(monthStart, monthEnd);
            revenues.add(new RevenueStats(stats.getRevenue(), 0L, 0.0));
        }
        return revenues;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}