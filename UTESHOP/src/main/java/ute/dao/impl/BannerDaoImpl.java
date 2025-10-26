package ute.dao.impl;

import jakarta.persistence.*;
import java.util.List;
import ute.configs.JPAConfig;
import ute.dao.inter.BannerDao;
import ute.entities.Banner;
import ute.entities.Product;

public class BannerDaoImpl implements BannerDao {

	// === COMMON HELPER ===
	private EntityManager em() {
		return JPAConfig.getEntityManager();
	}

	// ---------------- FINDERS ----------------
	@Override
	public List<Banner> findAll() {
		EntityManager em = em();
		try {
			String jpql = """
					    SELECT DISTINCT b
					    FROM Banner b
					    LEFT JOIN FETCH b.products p
					    LEFT JOIN FETCH p.images
					    LEFT JOIN FETCH p.brand
					""";
			return em.createQuery(jpql, Banner.class).getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public Banner findById(Long id) {
		EntityManager em = em();
		try {
			String jpql = """
					    SELECT DISTINCT b
					    FROM Banner b
					    left JOIN FETCH b.products p
						LEFT JOIN FETCH p.images
					    LEFT JOIN FETCH p.brand
					    WHERE b.bannerID = :id
					""";
			TypedQuery<Banner> q = em.createQuery(jpql, Banner.class);
			q.setParameter("id", id);
			List<Banner> result = q.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findByBannerId(int bannerId) {
		EntityManager em = em();
		try {
			String jpql = """
					    SELECT DISTINCT p
					    FROM Product p
					    right JOIN FETCH p.banners b
					    LEFT JOIN FETCH p.images
					    LEFT JOIN FETCH p.brand
					    WHERE b.bannerID = :bannerId
					""";
			return em.createQuery(jpql, Product.class)
					.setParameter("bannerId", (long) bannerId)
					.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findProductsByBannerIdinPage(int bannerId, int page, int pageSize) {
		EntityManager em = em();
		try {
			String jpql = """
					    SELECT DISTINCT p
					    FROM Product p
					    right JOIN p.banners b
					    WHERE b.bannerID = :bannerID
					    ORDER BY p.productID DESC
					""";
			TypedQuery<Product> q = em.createQuery(jpql, Product.class);
			q.setParameter("bannerID", (long) bannerId);
			q.setFirstResult((page - 1) * pageSize);
			q.setMaxResults(pageSize);
			return q.getResultList();
		} finally {
			em.close();
		}
	}

	// ---------------- CRUD ----------------
	@Override
	public void insert(Banner banner) {
		EntityManager em = em();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(banner);
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	public void update(Banner banner) {
		EntityManager em = em();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.merge(banner);
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	@Override
	public void delete(Long id) {
		EntityManager em = em();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Banner b = em.find(Banner.class, id);
			if (b != null)
				em.remove(b);
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
	}

	// ---------------- RELATIONS ----------------
	@Override
	public void addProductToBanner(Long bannerId, Long productId) {
		EntityManager em = em();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Banner banner = em.find(Banner.class, bannerId);
			Product product = em.find(Product.class, productId);
			if (banner == null || product == null)
				throw new IllegalArgumentException("Banner hoặc sản phẩm không tồn tại!");

			banner.getProducts().add(product);
			product.getBanners().add(banner);

			em.merge(banner); // ✅ merge banner, không merge product
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			throw new RuntimeException("Lỗi khi thêm product vào banner: " + e.getMessage(), e);
		} finally {
			em.close();
		}
	}

	@Override
	public void removeProductFromBanner(Long bannerId, Long productId) {
		EntityManager em = em();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Banner banner = em.find(Banner.class, bannerId);
			Product product = em.find(Product.class, productId);
			if (banner == null || product == null)
				throw new IllegalArgumentException("Banner hoặc sản phẩm không tồn tại!");

			banner.getProducts().remove(product);
			product.getBanners().remove(banner);

			em.merge(banner); // ✅ merge banner
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			throw new RuntimeException("Lỗi khi xóa product khỏi banner: " + e.getMessage(), e);
		} finally {
			em.close();
		}
	}

	@Override
	public void clearProductFromBanner(Long bannerId) {
		EntityManager em = em();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Banner banner = em.find(Banner.class, bannerId);
			if (banner == null)
				throw new IllegalArgumentException("Banner không tồn tại!");

			// Sao chép danh sách tránh ConcurrentModificationException
			for (Product product : List.copyOf(banner.getProducts())) {
				product.getBanners().remove(banner);
			}
			banner.getProducts().clear();

			em.merge(banner);
			tx.commit();
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			throw new RuntimeException("Lỗi khi clear product khỏi banner: " + e.getMessage(), e);
		} finally {
			em.close();
		}
	}

	// ---------------- PAGINATION ----------------
	@Override
	public List<Banner> findPage(int page, int pageSize) {
		EntityManager em = em();
		try {
			String jpql = "SELECT b FROM Banner b left join fetch b.products ORDER BY b.bannerID";
			TypedQuery<Banner> q = em.createQuery(jpql, Banner.class);
			q.setFirstResult((page - 1) * pageSize);
			q.setMaxResults(pageSize);
			return q.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public long count() {
		EntityManager em = em();
		try {
			return em.createQuery("SELECT COUNT(b) FROM Banner b", Long.class).getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public long countByName(String name) {
		EntityManager em = em();
		try {
			return em
					.createQuery("SELECT COUNT(b) FROM Banner b WHERE LOWER(b.bannerName) LIKE LOWER(:name)",
							Long.class)
					.setParameter("name", "%" + name + "%")
					.getSingleResult();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Banner> findByNamePaginated(String name, int firstResult, int maxResults) {
		EntityManager em = em();
		try {
			String jpql = """
					    SELECT b
					    FROM Banner b
						left join fetch b.products p
					    WHERE LOWER(b.bannerName) LIKE LOWER(:name)
					    ORDER BY b.bannerID
					""";
			TypedQuery<Banner> q = em.createQuery(jpql, Banner.class);
			q.setParameter("name", "%" + name + "%");
			q.setFirstResult(firstResult);
			q.setMaxResults(maxResults);
			return q.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public Banner findByNameExact(String name) {
		EntityManager em = em();
		try {
			String jpql = """
					    SELECT b
					    FROM Banner b
						left join fetch b.products
					    WHERE LOWER(b.bannerName) = LOWER(:name)
					""";
			TypedQuery<Banner> q = em.createQuery(jpql, Banner.class);
			q.setParameter("name", name);
			List<Banner> result = q.getResultList();
			return result.isEmpty() ? null : result.get(0);
		} finally {
			em.close();
		}
	}

	@Override
	public List<Banner> findByName(String name) {
		// TODO Auto-generated method stub
		EntityManager em = em();
		try {
			String jpql = """
					    SELECT b
					    FROM Banner b
						left join fetch b.products
					    WHERE LOWER(b.bannerName) = LOWER(:name)
					""";
			TypedQuery<Banner> q = em.createQuery(jpql, Banner.class);
			q.setParameter("name", name);
			List<Banner> result = q.getResultList();
			return result.isEmpty() ? null : result;
		} finally {
			em.close();
		}
	}
}
