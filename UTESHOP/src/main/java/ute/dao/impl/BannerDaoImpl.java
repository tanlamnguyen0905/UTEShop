package ute.dao.impl;

// ...existing code...
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import ute.entities.Banner;
import ute.entities.Product;
import jakarta.persistence.Persistence;
import ute.configs.JPAConfig;
import ute.dao.inter.BannerDao;

public class BannerDaoImpl implements BannerDao {

	@Override
	public List<Banner> findAll() {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT DISTINCT b FROM Banner b LEFT JOIN FETCH b.products p LEFT JOIN FETCH p.images";
			TypedQuery<Banner> q = em.createQuery(jpql, Banner.class);
			return q.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findByBannerId(int bannerId) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			String jpql = "SELECT DISTINCT p FROM Product p "
					+ "JOIN p.banners b "
					+ "LEFT JOIN FETCH p.images "
					+ "WHERE b.bannerID = :bannerId";
			TypedQuery<Product> query = em.createQuery(jpql, Product.class);
			query.setParameter("bannerId", (long) bannerId);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Product> findProductsByBannerIdinPage(int bannerId, int page, int pageSize) {
		EntityManager em = JPAConfig.getEntityManager();
		try {
			TypedQuery<Product> query = em.createQuery(
					"SELECT p FROM Product p JOIN p.banners b WHERE b.bannerID = :bannerID ORDER BY p.productID DESC",
					Product.class);
			query.setParameter("bannerID", (long) bannerId);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

}
