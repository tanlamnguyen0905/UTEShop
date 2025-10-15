package ute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.CategoryDao;
import ute.entities.Categories;

public class CategoryDaoImpl implements CategoryDao {
	private EntityManager em = Persistence.createEntityManagerFactory("UTESHOP").createEntityManager();;

	@Override
	public List<Categories> findAll() {
		TypedQuery<Categories> query = em.createQuery("SELECT c FROM Categories c", Categories.class);
		return query.getResultList();
	}

}
