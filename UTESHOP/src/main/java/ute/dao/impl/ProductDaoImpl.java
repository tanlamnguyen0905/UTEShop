package ute.dao.impl;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.ProductDao;
import ute.entities.Image;
import ute.entities.Product;
import ute.utils.ProductFilter;

public class ProductDaoImpl implements ProductDao {

	@Override
	public void insert(Product product) {
		EntityManager em = JPAConfig.getEntityManager();
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();

			// Giải quyết lỗi cascade: ensure product <-> images đồng bộ trong cùng context
			if (product.getImages() != null) {
				for (Image img : product.getImages()) {
					img.setProduct(product);
				}
			}

			em.persist(product);
			trans.commit();
		} catch (Exception e) {
			if (trans.isActive())
				trans.rollback();
			e.printStackTrace();
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

			if (product.getImages() != null) {
				for (Image img : product.getImages()) {
					img.setProduct(product);
				}
			}

			em.merge(product);
			trans.commit();
		} catch (Exception e) {
			if (trans.isActive())
				trans.rollback();
			e.printStackTrace();
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
		try {
			TypedQuery<Product> query = em.createQuery(
					"SELECT p FROM Product p " +
							"LEFT JOIN p.orderDetails od " + // ← Thống nhất với findBestSeller
							"GROUP BY p " +
							"ORDER BY COALESCE(SUM(od.quantity), 0) DESC",
					Product.class);
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
			// ORDER BY số lượng reviews thực tế
			TypedQuery<Product> query = em.createQuery(
					"SELECT p FROM Product p LEFT JOIN p.reviews r GROUP BY p ORDER BY COUNT(r) DESC",
					Product.class);
			query.setMaxResults(limit);
			List<Product> products = query.getResultList();
			// Initialize collections
			for (Product p : products) {
				if (p.getImages() != null)
					p.getImages().size();
				if (p.getFavorites() != null)
					p.getFavorites().size();
				if (p.getReviews() != null)
					p.getReviews().size();
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
					"SELECT p FROM Product p " +
							"LEFT JOIN p.orderDetails od " + // ← Thống nhất với findBestSeller
							"GROUP BY p " +
							"ORDER BY COALESCE(SUM(od.quantity), 0) DESC",
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
		String sortBy = filter.getSortBy() != null ? filter.getSortBy() : "1";

		// Xác định các JOIN cần thiết
		boolean needOrderDetails = "0".equals(sortBy) && useOrderDetailSort(); // Kiểm tra config
		boolean needReviews = "2".equals(sortBy);
		boolean needFavorites = "3".equals(sortBy);
		boolean needBanners = filter.getBannerId() != null;

		StringBuilder jpql = new StringBuilder();

		// Xây dựng SELECT clause
		if (needFavorites) {
			jpql.append("SELECT p, COUNT(f) as favCount FROM Product p");
		} else {
			jpql.append("SELECT p FROM Product p");
		}

		// Xây dựng JOIN clause
		if (needOrderDetails) {
			jpql.append(" LEFT JOIN p.orderDetails od");
		}
		if (needReviews) {
			jpql.append(" LEFT JOIN p.reviews r");
		}
		if (needFavorites) {
			jpql.append(" LEFT JOIN p.favorites f");
		}
		if (needBanners) {
			jpql.append(" LEFT JOIN p.banners b");
		}

		jpql.append(" WHERE 1=1");

		// WHERE conditions
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

		// GROUP BY và ORDER BY
		switch (sortBy) {
			case "0": // Bán chạy
				if (needOrderDetails) {
					// Tính từ OrderDetail
					jpql.append(" GROUP BY p ORDER BY COALESCE(SUM(od.quantity), 0) DESC");
				} else {
					// Dùng field có sẵn
					jpql.append(" ORDER BY p.soldCount DESC");
				}
				break;
			case "1": // Mới nhất
				jpql.append(" ORDER BY p.productID DESC");
				break;
			case "2": // Nhiều đánh giá
				jpql.append(" GROUP BY p ORDER BY COUNT(r) DESC");
				break;
			case "3": // Yêu thích
				jpql.append(" GROUP BY p ORDER BY COUNT(f) DESC");
				break;
			case "4": // Giá tăng dần
				jpql.append(" ORDER BY p.unitPrice ASC");
				break;
			case "5": // Giá giảm dần
				jpql.append(" ORDER BY p.unitPrice DESC");
				break;
			default:
				jpql.append(" ORDER BY p.productID DESC");
		}

		EntityManager em = JPAConfig.getEntityManager();
		try {
			List<Product> products;

			if (needFavorites) {
				// Query trả về Object[]
				TypedQuery<Object[]> query = em.createQuery(jpql.toString(), Object[].class);
				setParameters(query, filter);
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
				// Query trả về Product
				TypedQuery<Product> query = em.createQuery(jpql.toString(), Product.class);
				setParameters(query, filter);
				query.setFirstResult((page - 1) * pageSize);
				query.setMaxResults(pageSize);

				products = query.getResultList();
			}

			// Initialize lazy collections
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

	private void setParameters(TypedQuery<?> query, ProductFilter filter) {
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
	}

	// Helper method để check config
	private boolean useOrderDetailSort() {
		// Kiểm tra nếu có field soldCount trong Product entity
		// Nếu có → return false (dùng field)
		// Nếu không → return true (tính từ OrderDetail)
		return false; // Default: dùng field soldCount
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
					Long.class);
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
