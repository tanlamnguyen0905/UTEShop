package ute.service.impl;

import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

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
    	if (user.getPassword().isEmpty())
    		return false;
    	user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return userDao.insert(user);
    }

    @Override
    public boolean update(Users user) {
    	if (!user.getPassword().isEmpty())
    		user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        return userDao.update(user);
    }

    @Override
    public boolean delete(Long id) {
        return userDao.delete(id);
    }

    @Override
    public Long getTotalCustomerCount() {
        return userDao.getTotalCustomerCount();
    }

    @Override
    public Long getActiveCustomerCount() {
        return userDao.getActiveCustomerCount();
    }

    @Override
    public List<Map<String, Object>> getTopCustomersByOrderCount(int limit) {
        return userDao.getTopCustomersByOrderCount(limit);
    }

    @Override
    public List<Map<String, Object>> getTopCustomersBySpending(int limit) {
        return userDao.getTopCustomersBySpending(limit);
    }
}
