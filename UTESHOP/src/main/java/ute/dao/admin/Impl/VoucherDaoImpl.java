package ute.dao.admin.Impl;

import ute.configs.JPAConfig;
import ute.dao.AbstractDao;
import ute.dao.admin.inter.VoucherDao;
import ute.entities.Voucher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class VoucherDaoImpl extends AbstractDao<Voucher> implements VoucherDao {
    public VoucherDaoImpl() {super(Voucher.class);}

    private EntityManager getEntityManager() {
        return JPAConfig.getEntityManager();
    }

    @Override
    public void insert(Voucher voucher) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(voucher);
            em.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Lỗi insert Voucher: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void update(Voucher voucher) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(voucher);
            em.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Lỗi update Voucher: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Voucher voucher) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.merge(voucher));
            em.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Lỗi delete Voucher: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Voucher voucher = em.find(Voucher.class, id);
            if (voucher != null) {
                em.remove(voucher);
                em.flush();
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Lỗi delete Voucher: " + e.getMessage(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Voucher findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Voucher.class, id);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Voucher> findByCodeVoucherPaginated(String codeVoucher, int firstResult, int maxResults) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT v FROM Voucher v WHERE LOWER(v.codeVoucher) LIKE LOWER(:codeVoucher) ORDER BY v.voucherID";
            TypedQuery<Voucher> query = em.createQuery(jpql, Voucher.class);
            query.setParameter("codeVoucher", "%" + codeVoucher + "%");
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public long countByCodeVoucher(String codeVoucher) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT COUNT(v) FROM Voucher v WHERE LOWER(v.codeVoucher) LIKE LOWER(:codeVoucher)";
            TypedQuery<Long> query = em.createQuery(jpql, Long.class);
            query.setParameter("codeVoucher", "%" + codeVoucher + "%");
            return query.getSingleResult();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<Voucher> findPage(int page, int size) {
        EntityManager em = getEntityManager();
        try {
            int firstResult = (page - 1) * size;
            String jpql = "SELECT v FROM Voucher v ORDER BY v.voucherID";
            TypedQuery<Voucher> query = em.createQuery(jpql, Voucher.class);
            query.setFirstResult(firstResult);
            query.setMaxResults(size);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public long count() {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Voucher> root = cq.from(Voucher.class);
            cq.select(cb.count(root));
            return em.createQuery(cq).getSingleResult();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Voucher findByCodeVoucherExact(String codeVoucher) {
        EntityManager em = getEntityManager();
        try {
            String jpql = "SELECT v FROM Voucher v WHERE LOWER(v.codeVoucher) = LOWER(:codeVoucher)";
            TypedQuery<Voucher> query = em.createQuery(jpql, Voucher.class);
            query.setParameter("codeVoucher", codeVoucher);
            List<Voucher> results = query.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}