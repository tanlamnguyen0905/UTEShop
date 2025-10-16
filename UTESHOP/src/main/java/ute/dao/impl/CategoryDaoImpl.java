package ute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.CategoryDao;
import ute.entities.Categories;

public class CategoryDaoImpl implements CategoryDao {
	@Override
	public List<Categories> findAll() {
		EntityManager em = JPAConfig.getEntityManager();
		TypedQuery<Categories> query = em.createQuery("SELECT c FROM Categories c", Categories.class);
		try {
			return query.getResultList();			
		} finally {
			em.close();
		}
	}

}
