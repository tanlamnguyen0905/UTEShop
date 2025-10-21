package ute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.AddressDao;
import ute.entities.Addresses;

public class AddressDaoImpl implements AddressDao {

    @Override
    public void insert(Addresses address) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(address);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Addresses address) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(address);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(Long addressID) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            Addresses address = em.find(Addresses.class, addressID);
            if (address != null) {
                em.remove(address);
            }
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Addresses findById(Long addressID) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.find(Addresses.class, addressID);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Addresses> findByUserId(Long userID) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT a FROM Addresses a WHERE a.user.userID = :userID ORDER BY a.addressID DESC";
            TypedQuery<Addresses> query = em.createQuery(jpql, Addresses.class);
            query.setParameter("userID", userID);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public int countByUserId(Long userID) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = "SELECT COUNT(a) FROM Addresses a WHERE a.user.userID = :userID";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("userID", userID);
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }
}

