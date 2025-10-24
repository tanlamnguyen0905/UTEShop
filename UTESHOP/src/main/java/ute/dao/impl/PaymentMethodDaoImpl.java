package ute.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.entities.PaymentMethod;

public class PaymentMethodDaoImpl {
    
    public List<PaymentMethod> findAll() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<PaymentMethod> query = em.createQuery(
                "SELECT pm FROM PaymentMethod pm", 
                PaymentMethod.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public PaymentMethod findById(Long id) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.find(PaymentMethod.class, id);
        } finally {
            em.close();
        }
    }
}

