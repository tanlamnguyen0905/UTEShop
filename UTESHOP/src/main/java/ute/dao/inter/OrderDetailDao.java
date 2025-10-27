package ute.dao.inter;

import java.util.List;
import java.util.Map;
import ute.entities.OrderDetail;

public interface OrderDetailDao {
	// Basic CRUD
	void insert(OrderDetail orderDetail);
	void update(OrderDetail orderDetail);
	void delete(Long id);
	OrderDetail findById(Long id);
	List<OrderDetail> findAll();
	
	// Dashboard-specific queries
	
	// Get top products by quantity sold
	// Returns List of Maps with keys: "name" (String), "qty" (Long), "amount" (Double)
	List<Map<String, Object>> getTopProductsByQuantity(int limit);
	
	// Get top products by revenue
	// Returns List of Maps with keys: "name" (String), "qty" (Long), "amount" (Double)
	List<Map<String, Object>> getTopProductsByRevenue(int limit);
	
	// Get sales grouped by category
	// Returns List of Maps with keys: "name" (String - category name), "amount" (Double)
	List<Map<String, Object>> getSalesByCategory();
	
	// Get order details by order ID
	List<OrderDetail> findByOrderId(Long orderId);
}