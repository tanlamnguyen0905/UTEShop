package ute.service.impl;

import ute.dao.impl.UserDaoImpl;
import ute.entities.Users;
import ute.service.inter.UserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

	private final UserDaoImpl userDaoImpl = new UserDaoImpl();

	@Override
	public boolean register(Users user) {
		return userDaoImpl.register(user);
	}

	@Override
	public Optional<Users> login(String username, String password) {
		return Optional.ofNullable(userDaoImpl.login(username, password));
	}

	@Override
	public Optional<Users> findById(int id) {
		return Optional.ofNullable(userDaoImpl.findById(id));
	}

	@Override
	public List<Users> findAll() {
		return userDaoImpl.findAll();
	}

	@Override
	public List<Users> searchByNameOrEmail(String keyword) {
		return userDaoImpl.searchByNameOrEmail(keyword);
	}

	@Override
	public boolean update(Users user) {
		return userDaoImpl.update(user);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userDaoImpl.findAll().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
	}

	@Override
	public boolean existsByEmail(String email) {
		return userDaoImpl.findAll().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
	}

	@Override
	public Optional<Users> findByEmail(String email) {
		return userDaoImpl.findAll().stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
	}

	@Override
	public boolean updatePassword(String email, String newPassword) {
		Optional<Users> userOpt = findByEmail(email);
		if (userOpt.isPresent()) {
			Users user = userOpt.get();
			user.setPassword(newPassword);
			return userDaoImpl.update(user);
		}
		return false;
	}

	@Override
	public boolean delete(Long UserId) {
		// TODO Auto-generated method stub
		return false;
	}
}
