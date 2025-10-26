package ute.dao.admin.Impl;

import ute.configs.JPAConfig;
import ute.dao.AbstractDao;
import ute.dao.admin.inter.ReviewDao;
import ute.entities.Review;
import ute.dto.ProductWithAvgRatingDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class ReviewDaoImpl extends AbstractDao<Review> implements ReviewDao {
    public ReviewDaoImpl() {super(Review.class);}

    private EntityManager getEntityManager() {
        return JPAConfig.getEntityManager();
    }

    @Override
    public void insert(Review review) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(review);
            em.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Lỗi insert Review: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void update(Review review) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(review);
            em.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Lỗi update Review: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Review review) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(review));
            em.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Lỗi delete Review: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Review review = em.find(Review.class, id);
            if (review != null) {
                em.remove(review);
                em.flush();
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Lỗi delete Review: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Review findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Review.class, id);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Review> findByContentOrRatingPaginated(String content, String rating, int firstResult, int maxResults) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT r FROM Review r WHERE 1=1";
            if (content != null && !content.trim().isEmpty()) {
                jpql += " AND LOWER(r.content) LIKE LOWER(:content)";
            }
            if (rating != null && !rating.equals("all")) {
                jpql += " AND r.rating = :rating";
            }
            jpql += " ORDER BY r.reviewID";
            TypedQuery<Review> query = em.createQuery(jpql, Review.class);
            if (content != null && !content.trim().isEmpty()) {
                query.setParameter("content", "%" + content.trim() + "%");
            }
            if (rating != null && !rating.equals("all")) {
                query.setParameter("rating", Double.parseDouble(rating));
            }
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public long countByContentOrRating(String content, String rating) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(r) FROM Review r WHERE 1=1";
            if (content != null && !content.trim().isEmpty()) {
                jpql += " AND LOWER(r.content) LIKE LOWER(:content)";
            }
            if (rating != null && !rating.equals("all")) {
                jpql += " AND r.rating = :rating";
            }
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            if (content != null && !content.trim().isEmpty()) {
                query.setParameter("content", "%" + content.trim() + "%");
            }
            if (rating != null && !rating.equals("all")) {
                query.setParameter("rating", Double.parseDouble(rating));
            }
            return query.getSingleResult();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Review> findPage(int page, int size) {
        EntityManager em = getEntityManager();
        try {
            int firstResult = (page - 1) * size;
            String jpql = "SELECT r FROM Review r ORDER BY r.reviewID";
            TypedQuery<Review> query = em.createQuery(jpql, Review.class);
            query.setFirstResult(firstResult);
            query.setMaxResults(size);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public long count() {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Review> root = cq.from(Review.class);
            cq.select(cb.count(root));
            return em.createQuery(cq).getSingleResult();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Review> findAll() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT r FROM Review r ORDER BY r.reviewID";
            TypedQuery<Review> query = em.createQuery(jpql, Review.class);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<ProductWithAvgRatingDTO> getAverageRatingPerProduct() {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT p.productID, p.productName, AVG(r.rating) FROM Review r JOIN r.product p GROUP BY p.productID, p.productName";
            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            List<Object[]> results = query.getResultList();
            List<ProductWithAvgRatingDTO> avgList = new java.util.ArrayList<>();
            for (Object[] row : results) {
                avgList.add(new ProductWithAvgRatingDTO((Long) row[0], (String) row[1], (Double) row[2]));
            }
            return avgList;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}