package ute.service.impl;

import ute.dao.impl.UserDaoImpl;
import ute.entities.Users;
import ute.service.inter.UserService;

public class UserServiceImpl implements UserService {

	private UserDaoImpl userDao = new UserDaoImpl();
	
	@Override
	public Users findByUsername(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userDao.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userDao.existsByEmail(email);
	}

	@Override
	public void insert(Users user) {
		userDao.insert(user);
	}

	@Override
	public void update(Users user) {
		userDao.update(user);
	}

}
