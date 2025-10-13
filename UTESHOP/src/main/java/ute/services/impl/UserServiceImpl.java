package ute.services.impl;

import ute.dao.UserDao;
import ute.entities.Users;
import ute.services.IUserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements IUserService {

	private final UserDao userDao = new UserDao();

	@Override
	public boolean register(Users user) {
		return userDao.register(user);
	}

	@Override
	public Optional<Users> login(String username, String password) {
		return Optional.ofNullable(userDao.login(username, password));
	}

	@Override
	public Optional<Users> findById(int id) {
		return Optional.ofNullable(userDao.findById(id));
	}

	@Override
	public List<Users> findAll() {
		return userDao.findAll();
	}

	@Override
	public List<Users> searchByNameOrEmail(String keyword) {
		return userDao.searchByNameOrEmail(keyword);
	}

	@Override
	public boolean update(Users user) {
		return userDao.update(user);
	}

	@Override
	public boolean delete(int id) {
		return userDao.delete(id);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userDao.findAll().stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
	}

	@Override
	public boolean existsByEmail(String email) {
		return userDao.findAll().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
	}

	@Override
	public Optional<Users> findByEmail(String email) {
		return userDao.findAll().stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
	}

	@Override
	public boolean updatePassword(String email, String newPassword) {
		Optional<Users> userOpt = findByEmail(email);
		if (userOpt.isPresent()) {
			Users user = userOpt.get();
			user.setPassword(newPassword);
			return userDao.update(user);
		}
		return false;
	}
}
