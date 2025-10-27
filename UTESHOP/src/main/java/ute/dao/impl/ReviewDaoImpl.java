package ute.dao.impl;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.ReviewDao;
import ute.entities.Review;

public class ReviewDaoImpl implements ReviewDao {

	@Override
	public void insert(Review review) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.persist(review);
			trans.commit();
		} catch (Exception e) {
			trans.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	@Override
	public void update(Review review) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.merge(review);
			trans.commit();
		} catch (Exception e) {
			trans.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	@Override
	public void delete(Long id) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			Review review = em.find(Review.class, id);
			if (review != null) {
				em.remove(review);
			}
			trans.commit();
		} catch (Exception e) {
			trans.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	@Override
	public Review findById(Long id) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			return em.find(Review.class, id);
		} finally {
			em.close();
		}
	}

	@Override
	public List<Review> findAll() {
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<Review> query = em.createQuery("SELECT r FROM Review r", Review.class);
		try {
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public Long getTotalReviewCount() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Long> query = em.createQuery(
				"SELECT COUNT(r) FROM Review r",
				Long.class
			);
			return query.getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public Double getAverageRating() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Double> query = em.createQuery(
				"SELECT COALESCE(AVG(r.rating), 0.0) FROM Review r",
				Double.class
			);
			return query.getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public Double getAverageRatingByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Double> query = em.createQuery(
				"SELECT COALESCE(AVG(r.rating), 0.0) FROM Review r " +
				"WHERE r.createdAt BETWEEN :startDate AND :endDate",
				Double.class
			);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			return query.getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Review> findByProductId(Long productId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Review> query = em.createQuery(
				"SELECT r FROM Review r WHERE r.product.productID = :productId ORDER BY r.createdAt DESC",
				Review.class
			);
			query.setParameter("productId", productId);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Review> findByUserId(Long userId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Review> query = em.createQuery(
				"SELECT r FROM Review r WHERE r.user.userID = :userId ORDER BY r.createdAt DESC",
				Review.class
			);
			query.setParameter("userId", userId);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

    @Override
    public Double getAverageRatingByProductId(Long productId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT AVG(r.rating) FROM Review r WHERE r.product.productID = :productId";
			TypedQuery<Double> query = em.createQuery(jpql, Double.class);
			query.setParameter("productId", productId);
			Double result = query.getSingleResult();
			return result != null ? result : 0.0;
		} finally {
			em.close();
		}
    }

    @Override
    public Long getReviewCountByProductId(Long productId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT COUNT(r) FROM Review r WHERE r.product.productID = :productId";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter("productId", productId);
			return query.getSingleResult();	
		} finally {
			em.close();
		}
    }

    @Override
    public Review findByUserIdAndProductId(Long userId, Long productId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT r FROM Review r WHERE r.user.userID = :userId AND r.product.productID = :productId";
			TypedQuery<Review> query = em.createQuery(jpql, Review.class);
			query.setParameter("userId", userId);
			query.setParameter("productId", productId);
			List<Review> results = query.getResultList();
			return results.isEmpty() ? null : results.get(0);
		} finally {
			em.close();
		}
    }

    @Override
    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT COUNT(od) FROM OrderDetail od " +
						"WHERE od.order.user.userID = :userId " +
						"AND od.product.productID = :productId " +
						"AND (od.order.orderStatus = :status1 OR od.order.orderStatus = :status2 " +
						"OR od.order.orderStatus = :status3 OR od.order.orderStatus = :status4)";
			TypedQuery<Long> query = em.createQuery(jpql, Long.class);
			query.setParameter("userId", userId);
			query.setParameter("productId", productId);
			query.setParameter("status1", "Hoàn thành");
			query.setParameter("status2", "Hoàn tất");
			query.setParameter("status3", "Đã giao");
			query.setParameter("status4", "Đã giao hàng");
			Long count = query.getSingleResult();
			return count != null && count > 0;
		} finally {
			em.close();
		}
    }
}