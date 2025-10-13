package ute.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ute.entities.Users;
import ute.configs.JPAConfig;

import java.util.List;

public class UserDaoImpl {

	// -------------------- CREATE / REGISTER --------------------
	public boolean register(Users user) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			em.getTransaction().begin();

			// Kiểm tra username hoặc email đã tồn tại chưa
			TypedQuery<Long> query = em.createQuery(
					"SELECT COUNT(u) FROM Users u WHERE u.username = :username OR u.email = :email OR u.phone = :phone",
					Long.class);
			query.setParameter("username", user.getUsername());
			query.setParameter("email", user.getEmail());
			query.setParameter("phone", user.getPhone());

			if (query.getSingleResult() > 0) {
				return false; // Đã tồn tại
			}

			em.persist(user);
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	// -------------------- LOGIN --------------------
	public Users login(String username, String password) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Users> query = em.createQuery(
					"SELECT u FROM Users u WHERE u.username = :username AND u.password = :password AND u.active = true",
					Users.class);
			query.setParameter("username", username);
			query.setParameter("password", password);

			List<Users> results = query.getResultList();
			return results.isEmpty() ? null : results.get(0);
		} finally {
			em.close();
		}
	}

	// -------------------- FIND --------------------
	public Users findById(int id) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			return em.find(Users.class, id);
		} finally {
			em.close();
		}
	}

	public List<Users> findAll() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u", Users.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	public List<Users> searchByNameOrEmail(String keyword) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Users> query = em
					.createQuery("SELECT u FROM Users u WHERE u.fullName LIKE :kw OR u.email LIKE :kw", Users.class);
			query.setParameter("kw", "%" + keyword + "%");
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	// -------------------- UPDATE --------------------
	public boolean update(Users user) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(user);
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	// -------------------- DELETE --------------------
	public boolean delete(int id) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			Users user = em.find(Users.class, id);
			if (user != null) {
				em.getTransaction().begin();
				em.remove(user);
				em.getTransaction().commit();
				return true;
			}
			return false;
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}
}
