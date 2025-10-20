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
    private static EntityManagerFactory emf = null;

    public static EntityManager getEntityManager() {
    	if (emf == null || !emf.isOpen()) 
    		createEntityManagerFactory();
        // Tạo và return EM
        return emf.createEntityManager();
    }
    
    private static void createEntityManagerFactory() {
    	try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("config.properties")) {
            Properties props = new Properties();
            props.load(is);
            
            // Tạo overrides cho DB connection
            Map<String, Object> overrides = new HashMap<>();
            String dbUrl = props.getProperty("db.url");
            String dbUser = props.getProperty("db.username");
            String dbPass = props.getProperty("db.password");

            overrides.put("jakarta.persistence.jdbc.url", dbUrl);
            overrides.put("jakarta.persistence.jdbc.user", dbUser);
            overrides.put("jakarta.persistence.jdbc.password", dbPass);

            // Tạo EMF với overrides
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, overrides);
    	} catch (Exception e) {
            System.err.println("Lỗi khi tạo EntityManager: " + e.getMessage());
            e.printStackTrace();
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
