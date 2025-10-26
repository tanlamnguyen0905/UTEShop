package ute.service.inter;

import java.util.List;

import ute.dto.BannerDTO;
import ute.dto.ProductDTO;
import ute.entities.Banner;
import ute.entities.Banner;
import ute.entities.Product;

public interface BannerService {

	List<Banner> findAll();

	List<Product> findByBannerId(int bannerId);

	List<Product> findProductsByBannerIdinPage(int bannerId, int page, int pageSize);

	Banner findById(Long id);

	void save(Banner category);

	void delete(Long categoryID);

	void update(Banner category);

	// Các phương thức mở rộng
	List<Banner> findByName(String name);

	Banner findByNameExact(String name);

	long count();

	List<Banner> findPage(int page, int pageSize);

	// Phân trang với tìm kiếm theo tên
	List<Banner> findByNamePaginated(String name, int firstResult, int maxResults);

	long countByName(String name);

	void addProductToBanner(Long bannerId, Long productId);

	void removeProductFromBanner(Long bannerId, Long productId);

	void clearProductFromBanner(Long bannerId);

	List<BannerDTO> MapToBannerDTO(List<Banner> banners);
}
