package ute.dao.admin.Impl;

import jakarta.persistence.EntityManager;
import ute.configs.JPAConfig;
import ute.dao.admin.inter.UserDao;
import ute.entities.Users;

public class UserDaoImpl implements UserDao {


    @Override
    public Users findUserById(long userId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.find(Users.class, (long) userId);
        } finally {
            em.close();
        }
    }
}
