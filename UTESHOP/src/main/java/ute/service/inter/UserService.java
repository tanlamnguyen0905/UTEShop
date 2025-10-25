package ute.service.inter;

import java.util.List;

import ute.dto.UpdateUserDTO;
import ute.dto.UserDTO;
import ute.entities.Users;

public interface UserService {
	Users findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	void insert(Users user);

	void update(Users user);
	
	// Admin methods using DTOs
	List<UserDTO> findAllUsers();
	
	UserDTO findUserById(Long userId);
	
	void updateUser(UpdateUserDTO dto) throws Exception;
	
	void deleteUser(Long userId) throws Exception;
	
	long countAllUsers();
	
	long countUsersByRole(String role);
	
}