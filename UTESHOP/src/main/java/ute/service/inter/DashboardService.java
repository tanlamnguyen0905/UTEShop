package ute.service.inter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DashboardService {
    // Overall statistics
    Map<String, Object> getOverallStats();
    
    // Revenue by date range
    List<Map<String, Object>> getRevenueDailyByDateRange(LocalDate startDate, LocalDate endDate);
    
    // Order status distribution
    Map<String, Long> countOrdersByStatus();
    
    // Top performing products
    List<Map<String, Object>> getTopProducts(int limit);
    
    // Top customers by spending
    List<Map<String, Object>> getTopCustomers(int limit);
    
    // Sales by category
    List<Map<String, Object>> getCategorySales();
}
