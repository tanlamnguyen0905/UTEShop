package ute.dao.admin.inter;

import java.util.List;

import ute.entities.Brand;

public interface BrandDao {
    // ======== CRUD cơ bản ========
    void insert(Brand brand);
    void update(Brand brand);
    void delete(int id);
    Brand findById(int id);
    List<Brand> findAll();

    // ======== Các hàm mở rộng thường dùng ========
    List<Brand> findByName(String name);
    List<Brand> findByNamePaginated(String name, int firstResult, int maxResults);
    long countByName(String name);
    long count();
    List<Brand> findPage(int page, int pageSize);
}