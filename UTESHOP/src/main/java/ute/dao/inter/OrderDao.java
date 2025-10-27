package ute.dao.inter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import ute.entities.Orders;

public interface OrderDao {
	// Basic CRUD
	void insert(Orders order);
	void update(Orders order);
	void delete(Long id);
	Orders findById(Long id);
	List<Orders> findAll();
	
	// Dashboard-specific queries
	
	// Total revenue (sum of all orders' amount)
	Double getTotalRevenue();
	
	// Total revenue within date range
	Double getTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate);
	
	// Count total orders
	Long getTotalOrderCount();
	
	// Count orders by specific status
	Long getOrderCountByStatus(String status);
	
	// Get order status distribution (status name + count)
	// Returns List of Maps with keys: "status" (String), "count" (Long)
	List<Map<String, Object>> getOrderStatusDistribution();
	
	// Get daily revenue for date range
	// Returns List of Maps with keys: "date" (String in yyyy-MM-dd), "total" (Double)
	List<Map<String, Object>> getDailyRevenue(LocalDateTime startDate, LocalDateTime endDate);
	
	// Get orders by user
	List<Orders> findByUserId(Long userId);
}