package ute.service.inter;

import ute.entities.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {
	// Đăng ký user mới
	boolean register(Users user);

	// Đăng nhập
	Optional<Users> login(String username, String password);

	// Tìm theo id
	Optional<Users> findById(int id);

	// Lấy tất cả
	List<Users> findAll();

	// Tìm kiếm theo keyword
	List<Users> searchByNameOrEmail(String keyword);

	// Cập nhật user
	boolean update(Users user);

	// Xóa user
	boolean delete(Long UserId);

	// Kiểm tra tồn tại
	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	// Tìm theo email
	Optional<Users> findByEmail(String email);

	// Đổi mật khẩu
	boolean updatePassword(String email, String newPassword);
}