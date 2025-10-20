package ute.dao.impl;

// ...existing code...
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import ute.entities.Banner;
import ute.entities.Product;
import ute.configs.JPAConfig;
import ute.dao.inter.BannerDao;

public class BannerDaoImpl implements BannerDao {

	private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("UTESHOP");

	@Override
	public List<Banner> findAll() {
		EntityManager em = EMF.createEntityManager();
		try {
			String jpql = "SELECT DISTINCT b FROM Banner b LEFT JOIN FETCH b.products p LEFT JOIN FETCH p.images";
			TypedQuery<Banner> q = em.createQuery(jpql, Banner.class);
			return q.getResultList();
		} finally {
			em.close();
		}
	}<<<<<<<HEAD

	@Override
	public List<Product> findByBannerId(int bannerId) {
		EntityManager em = EMF.createEntityManager();
		try {
			String jpql = "SELECT DISTINCT p FROM Product p "
					+ "JOIN FETCH p.banners b "
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
		// TODO Auto-generated method stub
		EntityManager em = EMF.createEntityManager();
	    TypedQuery<Product> query = em.createQuery(
	            "SELECT p FROM Product p WHERE p.banners.bannerID = :bannerID ORDER BY p.productID DESC",
	            Product.class
	        );
	        query.setParameter("bannerID", bannerId);
	        query.setFirstResult((page - 1) * pageSize);
	        query.setMaxResults(pageSize);
	        return query.getResultList();
	}

}=======

}>>>>>>>origin/dev
