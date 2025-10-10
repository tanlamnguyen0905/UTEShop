package ute.services.impl;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;

import ute.dao.UserDao;
import ute.entities.Users;
import ute.services.IUserService;

public class UserServiceImpl implements IUserService {

    private UserDao userDAO = new UserDao();

    @Override
    public Users register(Users user) {
        // Kiểm tra trùng username
        if (userDAO.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // Kiểm tra trùng email
        if (userDAO.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Mã hoá mật khẩu trước khi lưu
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);

        // Lưu user
        userDAO.save(user);
        return user;
    }

    @Override
    public Optional<Users> login(String username, String password) {
        Optional<Users> u = userDAO.findByUsername(username);

        if (u.isPresent()) {
            Users user = u.get();
            // So sánh mật khẩu (hash)
            if (BCrypt.checkpw(password, user.getPassword())) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean existsByUsername(String username) {
        return userDAO.findByUsername(username).isPresent();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDAO.findByEmail(email).isPresent();
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public void updatePassword(String email, String newPassword) {
        Optional<Users> u = userDAO.findByEmail(email);

        if (u.isPresent()) {
            Users user = u.get();
            // Mã hoá mật khẩu mới
            user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            userDAO.save(user);
        } else {
            throw new RuntimeException("Email not found");
        }
    }
}