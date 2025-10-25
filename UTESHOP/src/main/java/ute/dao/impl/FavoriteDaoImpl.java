package ute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.FavoriteDao;
import ute.dto.ProductDTO;
import ute.entities.Favorite;
import ute.entities.Product;
import ute.service.admin.Impl.ProductServiceImpl;
import ute.service.admin.inter.ProductService;

public class FavoriteDaoImpl implements FavoriteDao {
    private EntityManager em;

    public FavoriteDaoImpl() {
        this.em = JPAConfig.getEntityManager();
    }

    @Override
    public Favorite insert(Favorite favorite) {
        try {
            em.getTransaction().begin();
            em.persist(favorite);
            em.getTransaction().commit();
            return favorite;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void deleteByUserAndProduct(Long userId, Long productId) {
        try {
            em.getTransaction().begin();
            TypedQuery<Favorite> q = em.createQuery(
                    "SELECT f FROM Favorite f WHERE f.user.userID = :userId AND f.product.productID = :productId",
                    Favorite.class);
            q.setParameter("userId", userId);
            q.setParameter("productId", productId);
            List<Favorite> list = q.getResultList();
            for (Favorite f : list) {
                em.remove(em.contains(f) ? f : em.merge(f));
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public boolean existsByUserAndProduct(Long userId, Long productId) {
        TypedQuery<Long> q = em.createQuery(
                "SELECT COUNT(f) FROM Favorite f WHERE f.user.userID = :userId AND f.product.productID = :productId",
                Long.class);
        q.setParameter("userId", userId);
        q.setParameter("productId", productId);
        Long cnt = q.getSingleResult();
        return cnt != null && cnt > 0;
    }

    @Override
    public List<Product> findByUserId(Long userId) {
        System.out.println("=== DEBUG FAVORITE ===");
        System.out.println("User ID: " + userId);

        List<Product> products = em.createQuery(
                "SELECT DISTINCT p FROM Product p " +
                        "JOIN p.favorites f " +
                        "WHERE f.user.userID = :userId",
                Product.class)
                .setParameter("userId", userId)
                .getResultList();

        return products;
    }

    @Override
    public void clearFavorite(Long userId) {
        // TODO Auto-generated method stub
        try {
            em.getTransaction().begin();
            TypedQuery<Favorite> q = em.createQuery(
                    "SELECT f FROM Favorite f WHERE f.user.userID = :userId",
                    Favorite.class);
            q.setParameter("userId", userId);
            List<Favorite> list = q.getResultList();
            for (Favorite f : list) {
                em.remove(em.contains(f) ? f : em.merge(f));
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

}
