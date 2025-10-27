package ute.dao.inter;

import java.util.List;

import ute.entities.Banner;
import ute.entities.Categories;
import ute.entities.Product;

public interface BannerDao {
	List<Banner> findAll();

	List<Product> findByBannerId(int bannerId);

	List<Product> findProductsByBannerIdinPage(int bannerId, int page, int pageSize);

	// Thêm danh mục mới
	void insert(Banner banner);

	// Cập nhật danh mục
	void update(Banner banner);

	// Xóa danh mục theo id
	void delete(Long id);

	// Tìm danh mục theo id
	Banner findById(Long id);

	// ======== Các hàm mở rộng thường dùng ========

	// Tìm theo tên (gần đúng)
	List<Banner> findByName(String name);

	// Tìm theo tên chính xác
	Banner findByNameExact(String name);

	// Đếm tổng số danh mục
	long count();

	// Phân trang (page, pageSize)
	List<Banner> findPage(int page, int pageSize);

	// Phân trang với tìm kiếm theo tên
	List<Banner> findByNamePaginated(String name, int firstResult, int maxResults);

	// Đếm số danh mục với tìm kiếm theo tên
	long countByName(String name);

	void addProductToBanner(Long bannerId, Long productId);

	void removeProductFromBanner(Long bannerId, Long productId);

	void clearProductFromBanner(Long bannerId);

}
