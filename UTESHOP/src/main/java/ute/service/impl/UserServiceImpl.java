package ute.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;

import ute.dao.impl.UserDaoImpl;
import ute.dto.UpdateUserDTO;
import ute.dto.UserDTO;
import ute.entities.Users;
import ute.service.inter.UserService;

public class UserServiceImpl implements UserService {
	private UserDaoImpl userDao = new UserDaoImpl();

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
