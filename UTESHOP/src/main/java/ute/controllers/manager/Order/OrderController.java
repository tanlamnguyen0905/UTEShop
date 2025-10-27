package ute.controllers.manager.Order;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.service.impl.OrderServiceImpl;
import ute.service.inter.OrderService;
import ute.utils.RevenueStats;

@WebServlet(urlPatterns = {
        "/api/manager/orders/revenue"
})
public class OrderController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderService orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        try {
            // Default: Tháng hiện tại (dựa trên ngày 27/10/2025)
            LocalDate now = LocalDate.now();  // 2025-10-27
            LocalDate fromDate = now.withDayOfMonth(1);  // 2025-10-01
            LocalDate toDate = now;  // 2025-10-27

            String fromStr = req.getParameter("fromDate");
            String toStr = req.getParameter("toDate");
            if (fromStr != null && !fromStr.trim().isEmpty()) {
                try {
                    fromDate = LocalDate.parse(fromStr.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException e) {
                    req.setAttribute("error", "Ngày bắt đầu không hợp lệ: " + e.getMessage());
                }
            }
            if (toStr != null && !toStr.trim().isEmpty()) {
                try {
                    toDate = LocalDate.parse(toStr.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException e) {
                    req.setAttribute("error", "Ngày kết thúc không hợp lệ: " + e.getMessage());
                }
            }

            // Đảm bảo fromDate <= toDate
            if (fromDate.isAfter(toDate)) {
                req.setAttribute("error", "Ngày bắt đầu phải trước hoặc bằng ngày kết thúc!");
                fromDate = toDate.withDayOfMonth(1);  // Reset default
            }

            // Fix: Format string cho JSP (tránh lỗi <fmt:formatDate> với LocalDate)
            DateTimeFormatter isoFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");  // Cho input date
            DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");  // Cho hiển thị
            String fromDateStr = fromDate.format(isoFormatter);
            String toDateStr = toDate.format(isoFormatter);
            String fromDisplay = fromDate.format(displayFormatter);
            String toDisplay = toDate.format(displayFormatter);

            // Set attributes string cho JSP
            req.setAttribute("fromDateStr", fromDateStr);
            req.setAttribute("toDateStr", toDateStr);
            req.setAttribute("fromDisplay", fromDisplay);
            req.setAttribute("toDisplay", toDisplay);

            // Gọi service thống kê
            List<RevenueStats> dailyRevenues = orderService.getDailyRevenue(fromDate, toDate);
            RevenueStats totalStats = orderService.getTotalRevenueStats(fromDate, toDate);

            // Set attributes cho JSP
            req.setAttribute("fromDate", fromDate);
            req.setAttribute("toDate", toDate);
            req.setAttribute("dailyRevenues", dailyRevenues);
            req.setAttribute("totalStats", totalStats);

            // Forward đến JSP
            req.getRequestDispatcher("/WEB-INF/views/manager/orders/revenue.jsp").forward(req, resp);
        } catch (Exception e) {
            // Log cho debug (xem trong console server)
            e.printStackTrace();
            req.setAttribute("error", "Lỗi thống kê doanh thu: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Reuse doGet cho POST (filter form)
        doGet(req, resp);
    }
}