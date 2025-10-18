package ute.dao.impl;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import ute.entities.Banner;
import ute.entities.Product;
import ute.dao.inter.BannerDao;

public class BannerDaoImpl implements BannerDao {

	private EntityManager em =  Persistence.createEntityManagerFactory("UTESHOP").createEntityManager();
	@Override
	public List<Banner> findAll() {
		// TODO Auto-generated method stub
	    try {
	        String jpql = "SELECT DISTINCT b FROM Banner b LEFT JOIN FETCH b.products p LEFT JOIN FETCH p.images";
	        return em.createQuery(jpql, Banner.class).getResultList();
	    } finally {
	        em.close();
	    }

//		TypedQuery<Banner> query = em.createQuery("SELECT b FROM Banner b", Banner.class);
//		return query.getResultList();
	}
	@Override
	public List<Product> findByBannerId(int bannerId) {
		TypedQuery<Product> query = em.createQuery("SELECT p FROM Banner p WHERE p.banner.id = :bannerId", Product.class);
		query.setParameter("bannerId", bannerId);
		return query.getResultList();
	}

	
}
