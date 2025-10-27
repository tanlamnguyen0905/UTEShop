package ute.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.inter.MessageDao;
import ute.entities.Message;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<Map<String, Object>> findAllRoomsWithLastMessage() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String sql = "SELECT m1.RoomId, m1.Content, m1.SenderName, m1.Role, m1.CreatedAt, " +
                        "(SELECT COUNT(*) FROM Message m2 WHERE m2.RoomId = m1.RoomId AND m2.Seen = 0) as UnreadCount " +
                        "FROM Message m1 " +
                        "WHERE m1.IdMessage IN ( " +
                        "    SELECT MAX(m3.IdMessage) FROM Message m3 GROUP BY m3.RoomId " +
                        ") " +
                        "ORDER BY m1.CreatedAt DESC";
            
            Query query = em.createNativeQuery(sql);
            @SuppressWarnings("unchecked")
            List<Object[]> results = query.getResultList();
            
            List<Map<String, Object>> rooms = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> room = new HashMap<>();
                room.put("roomId", row[0]);
                room.put("lastMessage", row[1]);
                room.put("lastSenderName", row[2]);
                room.put("lastSenderRole", row[3]);
                room.put("lastMessageTime", row[4]);
                room.put("unreadCount", row[5]);
                rooms.add(room);
            }
            
            return rooms;
        } finally {
            em.close();
        }
    }
}
