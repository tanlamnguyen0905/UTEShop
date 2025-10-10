package ute.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;

import ute.entities.Users;
import ute.utils.HibernateUtil;

public class UserDao {
	// ------------------ Thêm hoặc cập nhật ------------------
	public void save(Users user) {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();
			session.merge(user); // merge = insert or update tùy theo trạng thái
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}

	// ------------------ Xóa người dùng ------------------
	public void delete(Users user) {
		Transaction tx = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			tx = session.beginTransaction();
			session.remove(session.contains(user) ? user : session.merge(user));
			tx.commit();
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}

	// ------------------ Tìm theo username ------------------
	public Optional<Users> findByUsername(String username) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Users user = session.createQuery("FROM Users WHERE username = :u", Users.class).setParameter("u", username)
					.uniqueResult();
			return Optional.ofNullable(user);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	// ------------------ Tìm theo email ------------------
	public Optional<Users> findByEmail(String email) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Users user = session.createQuery("FROM Users WHERE email = :e", Users.class).setParameter("e", email)
					.uniqueResult();
			return Optional.ofNullable(user);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	// ------------------ Lấy tất cả người dùng ------------------
	public List<Users> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("FROM Users", Users.class).list();
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}
}
