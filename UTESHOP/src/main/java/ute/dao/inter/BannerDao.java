package ute.dao.inter;

import java.util.List;

import ute.entities.Banner;
import ute.entities.Product;

public interface BannerDao {
	List<Banner> findAll();
	List<Product> findByBannerId(int bannerId);
}
