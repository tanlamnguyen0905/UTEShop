package ute.service.inter;

import java.util.List;

import ute.entities.Banner;
import ute.entities.Product;

public interface BannerService {

	List<Banner> findAll();
	List<Product> findByBannerId(int bannerId);
}
