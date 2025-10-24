package ute.configs;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@PersistenceContext
public class JPAConfig {
    private static final String PERSISTENCE_UNIT_NAME = "UTESHOP";
    private static EntityManagerFactory emf;

    public static EntityManager getEntityManager() {
        try {
            Properties props = new Properties();
            try (InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("config.properties")) {
                if (is == null) {
                    throw new RuntimeException("Không tìm thấy file config.properties");
                }
                props.load(is);
            }

            // Tạo overrides cho DB connection
            Map<String, Object> overrides = new HashMap<>();
            String dbUrl = props.getProperty("db.url");
            String dbUser = props.getProperty("db.username");
            String dbPass = props.getProperty("db.password");

            if (dbUrl == null || dbUser == null || dbPass == null) {
                throw new RuntimeException("Thiếu thông tin kết nối database trong config.properties");
            }

            overrides.put("jakarta.persistence.jdbc.url", dbUrl);
            overrides.put("jakarta.persistence.jdbc.user", dbUser);
            overrides.put("jakarta.persistence.jdbc.password", dbPass);

            // Tạo EMF nếu chưa có (với overrides)
            if (emf == null || !emf.isOpen()) {
                System.out.println("Đang tạo EntityManagerFactory...");
                System.out.println("Database URL: " + dbUrl);
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, overrides);
                System.out.println("EntityManagerFactory đã được tạo thành công!");
            }

            // Tạo và return EM
            EntityManager em = emf.createEntityManager();
            if (em == null) {
                throw new RuntimeException("Không thể tạo EntityManager từ EntityManagerFactory");
            }
            return em;

        } catch (Exception e) {
            System.err.println("========================================");
            System.err.println("LỖI KHI TẠO ENTITYMANAGER");
            System.err.println("========================================");
            System.err.println("Chi tiết lỗi: " + e.getMessage());
            e.printStackTrace();
            System.err.println("========================================");
            System.err.println("Vui lòng kiểm tra:");
            System.err.println("1. SQL Server đã chạy chưa?");
            System.err.println("2. Database UTESHOP đã tồn tại chưa?");
            System.err.println("3. Username/password trong config.properties đúng chưa?");
            System.err.println("4. File persistence.xml có lỗi cú pháp không?");
            System.err.println("========================================");
            throw new RuntimeException("Lỗi khởi tạo EntityManager: " + e.getMessage(), e);
        }
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        EntityManager em = getEntityManager();
        if (em != null) {
            em.close();
        }
        return emf;
    }

    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            emf = null;
        }
    }

    public static void main(String[] args) {
        EntityManager em = getEntityManager();
        if (em == null) {
            System.out.println("EntityManager is null");
        } else {
            System.out.println("EntityManager created successfully: " + em);
            em.close();
        }
        closeEntityManagerFactory();
    }
}