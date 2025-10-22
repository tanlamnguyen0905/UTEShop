package ute.service.impl;

import java.util.List;

import ute.dao.inter.UserDao;
import ute.entities.Users;
import ute.service.inter.UserService;

public class UserServiceImpl implements UserService {
	private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<Users> search(String search, String role, String status) {
        return userDao.search(search, role, status);
    }

    @Override
    public Users getById(Long id) {
        return userDao.findById(id);
    }

    @Override
    public boolean create(Users user) {
        return userDao.insert(user);
    }

    @Override
    public boolean update(Users user) {
        return userDao.update(user);
    }

    @Override
    public boolean delete(Long id) {
        return userDao.delete(id);
    }
}
