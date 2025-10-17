package ute.dao.impl;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ute.entities.Banner;
import ute.entities.Product;
import ute.configs.JPAConfig;
import ute.dao.inter.BannerDao;

public class BannerDaoImpl implements BannerDao {
	@Override
	public List<Banner> findAll() {
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<Banner> query = em.createQuery("SELECT b FROM Banner b", Banner.class);
		try {
			return query.getResultList();			
		} finally {
			em.close();
		}
	}
	@Override
	public List<Product> findByBannerId(int bannerId) {
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Banner p WHERE p.banner.id = :bannerId", Product.class);
		query.setParameter("bannerId", bannerId);
		try {
			return query.getResultList();			
		} finally {
			em.close();
		}
	}
	
}
