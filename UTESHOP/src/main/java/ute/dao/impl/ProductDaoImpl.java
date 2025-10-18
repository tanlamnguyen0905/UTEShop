package ute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.ProductDao;
import ute.entities.Product;

public class ProductDaoImpl implements ProductDao {
	@Override
	public void insert(Product product) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.persist(product);
			trans.commit();
		} catch (Exception e) {
			trans.rollback();
			e.printStackTrace();
		}
	}

	@Override
	public void update(Product product) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.merge(product);
			trans.commit();
		} catch (Exception e) {
			trans.rollback();
			e.printStackTrace();
		}
	}

	@Override
	public void delete(int id) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			Product p = em.find(Product.class, (long) id);
			if (p != null) {
				em.remove(p);
			}
			trans.commit();
		} catch (Exception e) {
			trans.rollback();
			e.printStackTrace();
		}
	}

	@Override
	public Product findById(int id) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			return em.find(Product.class, (long) id);			
		} finally {
			em.close();
		}
		
	}

	@Override
	public List<Product> findAll() {
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);
		try {
			return query.getResultList();			
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findByName(String name) {
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<Product> query = em
				.createQuery("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(:name)", Product.class);
		query.setParameter("name", "%" + name + "%");
		try {
			return query.getResultList();			
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findByCategoryId(int categoryId) {
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE p.category.categoryID = :id",
				Product.class);
		query.setParameter("id", (long) categoryId);
		try {
			return query.getResultList();			
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findLatest(int limit) {
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p ORDER BY p.productID DESC", Product.class);
		query.setMaxResults(limit);
		try {
			return query.getResultList();			
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findBestSeller(int limit) {
		// Giả sử dựa theo số lượng bán trong OrderDetail
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<Product> query = em.createQuery(
			    "SELECT p FROM OrderDetail od " +
			    "JOIN od.product p " +
			    "GROUP BY p " +
			    "ORDER BY SUM(od.quantity) DESC",
			    Product.class
			);
		query.setMaxResults(limit);
		List<Product> bestSellers;
		try { 
			bestSellers = query.getResultList();
		} finally {
			em.close();
		}
		for (Product p : bestSellers) {
		    p.getImages().size(); // kích hoạt lazy load ảnh
		}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   
		return bestSellers;
	}

	@Override
	public List<Product> findByPriceRange(double minPrice, double maxPrice) {
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE p.unitPrice BETWEEN :min AND :max",
				Product.class);
		query.setParameter("min", minPrice);
		query.setParameter("max", maxPrice);
		try {
			return query.getResultList();			
		} finally {
			em.close();
		}
	}

	@Override
	public long count() {
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<Long> query = em.createQuery("SELECT COUNT(p) FROM Product p", Long.class);
		try {
			return query.getSingleResult();			
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findPage(int page, int pageSize) {
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		try {
			return query.getResultList();			
		} finally {
			em.close();
		}
	}

}
