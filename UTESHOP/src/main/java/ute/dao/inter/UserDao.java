package ute.dao.inter;

import java.util.List;
import ute.entities.Users;

public interface UserDao {

	Users findByUsername(String username);

	Users findByEmail(String email);
	
	Users findById(Long userId);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	void insert(Users user);

	void update(Users user);

	void delete(Long userId);

	boolean activateUserByEmail(String email);
	
	List<Users> findByRole(String role);
	
	List<Users> findAll();
	
	long countAll();
	
	long countByRole(String role);
}
