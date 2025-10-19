package ute.dao.inter;

import java.util.List;

import ute.entities.Categories;

public interface CategoriesDao {
    // ======== CRUD cơ bản ========

    // Thêm danh mục mới
    void insert(Categories category);

    // Cập nhật danh mục
    void update(Categories category);

    // Xóa danh mục theo id
    void delete(Long id);

    // Tìm danh mục theo id
    Categories findById(Long id);

    // Lấy danh sách tất cả danh mục
    List<Categories> findAll();

    // ======== Các hàm mở rộng thường dùng ========

    // Tìm theo tên (gần đúng)
    List<Categories> findByName(String name);

    // Tìm theo tên chính xác
    Categories findByNameExact(String name);

    // Đếm tổng số danh mục
    long count();

    // Phân trang (page, pageSize)
    List<Categories> findPage(int page, int pageSize);

    // Phân trang với tìm kiếm theo tên
    List<Categories> findByNamePaginated(String name, int firstResult, int maxResults);

    // Đếm số danh mục với tìm kiếm theo tên
    long countByName(String name);
}