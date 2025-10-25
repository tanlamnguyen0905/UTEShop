package ute.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import ute.dao.impl.UserDaoImpl;
import ute.dto.UpdateUserDTO;
import ute.dto.UserDTO;
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

	// ==================== ADMIN METHODS USING DTOs ====================
	
	@Override
	public List<UserDTO> findAllUsers() {
		List<Users> users = userDao.findAll();
		return users.stream()
			.map(UserDTO::fromEntity)
			.collect(Collectors.toList());
	}

	@Override
	public UserDTO findUserById(Long userId) {
		Users user = userDao.findById(userId);
		return UserDTO.fromEntity(user);
	}

	@Override
	public void updateUser(UpdateUserDTO dto) throws Exception {
		// Validate
		if (!dto.isValid()) {
			throw new IllegalArgumentException(dto.getValidationError());
		}
		
		// Check if user exists
		Users existingUser = userDao.findById(dto.getUserID());
		if (existingUser == null) {
			throw new IllegalArgumentException("Không tìm thấy người dùng với ID: " + dto.getUserID());
		}
		
		// Check duplicate email (nếu đổi email)
		if (!existingUser.getEmail().equals(dto.getEmail())) {
			if (userDao.existsByEmail(dto.getEmail())) {
				throw new IllegalArgumentException("Email đã được sử dụng bởi người dùng khác");
			}
		}
		
		// Update entity (không update password và avatar ở đây)
		Users updatedUser = Users.builder()
			.userID(dto.getUserID())
			.fullname(dto.getFullname())
			.email(dto.getEmail())
			.phone(dto.getPhone())
			.avatar(existingUser.getAvatar())
			.role(dto.getRole())
			.status(dto.getStatus())
			.build();
		
		userDao.update(updatedUser);
	}

	@Override
	public void deleteUser(Long userId) throws Exception {
		// Check if user exists
		Users user = userDao.findById(userId);
		if (user == null) {
			throw new IllegalArgumentException("Không tìm thấy người dùng với ID: " + userId);
		}
		
		// Không cho xóa admin cuối cùng
		if ("ADMIN".equalsIgnoreCase(user.getRole())) {
			long adminCount = userDao.countByRole("ADMIN");
			if (adminCount <= 1) {
				throw new IllegalArgumentException("Không thể xóa admin cuối cùng của hệ thống");
			}
		}
		
		userDao.delete(userId);
	}

	@Override
	public long countAllUsers() {
		return userDao.countAll();
	}

	@Override
	public long countUsersByRole(String role) {
		return userDao.countByRole(role);
	}

}
