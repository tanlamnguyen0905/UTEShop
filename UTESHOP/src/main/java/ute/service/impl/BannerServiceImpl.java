package ute.service.impl;

import java.util.List;

import ute.dao.impl.BannerDaoImpl;
import ute.entities.Banner;
import ute.entities.Product;

public class BannerServiceImpl implements ute.service.inter.BannerService {
	private static BannerDaoImpl instance= new BannerDaoImpl();
	@Override
	public List<Banner> findAll() {
		// TODO Auto-generated method stub
		return instance.findAll();
	}

	@Override
	public List<Product> findByBannerId(int bannerId) {
		// TODO Auto-generated method stub
		return instance.findByBannerId(bannerId);
	}

	@Override
	public List<Product> findProductsByBannerIdinPage(int bannerId, int page, int pageSize) {
		// TODO Auto-generated method stub
		return instance.findProductsByBannerIdinPage(bannerId, page, pageSize);
	}
	

}
