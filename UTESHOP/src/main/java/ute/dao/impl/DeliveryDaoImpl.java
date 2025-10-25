package ute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.DeliveryDao;
import ute.entities.Delivery;

public class DeliveryDaoImpl implements DeliveryDao {

    @Override
    public void insert(Delivery delivery) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(delivery);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error creating delivery", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Delivery delivery) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(delivery);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating delivery", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long deliveryId) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Delivery delivery = em.find(Delivery.class, deliveryId);
            if (delivery != null) {
                em.remove(delivery);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting delivery", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Delivery findById(Long deliveryId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Delivery> query = em.createQuery(
                "SELECT d FROM Delivery d " +
                "LEFT JOIN FETCH d.order o " +
                "LEFT JOIN FETCH o.orderDetails od " +
                "LEFT JOIN FETCH od.product p " +
                "LEFT JOIN FETCH o.address " +
                "LEFT JOIN FETCH o.user " +
                "LEFT JOIN FETCH d.shipper " +
                "WHERE d.deliveryID = :deliveryId",
                Delivery.class
            );
            query.setParameter("deliveryId", deliveryId);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Delivery findByOrderId(Long orderId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Delivery> query = em.createQuery(
                "SELECT d FROM Delivery d " +
                "LEFT JOIN FETCH d.order o " +
                "LEFT JOIN FETCH d.shipper " +
                "WHERE d.order.orderID = :orderId",
                Delivery.class
            );
            query.setParameter("orderId", orderId);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Delivery> findByShipperId(Long shipperId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Delivery> query = em.createQuery(
                "SELECT DISTINCT d FROM Delivery d " +
                "LEFT JOIN FETCH d.order o " +
                "LEFT JOIN FETCH o.orderDetails od " +
                "LEFT JOIN FETCH od.product p " +
                "LEFT JOIN FETCH o.address " +
                "LEFT JOIN FETCH o.user " +
                "LEFT JOIN FETCH o.paymentMethod " +
                "WHERE d.shipper.userID = :shipperId " +
                "ORDER BY d.assignedDate DESC",
                Delivery.class
            );
            query.setParameter("shipperId", shipperId);
            List<Delivery> deliveries = query.getResultList();
            
            // Initialize images for each product
            for (Delivery delivery : deliveries) {
                if (delivery.getOrder().getOrderDetails() != null) {
                    for (ute.entities.OrderDetail detail : delivery.getOrder().getOrderDetails()) {
                        if (detail.getProduct() != null && detail.getProduct().getImages() != null) {
                            detail.getProduct().getImages().size();
                        }
                    }
                }
            }
            
            return deliveries;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Delivery> findByStatus(String status) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Delivery> query = em.createQuery(
                "SELECT d FROM Delivery d " +
                "LEFT JOIN FETCH d.order " +
                "LEFT JOIN FETCH d.shipper " +
                "WHERE d.deliveryStatus = :status " +
                "ORDER BY d.assignedDate DESC",
                Delivery.class
            );
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Delivery> findByShipperIdAndStatus(Long shipperId, String status) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Delivery> query = em.createQuery(
                "SELECT DISTINCT d FROM Delivery d " +
                "LEFT JOIN FETCH d.order o " +
                "LEFT JOIN FETCH o.orderDetails od " +
                "LEFT JOIN FETCH od.product p " +
                "LEFT JOIN FETCH o.address " +
                "LEFT JOIN FETCH o.user " +
                "LEFT JOIN FETCH o.paymentMethod " +
                "WHERE d.shipper.userID = :shipperId AND d.deliveryStatus = :status " +
                "ORDER BY d.assignedDate DESC",
                Delivery.class
            );
            query.setParameter("shipperId", shipperId);
            query.setParameter("status", status);
            List<Delivery> deliveries = query.getResultList();
            
            // Initialize images for each product
            for (Delivery delivery : deliveries) {
                if (delivery.getOrder().getOrderDetails() != null) {
                    for (ute.entities.OrderDetail detail : delivery.getOrder().getOrderDetails()) {
                        if (detail.getProduct() != null && detail.getProduct().getImages() != null) {
                            detail.getProduct().getImages().size();
                        }
                    }
                }
            }
            
            return deliveries;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Delivery> findAll() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Delivery> query = em.createQuery(
                "SELECT d FROM Delivery d " +
                "LEFT JOIN FETCH d.order " +
                "LEFT JOIN FETCH d.shipper " +
                "ORDER BY d.assignedDate DESC",
                Delivery.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(d) FROM Delivery d",
                Long.class
            );
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByShipperId(Long shipperId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(d) FROM Delivery d WHERE d.shipper.userID = :shipperId",
                Long.class
            );
            query.setParameter("shipperId", shipperId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByShipperIdAndStatus(Long shipperId, String status) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(d) FROM Delivery d WHERE d.shipper.userID = :shipperId AND d.deliveryStatus = :status",
                Long.class
            );
            query.setParameter("shipperId", shipperId);
            query.setParameter("status", status);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Delivery> findByShipperIdPaginated(Long shipperId, int offset, int limit) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Delivery> query = em.createQuery(
                "SELECT d FROM Delivery d " +
                "LEFT JOIN FETCH d.order " +
                "WHERE d.shipper.userID = :shipperId " +
                "ORDER BY d.assignedDate DESC",
                Delivery.class
            );
            query.setParameter("shipperId", shipperId);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}

