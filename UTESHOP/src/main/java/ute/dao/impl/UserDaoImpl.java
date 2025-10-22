package ute.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import ute.entities.Users;
import ute.configs.JPAConfig;
import ute.dao.inter.UserDao;

import java.time.LocalDateTime;
import java.util.List;

public class UserDaoImpl implements UserDao {

    @Override
    public Users findByUsername(String username) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Users> query = em.createQuery(
                    "SELECT u FROM Users u WHERE u.username = :username", Users.class);
            query.setParameter("username", username);
            List<Users> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(u) FROM Users u WHERE u.username = :username", Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            Long count = em.createQuery(
                    "SELECT COUNT(u) FROM Users u WHERE u.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    @Override
    public void insert(Users user) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Users user) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            // Tìm user hiện có trong DB
            Users existingUser = em.find(Users.class, user.getUserID());
            if (existingUser == null)
                throw new IllegalArgumentException("Không tìm thấy người dùng với ID: " + user.getUserID());

            // Cập nhật các thông tin cho phép thay đổi
            // (giữ nguyên các quan hệ và dữ liệu hệ thống như createAt, role, status nếu
            // bạn muốn)

            if (user.getFullname() != null)
                existingUser.setFullname(user.getFullname());
            if (user.getEmail() != null)
                existingUser.setEmail(user.getEmail());
            if (user.getPhone() != null)
                existingUser.setPhone(user.getPhone());
            if (user.getAvatar() != null)
                existingUser.setAvatar(user.getAvatar());

            // Nếu có cập nhật mật khẩu (đã mã hóa sẵn trước khi truyền vào)
            if (user.getPassword() != null && !user.getPassword().isEmpty())
                existingUser.setPassword(user.getPassword());

            // Nếu có cập nhật trạng thái hoặc quyền
            if (user.getStatus() != null)
                existingUser.setStatus(user.getStatus());
            if (user.getRole() != null)
                existingUser.setRole(user.getRole());

            // Cập nhật thời gian đăng nhập gần nhất (nếu có)
            if (user.getLastLoginAt() != null)
                existingUser.setLastLoginAt(user.getLastLoginAt());

            em.merge(existingUser);
            tx.commit();

        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean activateUserByEmail(String email) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Tìm user theo email
            Users user = em.createQuery(
                    "SELECT u FROM Users u WHERE u.email = :email", Users.class)
                    .setParameter("email", email)
                    .getSingleResult();

            if (user != null) {
                user.setStatus("ACTIVE");
                user.setLastLoginAt(LocalDateTime.now());
                em.merge(user);
            }

            tx.commit();
            return true;
        } catch (NoResultException e) {
            if (tx.isActive())
                tx.rollback();
            return false;
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

}
