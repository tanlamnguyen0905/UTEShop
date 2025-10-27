package ute.service.admin.Impl;

import ute.dao.admin.Impl.UserDaoImpl;
import ute.dao.admin.inter.UserDao;
import ute.entities.Users;
import ute.service.admin.inter.UserService;

public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();
    @Override
    public Users findUserById(Long userId) {
        return userDao.findUserById(userId);
    }
}
