package ute.service.impl;

import ute.entities.Users;
import ute.service.inter.UserService;

public class UserServiceImpl implements UserService {

	private static UserServiceImpl instance;
	@Override
	public Users findByUsername(String username) {
		// TODO Auto-generated method stub
		return instance.findByUsername(username);
	}

	@Override
	public boolean existsByUsername(String username) {
		// TODO Auto-generated method stub
		return instance.existsByUsername(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		// TODO Auto-generated method stub
		return instance.existsByEmail(email);
	}

	@Override
	public void insert(Users user) {
		// TODO Auto-generated method stub
		instance.insert(user);
	}

	@Override
	public void update(Users user) {
		// TODO Auto-generated method stub
		instance.update(user);
	}

}
