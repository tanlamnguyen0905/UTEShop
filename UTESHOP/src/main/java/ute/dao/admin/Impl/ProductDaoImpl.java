package ute.dao.admin.Impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import ute.configs.JPAConfig;
import ute.dao.admin.inter.ProductDao;
import ute.entities.Product;
import ute.utils.ProductFilter;

public class ProductDaoImpl implements ProductDao {

    private static final String BASE_FETCH = "SELECT p FROM Product p " +
            "LEFT JOIN FETCH p.images " +
            "LEFT JOIN FETCH p.reviews " +
            "LEFT JOIN FETCH p.favorites " ;

    // Thêm constant mới cho query đơn giản (không fetch collection)
    private static final String BASE_QUERY_SIMPLE = "SELECT p FROM Product p ";

    @Override
    public void insert(Product product) {
        EntityManager em = JPAConfig.getEntityManager();
        var trans = em.getTransaction();
        try {
            trans.begin();
            em.persist(product);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Product product) {
        EntityManager em = JPAConfig.getEntityManager();
        var trans = em.getTransaction();
        try {
            trans.begin();
            em.merge(product);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        var trans = em.getTransaction();
        try {
            trans.begin();
            Product p = em.find(Product.class, (long) id);
            if (p != null) {
                em.remove(p);
            }
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Product findById(int id) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            // Use em.find to avoid multiple JOIN FETCH issues; collections will be lazy-loaded
            Product product = em.find(Product.class, (long) id);
            if (product == null) {
                return null;
            }
            // Manually trigger transients if needed (lazy load collections)
            if (product.getReviews() != null) {
                product.getReviews().size(); // Triggers lazy load for rating
            }
            if (product.getFavorites() != null) {
                product.getFavorites().size(); // Triggers lazy load for favoriteCount
            }
            return product;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findAll() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(BASE_QUERY_SIMPLE, Product.class);
            List<Product> products = query.getResultList();
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findByName(String name) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(BASE_QUERY_SIMPLE + "WHERE LOWER(p.productName) LIKE LOWER(:name)", Product.class);
            query.setParameter("name", "%" + name + "%");
            List<Product> products = query.getResultList();
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findByCategoryId(int categoryId) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(BASE_FETCH + "WHERE p.category.categoryID = :id", Product.class);
            query.setParameter("id", (long) categoryId);
            List<Product> products = query.getResultList();
            products.forEach(p -> {
                if (p.getReviews() != null) p.getReviews().size();
                if (p.getFavorites() != null) p.getFavorites().size();
            });
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findByCategoryIdinPage(int categoryId, int page, int pageSize) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(BASE_FETCH + "WHERE p.category.categoryID = :id ORDER BY p.productID DESC", Product.class);
            query.setParameter("id", (long) categoryId);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            List<Product> products = query.getResultList();
            products.forEach(p -> {
                if (p.getReviews() != null) p.getReviews().size();
                if (p.getFavorites() != null) p.getFavorites().size();
            });
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findBestSeller(int limit) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(BASE_FETCH + "ORDER BY p.soldCount DESC", Product.class);
            query.setMaxResults(limit);
            List<Product> products = query.getResultList();
            products.forEach(p -> {
                if (p.getReviews() != null) p.getReviews().size();
                if (p.getFavorites() != null) p.getFavorites().size();
            });
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findBestSellerinPage(int limit, int page, int pageSize) {
        // Assuming limit is ignored or used as total, but paginated
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(BASE_FETCH + "ORDER BY p.soldCount DESC", Product.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            List<Product> products = query.getResultList();
            products.forEach(p -> {
                if (p.getReviews() != null) p.getReviews().size();
                if (p.getFavorites() != null) p.getFavorites().size();
            });
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findByPriceRange(double minPrice, double maxPrice) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(BASE_FETCH + "WHERE p.unitPrice BETWEEN :minPrice AND :maxPrice", Product.class);
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
            List<Product> products = query.getResultList();
            products.forEach(p -> {
                if (p.getReviews() != null) p.getReviews().size();
                if (p.getFavorites() != null) p.getFavorites().size();
            });
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findByPriceRangeinPage(double minPrice, double maxPrice, int page, int pageSize) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(BASE_FETCH + "WHERE p.unitPrice BETWEEN :minPrice AND :maxPrice ORDER BY p.productID DESC", Product.class);
            query.setParameter("minPrice", minPrice);
            query.setParameter("maxPrice", maxPrice);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            List<Product> products = query.getResultList();
            products.forEach(p -> {
                if (p.getReviews() != null) p.getReviews().size();
                if (p.getFavorites() != null) p.getFavorites().size();
            });
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public long count() {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(p) FROM Product p", Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findPage(int page, int pageSize) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(BASE_QUERY_SIMPLE + "ORDER BY p.productID DESC", Product.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            List<Product> products = query.getResultList();
            // Không cần trigger transients ở đây vì không fetch collection
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findNewProduct(int limit) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(BASE_FETCH + "ORDER BY p.importDate DESC", Product.class);
            query.setMaxResults(limit);
            List<Product> products = query.getResultList();
            products.forEach(p -> {
                if (p.getReviews() != null) p.getReviews().size();
                if (p.getFavorites() != null) p.getFavorites().size();
            });
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findTopReview(int limit) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = BASE_FETCH + "LEFT JOIN p.reviews r GROUP BY p ORDER BY AVG(r.rating) DESC, COUNT(r) DESC";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setMaxResults(limit);
            List<Product> products = query.getResultList();
            products.forEach(p -> {
                if (p.getReviews() != null) p.getReviews().size();
                if (p.getFavorites() != null) p.getFavorites().size();
            });
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findTopFavorite(int limit) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = BASE_FETCH + "LEFT JOIN p.favorites f GROUP BY p ORDER BY COUNT(f) DESC";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setMaxResults(limit);
            List<Product> products = query.getResultList();
            products.forEach(p -> {
                if (p.getReviews() != null) p.getReviews().size();
                if (p.getFavorites() != null) p.getFavorites().size();
            });
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findTopFavoriteinPage(int page, int pageSize) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            String jpql = BASE_FETCH + "LEFT JOIN p.favorites f GROUP BY p ORDER BY COUNT(f) DESC";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
            List<Product> products = query.getResultList();
            products.forEach(p -> {
                if (p.getReviews() != null) p.getReviews().size();
                if (p.getFavorites() != null) p.getFavorites().size();
            });
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findProductsByFilter(ProductFilter filter, int page, int pageSize) {
        StringBuilder jpql = new StringBuilder(BASE_FETCH + "LEFT JOIN p.orderDetails od LEFT JOIN p.favorites f LEFT JOIN p.banners b WHERE 1=1");

        if (filter.getCategoryId() != null) jpql.append(" AND p.category.categoryID = :categoryId");
        if (filter.getBrandId() != null) jpql.append(" AND p.brand.brandID = :brandId");
        if (filter.getBannerId() != null) jpql.append(" AND b.bannerID = :bannerId");
        if (filter.getMinPrice() != null) jpql.append(" AND p.unitPrice >= :minPrice");
        if (filter.getMaxPrice() != null) jpql.append(" AND p.unitPrice <= :maxPrice");
        if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
            jpql.append(" AND LOWER(p.productName) LIKE :keyword");
        }

        String sortBy = filter.getSortBy() != null ? filter.getSortBy() : "0";
        switch (sortBy) {
            case "1": jpql.append(" ORDER BY p.soldCount DESC"); break;
            case "2": jpql.append(" LEFT JOIN p.reviews r GROUP BY p ORDER BY AVG(r.rating) DESC, COUNT(r) DESC"); break;
            case "3": jpql.append(" LEFT JOIN p.favorites f GROUP BY p ORDER BY COUNT(f) DESC"); break;
            case "4": jpql.append(" ORDER BY p.unitPrice ASC"); break;
            case "5": jpql.append(" ORDER BY p.unitPrice DESC"); break;
            default: jpql.append(" ORDER BY p.productID DESC");
        }

        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(jpql.toString(), Product.class);
            // Set parameters
            if (filter.getCategoryId() != null) query.setParameter("categoryId", filter.getCategoryId());
            if (filter.getBrandId() != null) query.setParameter("brandId", filter.getBrandId());
            if (filter.getBannerId() != null) query.setParameter("bannerId", filter.getBannerId());
            if (filter.getMinPrice() != null) query.setParameter("minPrice", filter.getMinPrice());
            if (filter.getMaxPrice() != null) query.setParameter("maxPrice", filter.getMaxPrice());
            if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
                query.setParameter("keyword", "%" + filter.getKeyword().toLowerCase() + "%");
            }
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            List<Product> products = query.getResultList();
            products.forEach(p -> {
                if (p.getReviews() != null) p.getReviews().size();
                if (p.getFavorites() != null) p.getFavorites().size();
            });
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public int countProductsByFilter(ProductFilter filter) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(DISTINCT p) FROM Product p LEFT JOIN p.orderDetails od LEFT JOIN p.favorites f LEFT JOIN p.banners b WHERE 1=1");

        if (filter.getCategoryId() != null) jpql.append(" AND p.category.categoryID = :categoryId");
        if (filter.getBrandId() != null) jpql.append(" AND p.brand.brandID = :brandId");
        if (filter.getBannerId() != null) jpql.append(" AND b.bannerID = :bannerId");
        if (filter.getMinPrice() != null) jpql.append(" AND p.unitPrice >= :minPrice");
        if (filter.getMaxPrice() != null) jpql.append(" AND p.unitPrice <= :maxPrice");
        if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
            jpql.append(" AND LOWER(p.productName) LIKE :keyword");
        }

        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);
            // Set parameters (tương tự findProductsByFilter)
            if (filter.getCategoryId() != null) query.setParameter("categoryId", filter.getCategoryId());
            if (filter.getBrandId() != null) query.setParameter("brandId", filter.getBrandId());
            if (filter.getBannerId() != null) query.setParameter("bannerId", filter.getBannerId());
            if (filter.getMinPrice() != null) query.setParameter("minPrice", filter.getMinPrice());
            if (filter.getMaxPrice() != null) query.setParameter("maxPrice", filter.getMaxPrice());
            if (filter.getKeyword() != null && !filter.getKeyword().isEmpty()) {
                query.setParameter("keyword", "%" + filter.getKeyword().toLowerCase() + "%");
            }
            return query.getSingleResult().intValue();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Product> findByNamePaginated(String name, int firstResult, int maxResults) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Product> query = em.createQuery(BASE_QUERY_SIMPLE + "WHERE LOWER(p.productName) LIKE LOWER(:name) ORDER BY p.productID DESC", Product.class);
            query.setParameter("name", "%" + name + "%");
            query.setFirstResult(firstResult);
            query.setMaxResults(maxResults);
            List<Product> products = query.getResultList();
            // Không cần trigger transients ở đây vì không fetch collection
            return products;
        } finally {
            em.close();
        }
    }

    @Override
    public long countByName(String name) {
        EntityManager em = JPAConfig.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(p) FROM Product p WHERE LOWER(p.productName) LIKE LOWER(:name)", Long.class);
            query.setParameter("name", "%" + name + "%");
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}