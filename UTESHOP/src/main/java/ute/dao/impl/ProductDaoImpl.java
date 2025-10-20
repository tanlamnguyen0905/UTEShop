package ute.dao.impl;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import ute.dao.inter.ProductDao;
import ute.entities.Product;
import ute.utils.ProductFilter;

public class ProductDaoImpl implements ProductDao {
	private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("UTESHOP");
	private final EntityManager em;

	public ProductDaoImpl() {
		this.em = EMF.createEntityManager();
	}

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
						"right JOIN od.product p " +
						"GROUP BY p " +
						"ORDER BY SUM(od.quantity) DESC",
				Product.class);
		query.setMaxResults(limit);
<<<<<<< HEAD
		// List<Product> bestSellers = query.getResultList();
		// for (Product p : bestSellers) {
		// p.getImages().size(); // kích hoạt lazy load ảnh
		// }
		return query.getResultList();

=======
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
>>>>>>> origin/dev
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

	<<<<<<<HEAD

	public List<Product> findNewProduct(int limit) {
		// TODO Auto-generated method stub
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p ORDER BY p.importDate DESC",
				Product.class);
		query.setMaxResults(limit);
		return query.getResultList();
	}

	@Override
	public List<Product> findTopReview(int limit) {
		// TODO Auto-generated method stub
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p ORDER BY p.reviewCount DESC",
				Product.class);
		query.setMaxResults(limit);
		return query.getResultList();
	}

	@Override
	public List<Product> findTopFavorite(int limit) {
		// TODO Auto-generated method stub
		// TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p ORDER BY
		// p.favoriteCount DESC",
		// Product.class);
		// return query.getResultList();
		TypedQuery<Object[]> query = em.createQuery(
				"SELECT p, COUNT(f) as favCount " +
						"FROM Product p LEFT JOIN p.favorites f " +
						"GROUP BY p " +
						"ORDER BY favCount DESC",
				Object[].class);

		List<Object[]> result = query.getResultList();

		// map lại thành Product + favoriteCount
		List<Product> products = result.stream()
				.map(arr -> {
					Product p = (Product) arr[0];
					p.setFavoriteCount(((Long) arr[1]).intValue());
					return p;
				})
				.collect(Collectors.toList());

		return products.subList(0, Math.min(limit, products.size()));
	}

	@Override
	public List<Product> findTopFavoriteinPage(int page, int pageSize) {
		// TODO Auto-generated method stub
		TypedQuery<Object[]> query = em.createQuery(
				"SELECT p, COUNT(f) as favCount " +
						"FROM Product p LEFT JOIN p.favorites f " +
						"GROUP BY p " +
						"ORDER BY favCount DESC",
				Object[].class);

		List<Object[]> result = query.getResultList();

		// map lại thành Product + favoriteCount
		List<Product> products = result.stream()
				.map(arr -> {
					Product p = (Product) arr[0];
					p.setFavoriteCount(((Long) arr[1]).intValue());
					return p;
				})
				.collect(Collectors.toList());

		return products.subList((page - 1) * pageSize, Math.min(pageSize * page, products.size()));
	}

	@Override
	public List<Product> findByCategoryIdinPage(int categoryId, int page, int pageSize) {
		TypedQuery<Product> query = em.createQuery(
				"SELECT p FROM Product p WHERE p.category.categoryID = :cate ORDER BY p.productID DESC",
				Product.class);
		query.setParameter("cate", categoryId);
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	@Override
	public List<Product> findBestSellerinPage(int limit, int page, int pageSize) {
		// TODO Auto-generated method stub
		TypedQuery<Product> query = em.createQuery(
				"SELECT p FROM OrderDetail od " +
						"LEFT JOIN od.product p " +
						"GROUP BY p " +
						"ORDER BY SUM(od.quantity) DESC",
				Product.class);
		query.setMaxResults(limit);
		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);
		List<Product> bestSellers = query.getResultList();
		for (Product p : bestSellers) {
			p.getImages().size(); // kích hoạt lazy load ảnh
		}
		return bestSellers;
	}

	@Override
	public List<Product> findProductsByFilter(ProductFilter filter, int page, int pageSize) {
		String jpql = "SELECT p FROM Product p LEFT JOIN p.orderDetails od LEFT JOIN p.favorites f LEFT JOIN p.banners b WHERE 1=1";

		if (filter.getCategoryId() != null) {
			jpql += " AND p.category.categoryID = :categoryId";
		}
		if (filter.getBrandId() != null) {
			jpql += " AND p.brand.brandID = :brandId";
		}
		if (filter.getBannerId() != null) {
			jpql += " AND b.bannerID = :bannerId";
		}
		if (filter.getMinPrice() != null) {
			jpql += " AND p.unitPrice >= :minPrice";
		}
		if (filter.getMaxPrice() != null) {
			jpql += " AND p.unitPrice <= :maxPrice";
		}
		if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
			jpql += " AND LOWER(p.productName) LIKE :keyword";
		}

		// Xử lý sắp xếp
		if ("0".equals(filter.getSortBy())) {
			// Bán chạy - sắp xếp theo tổng số lượng bán
			jpql += " GROUP BY p ORDER BY COALESCE(SUM(od.quantity), 0) DESC";
		} else if ("1".equals(filter.getSortBy())) {
			// Mới nhất
			jpql += " ORDER BY p.productID DESC";
		} else if ("2".equals(filter.getSortBy())) {
			// Nhiều đánh giá
			jpql += " ORDER BY p.reviewCount DESC";
		} else if ("3".equals(filter.getSortBy())) {
			// Yêu thích - sắp xếp theo số lượng favorite
			jpql += " GROUP BY p ORDER BY COUNT(f) DESC";
		} else if ("4".equals(filter.getSortBy())) {
			// Giá tăng dần
			jpql += " ORDER BY p.unitPrice ASC";
		} else if ("5".equals(filter.getSortBy())) {
			// Giá giảm dần
			jpql += " ORDER BY p.unitPrice DESC";
		} else {
			// Mặc định: mới nhất
			jpql += " ORDER BY p.productID DESC";
		}

		TypedQuery<Product> query = em.createQuery(jpql, Product.class);

		if (filter.getCategoryId() != null)
			query.setParameter("categoryId", filter.getCategoryId());
		if (filter.getBrandId() != null)
			query.setParameter("brandId", filter.getBrandId());
		if (filter.getBannerId() != null)
			query.setParameter("bannerId", filter.getBannerId());
		if (filter.getMinPrice() != null)
			query.setParameter("minPrice", filter.getMinPrice());
		if (filter.getMaxPrice() != null)
			query.setParameter("maxPrice", filter.getMaxPrice());
		if (filter.getKeyword() != null && !filter.getKeyword().isEmpty())
			query.setParameter("keyword", "%" + filter.getKeyword().toLowerCase() + "%");

		query.setFirstResult((page - 1) * pageSize);
		query.setMaxResults(pageSize);

		return query.getResultList();
	}

	@Override
	public List<Product> findByPriceRangeinPage(double minPrice, double maxPrice, int page, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int countProductsByFilter(ProductFilter filter) {
		String jpql = "SELECT COUNT(DISTINCT p) FROM Product p LEFT JOIN p.orderDetails od LEFT JOIN p.favorites f LEFT JOIN p.banners b WHERE 1=1";

		if (filter.getCategoryId() != null) {
			jpql += " AND p.category.categoryID = :categoryId";
		}
		if (filter.getBrandId() != null) {
			jpql += " AND p.brand.brandID = :brandId";
		}
		if (filter.getBannerId() != null) {
			jpql += " AND b.bannerID = :bannerId";
		}
		if (filter.getMinPrice() != null) {
			jpql += " AND p.unitPrice >= :minPrice";
		}
		if (filter.getMaxPrice() != null) {
			jpql += " AND p.unitPrice <= :maxPrice";
		}
		if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
			jpql += " AND LOWER(p.productName) LIKE :keyword";
		}

		TypedQuery<Long> query = em.createQuery(jpql, Long.class);

		if (filter.getCategoryId() != null)
			query.setParameter("categoryId", filter.getCategoryId());
		if (filter.getBrandId() != null)
			query.setParameter("brandId", filter.getBrandId());
		if (filter.getBannerId() != null)
			query.setParameter("bannerId", filter.getBannerId());
		if (filter.getMinPrice() != null)
			query.setParameter("minPrice", filter.getMinPrice());
		if (filter.getMaxPrice() != null)
			query.setParameter("maxPrice", filter.getMaxPrice());
		if (filter.getKeyword() != null && !filter.getKeyword().isEmpty())
			query.setParameter("keyword", "%" + filter.getKeyword().toLowerCase() + "%");

		return query.getSingleResult().intValue();
	}

	=======>>>>>>>origin/dev
}
