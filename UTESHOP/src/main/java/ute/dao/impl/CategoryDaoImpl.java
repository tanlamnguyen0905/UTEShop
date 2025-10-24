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
		EntityManager em = null;
		try {
			em = JPAConfig.getEntityManager();
			if (em == null) {
				throw new RuntimeException("EntityManager is null - Database connection failed");
			}
			TypedQuery<Categories> query = em.createQuery("SELECT c FROM Categories c", Categories.class);
			return query.getResultList();			
		} catch (Exception e) {
			System.err.println("Lỗi trong CategoryDaoImpl.findAll(): " + e.getMessage());
			e.printStackTrace();
			throw e;
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

}
