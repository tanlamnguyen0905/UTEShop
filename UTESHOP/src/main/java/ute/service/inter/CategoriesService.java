package ute.service.inter;

import java.util.List;

import ute.entities.Categories;

public interface CategoriesService {
    List<Categories> findAll();
    Categories findById(Long id);
    void save(Categories category);
    void delete(Long categoryID);
    void update(Categories category);

    // Các phương thức mở rộng
    List<Categories> findByName(String name);
    Categories findByNameExact(String name);
    long count();
    List<Categories> findPage(int page, int pageSize);

    // Phân trang với tìm kiếm theo tên
    List<Categories> findByNamePaginated(String name, int firstResult, int maxResults);
    long countByName(String name);
}