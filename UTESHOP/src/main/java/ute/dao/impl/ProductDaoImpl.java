package ute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import ute.dao.inter.ProductDao;
import ute.entities.Product;

public class ProductDaoImpl implements ProductDao {

	private EntityManager em;

	public ProductDaoImpl() {
		// "UTESHOP" là tên persistence-unit bạn đã khai báo trong persistence.xml
		em = Persistence.createEntityManagerFactory("UTESHOP").createEntityManager();
	}

	@Override
	public void insert(Product product) {
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
		return em.find(Product.class, (long) id);
	}

	@Override
	public List<Product> findAll() {
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);
		return query.getResultList();
	}

	@Override
	public List<Product> findByName(String name) {
		TypedQuery<Product> query = em
				.createQuery("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(:name)", Product.class);
		query.setParameter("name", "%" + name + "%");
		return query.getResultList();
	}

	@Override
	public List<Product> findByCategoryId(int categoryId) {
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE p.category.categoryID = :id",
				Product.class);
		query.setParameter("id", (long) categoryId);
		return query.getResultList();
	}

	@Override
	public List<Product> findLatest(int limit) {
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p ORDER BY p.productID DESC", Product.class);
		query.setMaxResults(limit);
		return query.getResultList();
	}

	@Override
	public List<Product> findBestSeller(int limit) {
		// Giả sử dựa theo số lượng bán trong OrderDetail
		TypedQuery<Product> query = em.createQuery(
				"SELECT p FROM Product p JOIN p.orderDetails od GROUP BY p ORDER BY SUM(od.quantity) DESC",
				Product.class);
		query.setMaxResults(limit);
		return query.getResultList();
	}

	@Override
	public List<Product> findByPriceRange(double minPrice, double maxPrice) {
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE p.unitPrice BETWEEN :min AND :max",
				Product.class);
		query.setParameter("min", minPrice);
		query.setParameter("max", maxPrice);
		return query.getResultList();
	}

	@Override
	public long count() {
		TypedQuery<Long> query = em.createQuery("SELECT COUNT(p) FROM Product p", Long.class);
		return query.getSingleResult();
	}

	@Override
	public List<Product> findPage(int page, int pageSize) {
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

}
