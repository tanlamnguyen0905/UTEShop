package ute.service.impl;

import java.util.List;

import ute.entities.Banner;
import ute.entities.Product;

public class BannerServiceImpl implements ute.service.inter.BannerService {
	private static BannerServiceImpl instance;
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
	

}
