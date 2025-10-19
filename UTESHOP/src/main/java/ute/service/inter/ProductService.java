package ute.service.inter;

import ute.entities.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAll();
    Product findById(Long id);
    void save(Product product);
    void delete(Long productID);
    void update(Product product);

    // Các phương thức mở rộng
    List<Product> findByName(String name);
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findLatest(int limit);
    List<Product> findBestSeller(int limit);
    List<Product> findByPriceRange(double minPrice, double maxPrice);
    long count();
    List<Product> findPage(int page, int pageSize);

    // Phân trang với tìm kiếm theo tên
    List<Product> findByNamePaginated(String name, int firstResult, int maxResults);
    long countByName(String name);
}