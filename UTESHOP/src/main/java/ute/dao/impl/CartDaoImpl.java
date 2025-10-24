package ute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.CartDao;
import ute.entities.Cart;
import ute.entities.CartDetail;

public class CartDaoImpl implements CartDao {

    @Override
    public Cart findCartByUserId(Long userId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Fetch cart with cartDetails to avoid LazyInitializationException
            TypedQuery<Cart> query = em.createQuery(
                "SELECT c FROM Cart c " +
                "LEFT JOIN FETCH c.cartDetails cd " +
                "LEFT JOIN FETCH cd.product " +
                "WHERE c.user.userID = :userId", 
                Cart.class);
            query.setParameter("userId", userId);
            Cart cart = query.getSingleResult();
            
            // Initialize collection to ensure it's loaded
            if (cart != null && cart.getCartDetails() != null) {
                cart.getCartDetails().size();
            }
            
            return cart;
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void createCart(Cart cart) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(cart);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void updateCart(Cart cart) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(cart);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteCart(Long cartId) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Cart cart = em.find(Cart.class, cartId);
            if (cart != null) {
                em.remove(cart);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public CartDetail findCartDetailById(Long cartDetailId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<CartDetail> query = em.createQuery(
                "SELECT cd FROM CartDetail cd " +
                "JOIN FETCH cd.cart " +
                "JOIN FETCH cd.product " +
                "WHERE cd.cartDetailID = :id", 
                CartDetail.class);
            query.setParameter("id", cartDetailId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<CartDetail> findCartDetailsByCartId(Long cartId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<CartDetail> query = em.createQuery(
                "SELECT cd FROM CartDetail cd " +
                "JOIN FETCH cd.product p " +
                "LEFT JOIN FETCH p.images " +
                "WHERE cd.cart.cartID = :cartId", 
                CartDetail.class);
            query.setParameter("cartId", cartId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public CartDetail findCartDetailByCartAndProduct(Long cartId, Long productId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<CartDetail> query = em.createQuery(
                "SELECT cd FROM CartDetail cd WHERE cd.cart.cartID = :cartId " +
                "AND cd.product.productID = :productId", 
                CartDetail.class);
            query.setParameter("cartId", cartId);
            query.setParameter("productId", productId);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void addCartDetail(CartDetail cartDetail) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(cartDetail);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void updateCartDetail(CartDetail cartDetail) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(cartDetail);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteCartDetail(Long cartDetailId) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            CartDetail cartDetail = em.find(CartDetail.class, cartDetailId);
            if (cartDetail != null) {
                em.remove(cartDetail);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void clearCart(Long cartId) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.createQuery("DELETE FROM CartDetail cd WHERE cd.cart.cartID = :cartId")
              .setParameter("cartId", cartId)
              .executeUpdate();
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public int getTotalItemsInCart(Long cartId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT SUM(cd.quantity) FROM CartDetail cd WHERE cd.cart.cartID = :cartId", 
                Long.class);
            query.setParameter("cartId", cartId);
            Long result = query.getSingleResult();
            return result != null ? result.intValue() : 0;
        } finally {
            em.close();
        }
    }
}

