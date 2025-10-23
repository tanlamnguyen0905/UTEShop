package ute.dao.admin.inter;

import java.util.List;

import ute.entities.Product;
import ute.utils.ProductFilter;

public interface ProductDao {

    // ======== CRUD cơ bản ========
    void insert(Product product);
    void update(Product product);
    void delete(int id);
    Product findById(int id);
    List<Product> findAll();

    // ======== Các hàm mở rộng thường dùng ========
    List<Product> findByName(String name);
    List<Product> findByCategoryId(int categoryId);
    List<Product> findByCategoryIdinPage(int categoryId, int page, int pageSize);
    List<Product> findBestSeller(int limit);
    List<Product> findBestSellerinPage(int limit, int page, int pageSize);
    List<Product> findByPriceRange(double minPrice, double maxPrice);
    List<Product> findByPriceRangeinPage(double minPrice, double maxPrice, int page, int pageSize);
    long count();
    List<Product> findPage(int page, int pageSize);
    List<Product> findNewProduct(int limit);
    List<Product> findTopReview(int limit);
    List<Product> findTopFavorite(int limit);
    List<Product> findTopFavoriteinPage(int page, int pageSize);
    List<Product> findProductsByFilter(ProductFilter filter, int page, int pageSize);
    int countProductsByFilter(ProductFilter filter);

    // Pagination by name (thêm từ trước)
    List<Product> findByNamePaginated(String name, int firstResult, int maxResults);
    long countByName(String name);
}