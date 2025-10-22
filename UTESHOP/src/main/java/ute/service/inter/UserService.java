package ute.service.inter;

import java.util.List;

import ute.entities.Users;

public interface UserService {
	List<Users> search(String search, String role, String status);
    Users getById(Long id);
    boolean create(Users user);
    boolean update(Users user);
    boolean delete(Long id);
}