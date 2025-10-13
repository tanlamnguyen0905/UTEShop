package ute.dao.inter;

import java.util.List;

import ute.entities.Users;

public interface UserDao {

	boolean register(Users user);

	Users login(String username, String password);

	Users findById(int id);

	boolean update(Users user);
	
	boolean delete(Long UserId);
	
	List<Users> findAll();
	
	List<Users> searchByNameOrEmail(String keyword);
}
