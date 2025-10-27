package ute.service.inter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderService {
    
    // Total revenue calculation
    Double getTotalRevenue();
    Double getTotalRevenueByDateRange(LocalDate startDate, LocalDate endDate);
    
    // Order counts
    Long getTotalOrderCount();
    Long getOrderCountByStatus(String status);
    
    // Order status distribution
    List<Map<String, Object>> getOrderStatusDistribution();
    
    // Daily revenue breakdown
    List<Map<String, Object>> getDailyRevenue(LocalDate startDate, LocalDate endDate);
}