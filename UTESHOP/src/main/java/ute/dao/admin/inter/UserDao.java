package ute.dao.admin.inter;

import ute.entities.Users;

public interface UserDao {
    Users findUserById(long userId);
}
