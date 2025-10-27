package ute.service.inter;

import java.util.List;
import java.util.Map;

import ute.entities.Users;

public interface UserService {
	List<Users> search(String search, String role, String status);
    Users getById(Long id);
    boolean create(Users user);
    boolean update(Users user);
    boolean delete(Long id);

    // Customer counts
    Long getTotalCustomerCount();
    Long getActiveCustomerCount();
    
    // Top customers by order count or spending
    List<Map<String, Object>> getTopCustomersByOrderCount(int limit);
    List<Map<String, Object>> getTopCustomersBySpending(int limit);
}