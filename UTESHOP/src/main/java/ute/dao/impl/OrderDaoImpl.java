package ute.dao.impl;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.OrderDao;
import ute.entities.Orders;

public class OrderDaoImpl implements OrderDao {

    @Override
    public void insert(Orders order) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(order);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error creating order", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Orders order) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(order);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error updating order", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long orderId) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Orders order = em.find(Orders.class, orderId);
            if (order != null) {
                em.remove(order);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Error deleting order", e);
        } finally {
            em.close();
        }
    }

    @Override
    public Orders findById(Long orderId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Fetch order with all relationships
            TypedQuery<Orders> query = em.createQuery(
                "SELECT o FROM Orders o " +
                "LEFT JOIN FETCH o.user " +
                "LEFT JOIN FETCH o.address " +
                "LEFT JOIN FETCH o.paymentMethod " +
                "LEFT JOIN FETCH o.orderDetails od " +
                "LEFT JOIN FETCH od.product " +
                "WHERE o.orderID = :orderId",
                Orders.class
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
    public List<Orders> findByUserId(Long userId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Step 1: Fetch orders with orderDetails and product (no images yet)
            TypedQuery<Orders> query = em.createQuery(
                "SELECT DISTINCT o FROM Orders o " +
                "LEFT JOIN FETCH o.orderDetails od " +
                "LEFT JOIN FETCH od.product p " +
                "LEFT JOIN FETCH o.address " +
                "LEFT JOIN FETCH o.paymentMethod " +
                "WHERE o.user.userID = :userId " +
                "ORDER BY o.orderDate DESC",
                Orders.class
            );
            query.setParameter("userId", userId);
            List<Orders> orders = query.getResultList();
            
            // Step 2: Initialize images for each product (avoid MultipleBagFetchException)
            for (Orders order : orders) {
                if (order.getOrderDetails() != null) {
                    for (ute.entities.OrderDetail detail : order.getOrderDetails()) {
                        if (detail.getProduct() != null && detail.getProduct().getImages() != null) {
                            // Force initialization of images collection
                            detail.getProduct().getImages().size();
                        }
                    }
                }
            }
            
            return orders;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Orders> findByStatus(String status) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Orders> query = em.createQuery(
                "SELECT o FROM Orders o " +
                "WHERE o.orderStatus = :status " +
                "ORDER BY o.orderDate DESC",
                Orders.class
            );
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Orders> findAll() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Orders> query = em.createQuery(
                "SELECT o FROM Orders o ORDER BY o.orderDate DESC",
                Orders.class
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
                "SELECT COUNT(o) FROM Orders o",
                Long.class
            );
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Orders> findPage(int page, int pageSize) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Orders> query = em.createQuery(
                "SELECT o FROM Orders o ORDER BY o.orderDate DESC",
                Orders.class
            );
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Orders> findByUserIdPaginated(Long userId, int offset, int limit) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Orders> query = em.createQuery(
                "SELECT o FROM Orders o " +
                "WHERE o.user.userID = :userId " +
                "ORDER BY o.orderDate DESC",
                Orders.class
            );
            query.setParameter("userId", userId);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByUserId(Long userId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(o) FROM Orders o WHERE o.user.userID = :userId",
                Long.class
            );
            query.setParameter("userId", userId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Orders> findConfirmedOrders() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Tìm các đơn hàng "Đã xác nhận" và chưa có delivery
            TypedQuery<Orders> query = em.createQuery(
                "SELECT DISTINCT o FROM Orders o " +
                "LEFT JOIN FETCH o.address " +
                "LEFT JOIN FETCH o.user " +
                "LEFT JOIN FETCH o.orderDetails od " +
                "LEFT JOIN FETCH od.product " +
                "WHERE o.orderStatus = :status " +
                "AND NOT EXISTS (SELECT d FROM Delivery d WHERE d.order.orderID = o.orderID) " +
                "ORDER BY o.orderDate DESC",
                Orders.class
            );
            query.setParameter("status", "Đã xác nhận");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countConfirmedOrders() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(o) FROM Orders o " +
                "WHERE o.orderStatus = :status " +
                "AND NOT EXISTS (SELECT d FROM Delivery d WHERE d.order.orderID = o.orderID)",
                Long.class
            );
            query.setParameter("status", "Đã xác nhận");
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Object[]> getDailyRevenueRaw(LocalDate fromDate, LocalDate toDate) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Fix: Sử dụng native SQL query với CAST AS DATE cho SQL Server
            String sql = """
                SELECT CAST(o.orderDate AS DATE) as order_date, 
                       SUM(o.amount - ISNULL(o.totalDiscount, 0) + ISNULL(o.shippingFee, 0)) as revenue 
                FROM Orders o 
                WHERE o.orderStatus IN ('Đã giao', 'Hoàn thành') 
                AND o.paymentStatus = 'Đã thanh toán' 
                AND CAST(o.orderDate AS DATE) BETWEEN ? AND ? 
                GROUP BY CAST(o.orderDate AS DATE) 
                ORDER BY order_date ASC
                """;
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fromDate);
            query.setParameter(2, toDate);
            List<Object[]> results = query.getResultList();

            // Convert Object[] về đúng type (order_date là java.sql.Date, convert sang LocalDate)
            return results.stream().map(row -> {
                java.sql.Date sqlDate = (java.sql.Date) row[0];
                LocalDate localDate = sqlDate.toLocalDate();
                Double revenue = ((Number) row[1]).doubleValue();
                return new Object[] { localDate, revenue };
            }).toList();
        } finally {
            em.close();
        }
    }

    @Override
    public Object[] getTotalRevenueStatsRaw(LocalDate fromDate, LocalDate toDate) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Fix: Native SQL với CAST AS DATE
            String sql = """
                SELECT SUM(o.amount - ISNULL(o.totalDiscount, 0) + ISNULL(o.shippingFee, 0)) as totalRevenue, 
                       COUNT(o.orderID) as orderCount, 
                       AVG(o.amount - ISNULL(o.totalDiscount, 0) + ISNULL(o.shippingFee, 0)) as avgRevenue 
                FROM Orders o 
                WHERE o.orderStatus IN ('Đã giao', 'Hoàn thành') 
                AND o.paymentStatus = 'Đã thanh toán' 
                AND CAST(o.orderDate AS DATE) BETWEEN ? AND ?
                """;
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, fromDate);
            query.setParameter(2, toDate);
            Object[] result = (Object[]) query.getSingleResult();

            // Convert về đúng type
            Double totalRevenue = result[0] != null ? ((Number) result[0]).doubleValue() : 0.0;
            Long orderCount = result[1] != null ? ((Number) result[1]).longValue() : 0L;
            Double avgRevenue = result[2] != null ? ((Number) result[2]).doubleValue() : 0.0;
            return new Object[] { totalRevenue, orderCount, avgRevenue };
        } finally {
            em.close();
        }
    }
    @Override
    public List<Orders> findByStatusPaginated(String orderStatus, String paymentStatus, int offset, int limit) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT o FROM Orders o LEFT JOIN FETCH o.user LEFT JOIN FETCH o.address LEFT JOIN FETCH o.paymentMethod LEFT JOIN FETCH o.orderDetails od LEFT JOIN FETCH od.product WHERE 1=1";
            if (orderStatus != null && !orderStatus.isEmpty()) {
                jpql += " AND o.orderStatus = :orderStatus";
            }
            if (paymentStatus != null && !paymentStatus.isEmpty()) {
                jpql += " AND o.paymentStatus = :paymentStatus";
            }
            jpql += " ORDER BY o.orderDate DESC";

            TypedQuery<Orders> query = em.createQuery(jpql, Orders.class);
            if (orderStatus != null && !orderStatus.isEmpty()) {
                query.setParameter("orderStatus", orderStatus);
            }
            if (paymentStatus != null && !paymentStatus.isEmpty()) {
                query.setParameter("paymentStatus", paymentStatus);
            }
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByStatus(String orderStatus, String paymentStatus) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT COUNT(o) FROM Orders o WHERE 1=1";
            if (orderStatus != null && !orderStatus.isEmpty()) {
                jpql += " AND o.orderStatus = :orderStatus";
            }
            if (paymentStatus != null && !paymentStatus.isEmpty()) {
                jpql += " AND o.paymentStatus = :paymentStatus";
            }

            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            if (orderStatus != null && !orderStatus.isEmpty()) {
                query.setParameter("orderStatus", orderStatus);
            }
            if (paymentStatus != null && !paymentStatus.isEmpty()) {
                query.setParameter("paymentStatus", paymentStatus);
            }
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}

