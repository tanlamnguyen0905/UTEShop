package ute.dao.inter;

import java.util.List;
import java.util.Map;
import ute.entities.Users;

public interface UserDao {
	List<Users> findAll();
	Users findById(Long id);
	Users findByUsername(String username);
	public List<Users> search(String keyword, String role, String status);

	boolean existsByUsername(String username);
	boolean existsByEmail(String email);

	boolean insert(Users user);
	boolean update(Users user);
	boolean delete(Long id);

	boolean activateUserByEmail(String email);

	// Count customers
	Long getTotalCustomerCount();
	
	// Count active customers
	Long getActiveCustomerCount();
	
	// Get top customers by order count
	// Returns List of Maps with keys: "name" (String), "orders" (Long), "spend" (Double)
	List<Map<String, Object>> getTopCustomersByOrderCount(int limit);
	
	// Get top customers by total spending
	// Returns List of Maps with keys: "name" (String), "orders" (Long), "spend" (Double)
	List<Map<String, Object>> getTopCustomersBySpending(int limit);
}
