package ute.dao.impl;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.ReviewDao;
import ute.entities.Review;

public class ReviewDaoImpl implements ReviewDao {
    private EntityManager em;

    public ReviewDaoImpl() {
        this.em = JPAConfig.getEntityManager();
    }

    @Override
    public void insert(Review review) {
        try {
            em.getTransaction().begin();
            em.persist(review);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void update(Review review) {
        try {
            em.getTransaction().begin();
            em.merge(review);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void delete(Long id) {
        try {
            em.getTransaction().begin();
            Review review = em.find(Review.class, id);
            if (review != null) {
                em.remove(review);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public List<Review> findByProductId(Long productId) {
        String jpql = "SELECT r FROM Review r WHERE r.product.productID = :productId ORDER BY r.createdAt DESC";
        TypedQuery<Review> query = em.createQuery(jpql, Review.class);
        query.setParameter("productId", productId);
        return query.getResultList();
    }

    @Override
    public Double getAverageRatingByProductId(Long productId) {
        String jpql = "SELECT AVG(r.rating) FROM Review r WHERE r.product.productID = :productId";
        TypedQuery<Double> query = em.createQuery(jpql, Double.class);
        query.setParameter("productId", productId);
        Double result = query.getSingleResult();
        return result != null ? result : 0.0;
    }

    @Override
    public Long getReviewCountByProductId(Long productId) {
        String jpql = "SELECT COUNT(r) FROM Review r WHERE r.product.productID = :productId";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("productId", productId);
        return query.getSingleResult();
    }

    @Override
    public Review findById(Long id) {
        return em.find(Review.class, id);
    }

    @Override
    public List<Review> findByUserId(Long userId) {
        String jpql = "SELECT r FROM Review r WHERE r.user.userID = :userId ORDER BY r.createdAt DESC";
        TypedQuery<Review> query = em.createQuery(jpql, Review.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Override
    public Review findByUserIdAndProductId(Long userId, Long productId) {
        String jpql = "SELECT r FROM Review r WHERE r.user.userID = :userId AND r.product.productID = :productId";
        TypedQuery<Review> query = em.createQuery(jpql, Review.class);
        query.setParameter("userId", userId);
        query.setParameter("productId", productId);
        List<Review> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
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
    }
}