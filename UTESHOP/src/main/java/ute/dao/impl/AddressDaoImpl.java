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

            // Debug log
            System.out.println("=== DAO INSERT ADDRESS ===");
            System.out.println("Name: " + address.getName());
            System.out.println("Phone: " + address.getPhone());
            System.out.println("Province: " + address.getProvince());
            System.out.println("==========================");

            em.persist(address);
            trans.commit();

            System.out.println("Address inserted with ID: " + address.getAddressID());
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

            // Tìm entity hiện tại trong DB
            Addresses existing = em.find(Addresses.class, address.getAddressID());
            if (existing != null) {
                // Debug log - Before update
                System.out.println("=== DAO UPDATE ADDRESS ===");
                System.out.println("AddressID: " + address.getAddressID());
                System.out.println("Old Phone: " + existing.getPhone());
                System.out.println("New Phone: " + address.getPhone());

                // Update từng field một để đảm bảo không bị mất dữ liệu
                existing.setName(address.getName());
                existing.setPhone(address.getPhone());
                existing.setProvince(address.getProvince());
                existing.setDistrict(address.getDistrict());
                existing.setWard(address.getWard());
                existing.setAddressDetail(address.getAddressDetail());
                existing.setIsDefault(address.getIsDefault());
                // User không thay đổi nên không cần set

                System.out.println("Updated Phone: " + existing.getPhone());
                System.out.println("==========================");
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

    @Override
    public void unsetDefaultForUser(Long userID) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            String jpql = "UPDATE Addresses a SET a.isDefault = false WHERE a.user.userID = :userID";
            em.createQuery(jpql)
                    .setParameter("userID", userID)
                    .executeUpdate();
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
