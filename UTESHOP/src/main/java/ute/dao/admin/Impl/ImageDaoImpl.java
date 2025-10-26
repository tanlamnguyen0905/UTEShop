package ute.dao.admin.Impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import ute.configs.JPAConfig;
import ute.dao.admin.inter.ImageDao;
import ute.entities.Brand;
import ute.entities.Image;

public class ImageDaoImpl implements ImageDao {
    @Override
    public Image findImageById(Long id) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            return em.find(Image.class, (long) id);
        } finally {
            em.close();
        }
    }

    @Override
    public void deleteImage(Image image) {
        if (image == null || image.getImageID() == null) {
            System.err.println("Image hoặc ID null, bỏ qua xóa.");
            return;
        }
        Long imageId = image.getImageID();
        EntityManager em = JPAConfig.getEntityManager();
        var trans = em.getTransaction();
        try {
            trans.begin();
            // SỬA: Dùng JPQL DELETE theo ID (an toàn, không cần entity managed)
            String jpql = "DELETE FROM Image i WHERE i.imageID = :imageId";
            Query query = em.createQuery(jpql);
            query.setParameter("imageId", imageId);
            int deletedRows = query.executeUpdate();  // Thực thi và trả số row xóa
            em.flush();  // Force SQL execute ngay
            trans.commit();
            System.out.println("Đã xóa thành công " + deletedRows + " ảnh với ID: " + imageId);  // Log trace
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
                System.err.println("Lỗi xóa ảnh ID " + imageId + ": " + e.getMessage() + " | Stack: " + e.getStackTrace()[0]);  // Log chi tiết
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
