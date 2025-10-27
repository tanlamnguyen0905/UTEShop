package ute.dao.admin.Impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.admin.inter.BrandDao;
import ute.entities.Brand;

public class BrandDaoImpl implements BrandDao {

    private static final String BASE_QUERY = "SELECT b FROM Brand b";

    @Override
    public void insert(Brand brand) {
        EntityManager em = JPAConfig.getEntityManager();
        var trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(brand);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Brand brand) {
        EntityManager em = JPAConfig.getEntityManager();
        var trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(brand);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = JPAConfig.getEntityManager();
        var trans = em.getTransaction();
        try {
            trans.begin();
            Brand b = em.find(Brand.class, id);
            if (b != null) {
                em.remove(b);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Brand findById(Long id) {  // FIX: Long
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.find(Brand.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Brand> findAll() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Brand> query = em.createQuery(BASE_QUERY, Brand.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Brand> findByName(String name) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Brand> query = em.createQuery(BASE_QUERY + " WHERE LOWER(b.brandName) LIKE LOWER(:name)", Brand.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Brand> findByNamePaginated(String name, int firstResult, int maxResults) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Brand> query = em.createQuery(BASE_QUERY + " WHERE LOWER(b.brandName) LIKE LOWER(:name) ORDER BY b.brandID DESC", Brand.class);
            query.setParameter("name", "%" + name + "%");
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByName(String name) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(b) FROM Brand b WHERE LOWER(b.brandName) LIKE LOWER(:name)", Long.class);
            query.setParameter("name", "%" + name + "%");
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(b) FROM Brand b", Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Brand> findPage(int page, int pageSize) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Brand> query = em.createQuery(BASE_QUERY + " ORDER BY b.brandID DESC", Brand.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}