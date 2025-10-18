package ute.service.inter;

import java.util.List;

import ute.entities.Product;

public interface ProductService {

    // ======== CRUD cơ bản ========

    // Thêm sản phẩm mới
    void insert(Product product);

    // Cập nhật sản phẩm
    void update(Product product);

    // Xóa sản phẩm theo id
    void delete(int id);

    // Tìm sản phẩm theo id
    Product findById(int id);

    // Lấy danh sách tất cả sản phẩm
    List<Product> findAll();

    // ======== Các hàm mở rộng thường dùng ========

    // Tìm theo tên (gần đúng)
    List<Product> findByName(String name);

    // Tìm theo danh mục (category)
    List<Product> findByCategoryId(int categoryId);
    

    // Lấy các sản phẩm mới nhất (giới hạn n)
    List<Product> findLatest(int limit);

    // Lấy các sản phẩm bán chạy nhất
    List<Product> findBestSeller(int limit);

    // Lấy các sản phẩm có giá trong khoảng
    List<Product> findByPriceRange(double minPrice, double maxPrice);

    // Đếm tổng số sản phẩm
    long count();

    // Phân trang (page, pageSize)
    List<Product> findPage(int page, int pageSize);
    
    
}
