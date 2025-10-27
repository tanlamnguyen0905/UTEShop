package ute.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.OrderDetailDao;
import ute.entities.OrderDetail;

public class OrderDetailDaoImpl implements OrderDetailDao {

	@Override
	public void insert(OrderDetail orderDetail) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.persist(orderDetail);
			trans.commit();
		} catch (Exception e) {
			trans.rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	@Override
	public void update(OrderDetail orderDetail) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.merge(orderDetail);
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
			OrderDetail orderDetail = em.find(OrderDetail.class, id);
			if (orderDetail != null) {
				em.remove(orderDetail);
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
	public OrderDetail findById(Long id) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			return em.find(OrderDetail.class, id);
		} finally {
			em.close();
		}
	}

	@Override
	public List<OrderDetail> findAll() {
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<OrderDetail> query = em.createQuery(
			"SELECT od FROM OrderDetail od", 
			OrderDetail.class
		);
		try {
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Map<String, Object>> getTopProductsByQuantity(int limit) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Object[]> query = em.createQuery(
				"SELECT p.productName, SUM(od.quantity), SUM(od.quantity * od.unitPrice) " +
				"FROM OrderDetail od " +
				"JOIN od.product p " +
				"GROUP BY p.productID, p.productName " +
				"ORDER BY SUM(od.quantity) DESC",
				Object[].class
			);
			query.setMaxResults(limit);
			
			List<Object[]> results = query.getResultList();
			List<Map<String, Object>> topProducts = new ArrayList<>();
			
			for (Object[] row : results) {
				Map<String, Object> map = new HashMap<>();
				map.put("name", row[0]);
				map.put("qty", row[1]);
				map.put("amount", row[2]);
				topProducts.add(map);
			}
			return topProducts;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Map<String, Object>> getTopProductsByRevenue(int limit) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Object[]> query = em.createQuery(
				"SELECT p.productName, SUM(od.quantity), SUM(od.quantity * od.unitPrice) " +
				"FROM OrderDetail od " +
				"JOIN od.product p " +
				"GROUP BY p.productID, p.productName " +
				"ORDER BY SUM(od.quantity * od.unitPrice) DESC",
				Object[].class
			);
			query.setMaxResults(limit);
			
			List<Object[]> results = query.getResultList();
			List<Map<String, Object>> topProducts = new ArrayList<>();
			
			for (Object[] row : results) {
				Map<String, Object> map = new HashMap<>();
				map.put("name", row[0]);
				map.put("qty", row[1]);
				map.put("amount", row[2]);
				topProducts.add(map);
			}
			return topProducts;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Map<String, Object>> getSalesByCategory() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Object[]> query = em.createQuery(
				"SELECT c.categoryName, SUM(od.quantity * od.unitPrice) " +
				"FROM OrderDetail od " +
				"JOIN od.product p " +
				"JOIN p.category c " +
				"GROUP BY c.categoryID, c.categoryName " +
				"ORDER BY SUM(od.quantity * od.unitPrice) DESC",
				Object[].class
			);
			
			List<Object[]> results = query.getResultList();
			List<Map<String, Object>> categorySales = new ArrayList<>();
			
			for (Object[] row : results) {
				Map<String, Object> map = new HashMap<>();
				map.put("name", row[0]);
				map.put("amount", row[1]);
				categorySales.add(map);
			}
			return categorySales;
		} finally {
			em.close();
		}
	}

	@Override
	public List<OrderDetail> findByOrderId(Long orderId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<OrderDetail> query = em.createQuery(
				"SELECT od FROM OrderDetail od WHERE od.order.orderID = :orderId",
				OrderDetail.class
			);
			query.setParameter("orderId", orderId);
			return query.getResultList();
		} finally {
			em.close();
		}
	}
}