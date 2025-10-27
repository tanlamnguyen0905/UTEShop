package ute.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ute.dao.impl.BannerDaoImpl;
import ute.dto.BannerDTO;
import ute.dto.ProductDTO;
import ute.entities.Banner;
import ute.entities.Product;

public class BannerServiceImpl implements ute.service.inter.BannerService {
	private static BannerDaoImpl instance = new BannerDaoImpl();

	@Override
	public List<Banner> findAll() {
		return instance.findAll();
	}

	@Override
	public List<Product> findByBannerId(int bannerId) {
		return instance.findByBannerId(bannerId);
	}

	@Override
	public List<Product> findProductsByBannerIdinPage(int bannerId, int page, int pageSize) {
		return instance.findProductsByBannerIdinPage(bannerId, page, pageSize);
	}

	@Override
	public Banner findById(Long id) {
		// TODO Auto-generated method stub
		return instance.findById(id);
	}

	@Override
	public void save(Banner banner) {
		instance.insert(banner);
	}

	@Override
	public void delete(Long bannerID) {
		// TODO Auto-generated method stub
		instance.delete(bannerID);
	}

	@Override
	public void update(Banner banner) {
		// TODO Auto-generated method stub
		instance.update(banner);
	}

	@Override
	public List<Banner> findByName(String name) {
		// TODO Auto-generated method stub
		return instance.findByName(name);
	}

	@Override
	public Banner findByNameExact(String name) {
		// TODO Auto-generated method stub
		return instance.findByNameExact(name);
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return instance.count();
	}

	@Override
	public List<Banner> findPage(int page, int pageSize) {
		// TODO Auto-generated method stub
		return instance.findPage(page, pageSize);
	}

	@Override
	public List<Banner> findByNamePaginated(String name, int firstResult, int maxResults) {
		// TODO Auto-generated method stub
		return instance.findByNamePaginated(name, firstResult, maxResults);
	}

	@Override
	public long countByName(String name) {
		// TODO Auto-generated method stub
		return instance.countByName(name);
	}

	@Override
	public void addProductToBanner(Long bannerId, Long productId) {
		// TODO Auto-generated method stub
		instance.addProductToBanner(bannerId, productId);
	}

	@Override
	public void removeProductFromBanner(Long bannerId, Long productId) {
		// TODO Auto-generated method stub
		instance.removeProductFromBanner(bannerId, productId);
	}

	@Override
	public void clearProductFromBanner(Long bannerId) {
		// TODO Auto-generated method stub
		instance.clearProductFromBanner(bannerId);
	}

	@Override
	public List<BannerDTO> MapToBannerDTO(List<Banner> banners) {
		// TODO Auto-generated method stub
		if (banners == null) {
			return null;
		}
		return banners.stream()
				.filter(Objects::nonNull)
				.map(BannerDTO::fromEntity)
				.collect(Collectors.toList());
	}

}
