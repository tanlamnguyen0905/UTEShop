package ute.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.MessageDao;
import ute.entities.Message;
import java.util.List;

public class MessageDaoImpl implements MessageDao {

    @Override
    public Message save(Message m) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(m);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive())
                tx.rollback();
            throw e;
        } finally {
            em.close();
        }
        return m;
    }

    @Override
    public List<Message> findRecentByRoom(String roomId, int limit) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Message> query = em.createQuery("SELECT m FROM Message m WHERE m.roomId = :roomId ORDER BY m.createdAt DESC", Message.class);
        query.setParameter("roomId", roomId);
        query.setMaxResults(limit);
        return query.getResultList();
    } finally {
        em.close();
    }
    }
}
