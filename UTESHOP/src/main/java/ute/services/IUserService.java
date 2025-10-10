package ute.services;

import ute.entities.Users;
import java.util.Optional;

public interface IUserService {
	Users register(Users user);

	Optional<Users> login(String username, String password);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	Optional<Users> findByEmail(String email);

	void updatePassword(String email, String newPassword);
}