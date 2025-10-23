package ute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.CategoriesDao;
import ute.entities.Categories;

public class CategoriesDaoImpl implements CategoriesDao {

    @Override
    public void insert(Categories category) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(category);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Categories category) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(category);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Categories category = em.find(Categories.class, id);
            if (category != null) {
                em.remove(category);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public Categories findById(Long id) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Categories> query = em.createQuery(
                    "SELECT c FROM Categories c LEFT JOIN FETCH c.products WHERE c.categoryID = :id",
                    Categories.class);
            query.setParameter("id", id);
            List<Categories> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Categories> findAll() {
        EntityManager em = JPAConfig.getEntityManager();
        TypedQuery<Categories> query = em.createQuery("SELECT c FROM Categories c LEFT JOIN FETCH c.products", Categories.class);
        try {
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Categories> findByName(String name) {
        EntityManager em = JPAConfig.getEntityManager();
        TypedQuery<Categories> query = em.createQuery(
                "SELECT c FROM Categories c LEFT JOIN FETCH c.products WHERE LOWER(c.categoryName) LIKE LOWER(:name)", Categories.class);
        query.setParameter("name", "%" + name + "%");
        try {
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Categories findByNameExact(String name) {
        EntityManager em = JPAConfig.getEntityManager();
        TypedQuery<Categories> query = em.createQuery(
                "SELECT c FROM Categories c LEFT JOIN FETCH c.products WHERE LOWER(c.categoryName) = LOWER(:name)", Categories.class);
        query.setParameter("name", name);
        try {
            List<Categories> result = query.getResultList();
            return result.isEmpty() ? null : result.get(0);
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = JPAConfig.getEntityManager();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Categories c", Long.class);
        try {
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Categories> findPage(int page, int pageSize) {
        EntityManager em = JPAConfig.getEntityManager();
        TypedQuery<Categories> query = em.createQuery("SELECT c FROM Categories c LEFT JOIN FETCH c.products ORDER BY c.categoryID", Categories.class);
        query.setFirstResult((page - 1) * pageSize);
        query.setMaxResults(pageSize);
        try {
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Categories> findByNamePaginated(String name, int firstResult, int maxResults) {
        EntityManager em = JPAConfig.getEntityManager();
        TypedQuery<Categories> query = em.createQuery(
                "SELECT c FROM Categories c LEFT JOIN FETCH c.products WHERE LOWER(c.categoryName) LIKE LOWER(:name) ORDER BY c.categoryID", Categories.class);
        query.setParameter("name", "%" + name + "%");
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        try {
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByName(String name) {
        EntityManager em = JPAConfig.getEntityManager();
        TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(c) FROM Categories c WHERE LOWER(c.categoryName) LIKE LOWER(:name)", Long.class);
        query.setParameter("name", "%" + name + "%");
        try {
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}