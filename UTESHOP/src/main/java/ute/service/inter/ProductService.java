package ute.service.inter;

import java.util.List;

import ute.dto.ProductDTO;
import ute.entities.Product;
import ute.utils.ProductFilter;
import ute.utils.ProductPage;

public interface ProductService {

    // ======== CRUD cơ bản ========

    // Thêm sản phẩm mới
    void insert(Product product);

    // Cập nhật sản phẩm
    void update(Product product);

    // Xóa sản phẩm theo id
    void delete(Long id);

    // Tìm sản phẩm theo id
    Product findById(Long id);

    // Lấy danh sách tất cả sản phẩm
    List<Product> findAll();

    // ======== Các hàm mở rộng thường dùng ========

    List<Product> findByName(String name);

    List<Product> findByCategoryId(int categoryId);

    List<Product> findByCategoryIdinPage(int categoryId, int page, int pageSize);

    List<Product> findBestSeller(int limit);

    List<Product> findByPriceRange(double minPrice, double maxPrice);

    long count();

    List<Product> findPage(int page, int pageSize);

    List<Product> findNewProduct(int limit);

    List<Product> findTopReview(int limit);

    List<Product> findTopFavorite(int limit);

    List<Product> findProductsByFilter(ProductFilter filter, int page, int pageSize);

    List<Product> findTopFavoriteinPage(int page, int pageSize);

    int countProductsByFilter(ProductFilter filter);

    ProductPage getProductsPageByFilter(ProductFilter filter);

    List<ProductDTO> MapToProductDTO(List<Product> products);

}
