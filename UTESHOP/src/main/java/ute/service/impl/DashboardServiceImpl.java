package ute.service.impl;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ute.dao.impl.UserDaoImpl;
import ute.service.inter.DashboardService;
import ute.service.inter.OrderService;
import ute.service.inter.ProductService;
import ute.service.inter.ReviewService;
import ute.service.inter.UserService;

public class DashboardServiceImpl implements DashboardService {
    
    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;
    private final ReviewService reviewService;
    
    public DashboardServiceImpl() {
        this.orderService = new OrderServiceImpl();
        this.productService = new ProductServiceImpl();
        this.userService = new UserServiceImpl();
        this.reviewService = new ReviewServiceImpl();
    }

    @Override
    public Map<String, Object> getOverallStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRevenue", orderService.getTotalRevenue());
        stats.put("totalOrders", orderService.getTotalOrderCount());
        stats.put("totalCustomers", userService.getTotalCustomerCount());
        stats.put("totalProducts", productService.getTotalProductCount());
        stats.put("totalReviews", reviewService.getTotalReviewCount());
        stats.put("avgStars", reviewService.getAverageRating());
        return stats;
    }

    @Override
    public List<Map<String, Object>> getRevenueDailyByDateRange(LocalDate startDate, LocalDate endDate) {
        return orderService.getDailyRevenue_2(startDate, endDate);
    }

    @Override
    public Map<String, Long> countOrdersByStatus() {
        return orderService.countOrdersByStatus();
    }

    @Override
    public List<Map<String, Object>> getTopProducts(int limit) {
        return productService.getTopProductsByRevenue(limit);
    }

    @Override
    public List<Map<String, Object>> getTopCustomers(int limit) {
        return userService.getTopCustomersBySpending(limit);
    }

    @Override
    public List<Map<String, Object>> getCategorySales() {
        return productService.getSalesByCategory();
    }
}
