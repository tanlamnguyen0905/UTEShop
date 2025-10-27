package ute.dao.impl;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.ProductDao;
import ute.entities.Product;
import ute.utils.ProductFilter;

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
			if (trans.isActive())
				trans.rollback();
			throw e;
		} finally {
			em.close();
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
			if (trans.isActive())
				trans.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	public void delete(int id) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			Product p = em.find(Product.class, Long.valueOf(id));
			if (p != null) {
				em.remove(p);
			}
			trans.commit();
		} catch (Exception e) {
			if (trans.isActive())
				trans.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	public Product findById(int id) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			// Fetch product with images first
			TypedQuery<Product> queryWithImages = em.createQuery(
					"SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.productID = :id",
					Product.class);
			queryWithImages.setParameter("id", Long.valueOf(id));
			Product product = queryWithImages.getSingleResult();

			// Then fetch favorites in a separate query
			TypedQuery<Product> queryWithFavorites = em.createQuery(
					"SELECT p FROM Product p LEFT JOIN FETCH p.favorites WHERE p.productID = :id",
					Product.class);
			queryWithFavorites.setParameter("id", Long.valueOf(id));
			product = queryWithFavorites.getSingleResult();

			// Force initialize collections
			if (product.getImages() != null) {
				product.getImages().size();
			}
			if (product.getFavorites() != null) {
				product.getFavorites().size();
			}

			return product;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findAll() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Product> query = em.createQuery("SELECT p FROM  Product p", Product.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findByName(String name) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Product> query = em.createQuery(
					"SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(:name)", Product.class);
			query.setParameter("name", "%" + name + "%");
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findByCategoryId(int categoryId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p WHERE p.category.categoryID = :id",
					Product.class);
			query.setParameter("id", (long) categoryId);
			List<Product> cate = query.getResultList();
			// Initialize collections
			for (Product p : cate) {
				if (p.getImages() != null)
					p.getImages().size();
				if (p.getFavorites() != null)
					p.getFavorites().size();
			}
			return cate;
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
			// Initialize collections
			for (Product p : bestSellers) {
				if (p.getImages() != null)
					p.getImages().size();
				if (p.getFavorites() != null)
					p.getFavorites().size();
			}
			return bestSellers;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findByPriceRange(double minPrice, double maxPrice) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Product> query = em.createQuery(
					"SELECT p FROM Product p WHERE p.unitPrice BETWEEN :min AND :max",
					Product.class);
			query.setParameter("min", minPrice);
			query.setParameter("max", maxPrice);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public long count() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Long> query = em.createQuery("SELECT COUNT(p) FROM Product p", Long.class);
			return query.getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findPage(int page, int pageSize) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p", Product.class);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findNewProduct(int limit) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p ORDER BY p.importDate DESC",
					Product.class);
			query.setMaxResults(limit);
			List<Product> products = query.getResultList();
			// Initialize collections
			for (Product p : products) {
				if (p.getImages() != null)
					p.getImages().size();
				if (p.getFavorites() != null)
					p.getFavorites().size();
			}
			return products;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findTopReview(int limit) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Product> query = em.createQuery("SELECT p FROM Product p ORDER BY p.reviewCount DESC",
					Product.class);
			query.setMaxResults(limit);
			List<Product> products = query.getResultList();
			// Initialize collections
			for (Product p : products) {
				if (p.getImages() != null)
					p.getImages().size();
				if (p.getFavorites() != null)
					p.getFavorites().size();
			}
			return products;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findTopFavorite(int limit) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Object[]> query = em.createQuery(
					"SELECT p, COUNT(f) as favCount " +
							"FROM Product p LEFT JOIN p.favorites f " +
							"GROUP BY p " +
							"ORDER BY favCount DESC",
					Object[].class);

			List<Object[]> result = query.getResultList();

			// map lại thành Product + favoriteCount và initialize collections
			List<Product> products = result.stream()
					.map(arr -> {
						Product p = (Product) arr[0];
						p.setFavoriteCount(((Long) arr[1]).intValue());
						if (p.getImages() != null)
							p.getImages().size();
						if (p.getFavorites() != null)
							p.getFavorites().size();
						return p;
					})
					.collect(Collectors.toList());

			return products.subList(0, Math.min(limit, products.size()));
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findTopFavoriteinPage(int page, int pageSize) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
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

			int start = (page - 1) * pageSize;
			if (start >= products.size() || start < 0) {
				return java.util.Collections.emptyList();
			}
			int end = Math.min(page * pageSize, products.size());
			return products.subList(start, end);
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findByCategoryIdinPage(int categoryId, int page, int pageSize) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Product> query = em.createQuery(
					"SELECT p FROM Product p WHERE p.category.categoryID = :cate ORDER BY p.productID DESC",
					Product.class);
			query.setParameter("cate", (long) categoryId);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findBestSellerinPage(int limit, int page, int pageSize) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Product> query = em.createQuery(
					"SELECT p FROM OrderDetail od right JOIN od.product p GROUP BY p ORDER BY SUM(od.quantity) DESC",
					Product.class);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			List<Product> bestSellers = query.getResultList();
			for (Product p : bestSellers) {
				if (p.getImages() != null)
					p.getImages().size();
				if (p.getFavorites() != null)
					p.getFavorites().size();
			}
			return bestSellers;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findProductsByFilter(ProductFilter filter, int page, int pageSize) {
		// Xác định xem cần JOIN bảng nào dựa trên filter và sortBy
		boolean needOrderDetails = "0".equals(filter.getSortBy()); // Bán chạy
		boolean needBanners = filter.getBannerId() != null; // Chỉ JOIN khi filter theo banner

		// Xây dựng JPQL động dựa trên loại sắp xếp
		StringBuilder jpql = new StringBuilder();
		String sortBy = filter.getSortBy() != null ? filter.getSortBy() : "1";

		if ("3".equals(sortBy)) { // Trường hợp sắp xếp theo yêu thích
			jpql.append("SELECT p, COUNT(f) as favCount FROM Product p LEFT JOIN p.favorites f");
		} else {
			jpql.append("SELECT DISTINCT p FROM Product p");
			if (needOrderDetails) {
				jpql.append(" LEFT JOIN p.orderDetails od");
			}
		}

		if (needBanners) {
			jpql.append(" LEFT JOIN p.banners b");
		}

		jpql.append(" WHERE 1=1");

		// Thêm các điều kiện filter
		if (filter.getCategoryId() != null) {
			jpql.append(" AND p.category.categoryID = :categoryId");
		}
		if (filter.getBrandId() != null) {
			jpql.append(" AND p.brand.brandID = :brandId");
		}
		if (filter.getBannerId() != null) {
			jpql.append(" AND b.bannerID = :bannerId");
		}
		if (filter.getMinPrice() != null) {
			jpql.append(" AND p.unitPrice >= :minPrice");
		}
		if (filter.getMaxPrice() != null) {
			jpql.append(" AND p.unitPrice <= :maxPrice");
		}
		if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
			jpql.append(" AND LOWER(p.productName) LIKE :keyword");
		}

		// Xử lý sắp xếp
		switch (sortBy) {
			case "0": // Bán chạy
				jpql.append(" ORDER BY p.soldCount DESC");
				break;
			case "1": // Mới nhất (default)
				jpql.append(" ORDER BY p.productID DESC");
				break;
			case "2": // Nhiều đánh giá
				jpql.append(" ORDER BY p.reviewCount DESC");
				break;
			case "3": // Yêu thích
				jpql.append(" GROUP BY p ORDER BY favCount DESC");
				break;
			case "4": // Giá tăng dần
				jpql.append(" ORDER BY p.unitPrice ASC");
				break;
			case "5": // Giá giảm dần
				jpql.append(" ORDER BY p.unitPrice DESC");
				break;
			default: // Mặc định: mới nhất
				jpql.append(" ORDER BY p.productID DESC");
		}

		EntityManager em = JPAConfig.getEntityManager();
		try {
			// Xác định kiểu query dựa trên sortBy
			List<Product> products;
			if ("3".equals(filter.getSortBy())) {
				TypedQuery<Object[]> query = em.createQuery(jpql.toString(), Object[].class);

				// Set parameters
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

				// Pagination
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);

				products = query.getResultList().stream()
						.map(row -> {
							Product p = (Product) row[0];
							p.setFavoriteCount(((Long) row[1]).intValue());
							return p;
						})
						.collect(Collectors.toList());
			} else {
				TypedQuery<Product> query = em.createQuery(jpql.toString(), Product.class);

				// Set parameters
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

				// Pagination
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);

				products = query.getResultList();
			}

			// Initialize lazy-loaded collections để tránh LazyInitializationException
			for (Product p : products) {
				if (p.getImages() != null)
					p.getImages().size();
				if (p.getFavorites() != null)
					p.getFavorites().size();
			}

			return products;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findByPriceRangeinPage(double minPrice, double maxPrice, int page, int pageSize) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Product> query = em.createQuery(
					"SELECT p FROM Product p WHERE p.unitPrice BETWEEN :min AND :max ORDER BY p.productID DESC",
					Product.class);
			query.setParameter("min", minPrice);
			query.setParameter("max", maxPrice);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public Long getActiveProductCount() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Long> query = em.createQuery(
				"SELECT COUNT(p) FROM Product p WHERE p.stockQuantity > 0",
				Long.class
			);
			return query.getSingleResult();
		} finally {
			em.close();
		}
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

		EntityManager em = JPAConfig.getEntityManager();
		try {
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
		} finally {
			em.close();
		}
	}

}
