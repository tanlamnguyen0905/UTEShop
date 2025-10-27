package ute.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
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
	public Double getTotalRevenue() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Double> query = em.createQuery(
				"SELECT COALESCE(SUM(o.amount), 0.0) FROM Orders o", 
				Double.class
			);
			return query.getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public Double getTotalRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Double> query = em.createQuery(
				"SELECT COALESCE(SUM(o.amount), 0.0) FROM Orders o " +
				"WHERE o.orderDate BETWEEN :startDate AND :endDate",
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
	public Long getTotalOrderCount() {
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
	public Long getOrderCountByStatus(String status) {
		// Since Orders entity doesn't have orderStatus field,
		// we'll just return total count for now
		// You can modify this based on your business logic
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
	public List<Map<String, Object>> getOrderStatusDistribution() {
		// Since Orders entity doesn't have orderStatus,
		// we'll return a simple count grouped by payment method or another field
		// For now, returning basic statistics
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Object[]> query = em.createQuery(
				"SELECT pm.name, COUNT(o) FROM Orders o " +
				"JOIN o.paymentMethod pm " +
				"GROUP BY pm.id, pm.name",
				Object[].class
			);
			List<Object[]> results = query.getResultList();
			
			List<Map<String, Object>> distribution = new ArrayList<>();
			for (Object[] row : results) {
				Map<String, Object> map = new HashMap<>();
				map.put("status", row[0]);
				map.put("count", row[1]);
				distribution.add(map);
			}
			return distribution;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Map<String, Object>> getDailyRevenue(LocalDateTime startDate, LocalDateTime endDate) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			// Format date as yyyy-MM-dd string
			TypedQuery<Object[]> query = em.createQuery(
				"SELECT CAST(o.orderDate AS date), SUM(o.amount) FROM Orders o " +
				"WHERE o.orderDate BETWEEN :startDate AND :endDate " +
				"GROUP BY CAST(o.orderDate AS date) " +
				"ORDER BY CAST(o.orderDate AS date)",
				Object[].class
			);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			
			List<Object[]> results = query.getResultList();
			List<Map<String, Object>> dailyRevenue = new ArrayList<>();
			
			for (Object[] row : results) {
				Map<String, Object> map = new HashMap<>();
				map.put("date", row[0].toString());
				map.put("total", row[1]);
				dailyRevenue.add(map);
			}
			return dailyRevenue;
		} finally {
			em.close();
		}
	}
}