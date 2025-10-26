package ute.service.admin.inter;

import java.util.List;

import ute.entities.Brand;

public interface BrandService {
    // ======== CRUD cơ bản ========
    void insert(Brand brand);
    void update(Brand brand);
    void delete(Long id);  // FIX: Long
    Brand findById(Long id);  // FIX: Long
    List<Brand> findAll();

    // ======== Các hàm mở rộng thường dùng ========
    List<Brand> findByName(String name);
    List<Brand> findByNamePaginated(String name, int firstResult, int maxResults);
    long countByName(String name);
    long count();
    List<Brand> findPage(int page, int pageSize);
}