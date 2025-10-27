package ute.service.inter;

import java.util.List;
import java.util.Map;

import ute.dto.UpdateUserDTO;
import ute.dto.UserDTO;
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
	Users findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	void insert(Users user);
	
	// Admin methods using DTOs
	List<UserDTO> findAllUsers();
	
	UserDTO findUserById(Long userId);
	
	void updateUser(UpdateUserDTO dto) throws Exception;
	
	void deleteUser(Long userId) throws Exception;
	
	long countAllUsers();
	
	long countUsersByRole(String role);
	
}