package ute.dao.inter;

import java.util.List;

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
}
