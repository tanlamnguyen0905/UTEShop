package ute.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.UserDao;
import ute.entities.Users;

public class UserDaoImpl implements UserDao {

	@Override
	public List<Users> search(String keyword, String role, String status) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			StringBuilder jpql = new StringBuilder("SELECT u FROM Users u WHERE 1=1");
			boolean hasKeyword = keyword != null && !keyword.isEmpty();
			boolean hasRole = role != null && !role.isEmpty();
			boolean hasStatus = status != null && !status.isEmpty();

			if (hasKeyword)
				jpql.append(" AND (u.username LIKE :kw OR u.fullname LIKE :kw OR u.email LIKE :kw OR u.phone LIKE :kw)");
			if (hasRole)
				jpql.append(" AND u.role = :role");
			if (hasStatus)
				jpql.append(" AND u.status = :status");

			TypedQuery<Users> query = em.createQuery(jpql.toString(), Users.class);

			if (hasKeyword)
				query.setParameter("kw", "%" + keyword + "%");
			if (hasRole)
				query.setParameter("role", role);
			if (hasStatus)
				query.setParameter("status", status);

			return query.getResultList();
		} finally {
			em.close();
		}
	}

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
	public boolean insert(Users user) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		boolean result = false;
		try {
			tx.begin();
			em.persist(user);
			tx.commit();
			result = true;
		} catch (Exception e) {
			if (tx.isActive()) tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
		return result;
	}

	@Override
	public boolean update(Users user) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		boolean result = false;
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
			result = true;
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
		return result;
	}

	@Override
	public boolean delete(Long id) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction tx = em.getTransaction();
		boolean result = false;
		try {
			tx.begin();
			Users user = em.find(Users.class, id);
			if (user != null) {
				em.remove(user);
			}
			tx.commit();
			result = true;
		} catch (Exception e) {
			if (tx.isActive()) tx.rollback();
			e.printStackTrace();
			throw new RuntimeException("Lỗi khi xóa tài khoản: " + e.getMessage(), e);
		} finally {
			em.close();
		}
		return result;
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

    @Override
    public Users findByEmail(String email) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Users> query = em.createQuery(
                    "SELECT u FROM Users u WHERE u.email = :email", Users.class);
            query.setParameter("email", email);
            List<Users> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

	@Override
	public Users findById(Long userId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			return em.find(Users.class, userId);
		} finally {
			em.close();
		}
	}

	@Override
	public List<Users> findByRole(String role) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Users> query = em.createQuery(
				"SELECT u FROM Users u WHERE u.role = :role AND u.status = 'active' ORDER BY u.fullname", 
				Users.class);
			query.setParameter("role", role);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Users> findAll() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Users> query = em.createQuery(
				"SELECT u FROM Users u ORDER BY u.createAt DESC", 
				Users.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public long countAll() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			return em.createQuery("SELECT COUNT(u) FROM Users u", Long.class)
				.getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public long countByRole(String role) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			return em.createQuery(
				"SELECT COUNT(u) FROM Users u WHERE u.role = :role", Long.class)
				.setParameter("role", role)
				.getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public Long getTotalCustomerCount() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Long> query = em.createQuery(
				"SELECT COUNT(u) FROM Users u WHERE u.role = 'USER'",
				Long.class
			);
			return query.getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public Long getActiveCustomerCount() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Long> query = em.createQuery(
				"SELECT COUNT(u) FROM Users u WHERE u.role = 'USER' AND u.status = 'ACTIVE'",
				Long.class
			);
			return query.getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Map<String, Object>> getTopCustomersByOrderCount(int limit) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Object[]> query = em.createQuery(
				"SELECT u.fullname, COUNT(o), COALESCE(SUM(o.amount), 0.0) " +
				"FROM Users u " +
				"JOIN u.orders o " +
				"WHERE u.role = 'USER' " +
				"GROUP BY u.userID, u.fullname " +
				"ORDER BY COUNT(o) DESC",
				Object[].class
			);
			query.setMaxResults(limit);
			
			List<Object[]> results = query.getResultList();
			List<Map<String, Object>> topCustomers = new ArrayList<>();
			
			for (Object[] row : results) {
				Map<String, Object> map = new HashMap<>();
				map.put("name", row[0]);
				map.put("orders", row[1]);
				map.put("spend", row[2]);
				topCustomers.add(map);
			}
			return topCustomers;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Map<String, Object>> getTopCustomersBySpending(int limit) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT u.fullname, COUNT(o), COALESCE(SUM(o.amount - o.totalDiscount + o.shippingFee), 0.0) " +
                      "FROM Users u " +
                      "JOIN u.orders o " +
                      "WHERE u.role = 'USER'" +
                      "GROUP BY u.userID, u.fullname " +
                      "ORDER BY SUM(o.amount - o.totalDiscount + o.shippingFee) DESC";

			TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
			query.setMaxResults(limit);

			List<Object[]> results = query.getResultList();
			List<Map<String, Object>> topCustomers = new ArrayList<>();

			for (Object[] row : results) {
				Map<String, Object> map = new HashMap<>();
				map.put("name", row[0]);
				map.put("orders", row[1]);
				map.put("spend", row[2]);
				topCustomers.add(map);
			}

			return topCustomers;
		} finally {
			em.close();
		}
	}
}
