package ute.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ute.configs.JPAConfig;
import ute.dao.inter.ImageDao;
import ute.entities.Image;

public class ImageDaoImpl implements ImageDao {

    @Override
    public void insert(Image image) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();

            // Đảm bảo image có product hợp lệ (đã được quản lý hoặc có ID)
            if (image.getProduct() == null) {
                throw new IllegalArgumentException("Image không có liên kết product!");
            }

            em.persist(image); // Lưu đối tượng image vào DB
            trans.commit();
            System.out.println("✅ Đã lưu image vào DB: " + image.getDirImage());
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Image image) {
        EntityManager em = JPAConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(image); // Cập nhật thông tin ảnh (nếu cần)
            trans.commit();
            System.out.println("🔄 Đã cập nhật image ID: " + image.getImageID());
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }
}
