package ute.service.inter;

import ute.entities.Users;

public interface UserService {
	Users findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	void insert(Users user);

	void update(Users user);
	
}