package ute.service.admin.inter;

import ute.entities.Users;

public interface UserService {
    public Users findUserById(Long userId);
}
