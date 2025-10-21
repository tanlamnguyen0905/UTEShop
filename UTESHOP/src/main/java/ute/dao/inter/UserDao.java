package ute.dao.inter;

import ute.entities.Users;

public interface UserDao {

	Users findByUsername(String username);

	Users findByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	void insert(Users user);

	void update(Users user);

	void delete(Long userId);

	boolean activateUserByEmail(String email);
}
