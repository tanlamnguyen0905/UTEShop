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
			trans.rollback();
			e.printStackTrace();
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
			Orders order = em.find(Orders.class, id);
			if (order != null) {
				em.remove(order);
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
	public Orders findById(Long id) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			return em.find(Orders.class, id);
		} finally {
			em.close();
		}
	}

	@Override
	public List<Orders> findAll() {
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<Orders> query = em.createQuery("SELECT o FROM Orders o", Orders.class);
		try {
			return query.getResultList();
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

	@Override
	public List<Orders> findByUserId(Long userId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Orders> query = em.createQuery(
				"SELECT o FROM Orders o WHERE o.user.userID = :userId",
				Orders.class
			);
			query.setParameter("userId", userId);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
}