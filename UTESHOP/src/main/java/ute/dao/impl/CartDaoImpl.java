package ute.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.CartDao;
import ute.entities.Cart;
import ute.entities.CartDetail;

public class CartDaoImpl implements CartDao {
    private EntityManager em;

    public CartDaoImpl() {
        this.em = JPAConfig.getEntityManager();
    }

    @Override
    public Cart findByUserId(Long userId) {
        TypedQuery<Cart> q = em.createQuery("SELECT c FROM Cart c WHERE c.user.userID = :userId", Cart.class);
        q.setParameter("userId", userId);
        return q.getResultStream().findFirst().orElse(null);
    }

    @Override
    public Cart createCart(Cart cart) {
        try {
            em.getTransaction().begin();
            em.persist(cart);
            em.getTransaction().commit();
            return cart;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public CartDetail addOrUpdateCartDetail(CartDetail detail) {
        try {
            em.getTransaction().begin();
            // if exists (by cart and product) update quantity
            TypedQuery<CartDetail> q = em.createQuery(
                    "SELECT cd FROM CartDetail cd WHERE cd.cart.cartID = :cartId AND cd.product.productID = :productId",
                    CartDetail.class);
            q.setParameter("cartId", detail.getCart().getCartID());
            q.setParameter("productId", detail.getProduct().getProductID());
            CartDetail existing = q.getResultStream().findFirst().orElse(null);
            if (existing != null) {
                existing.setQuantity(existing.getQuantity() + detail.getQuantity());
                existing.setUnitPrice(detail.getUnitPrice());
                em.merge(existing);
                em.getTransaction().commit();
                return existing;
            }
            em.persist(detail);
            em.getTransaction().commit();
            return detail;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }
}
