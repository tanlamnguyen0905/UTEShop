package ute.service.impl;

import java.util.List;

import ute.dao.impl.ProductDaoImpl;
import ute.entities.Product;
import ute.utils.ProductFilter;

public class ProductServiceImpl implements ute.service.inter.ProductService {

	private static ProductDaoImpl instance = new ProductDaoImpl();

	@Override
	public void insert(Product product) {
		instance.insert(product);
	}

	@Override
	public void update(Product product) {
		instance.update(product);
	}

	@Override
	public void delete(int id) {
		instance.delete(id);
	}

	@Override
	public Product findById(int id) {
		return instance.findById(id);
	}

	@Override
	public List<Product> findAll() {
		return instance.findAll();
	}

	@Override
	public List<Product> findByName(String name) {
		return instance.findByName(name);
	}

	@Override
	public List<Product> findByCategoryId(int categoryId) {
		return instance.findByCategoryId(categoryId);
	}

	@Override
	public List<Product> findLatest(int limit) {
		return instance.findLatest(limit);
	}

	@Override
	public List<Product> findBestSeller(int limit) {
		return instance.findBestSeller(limit);
	}

	@Override
	public List<Product> findByPriceRange(double minPrice, double maxPrice) {
		return instance.findByPriceRange(minPrice, maxPrice);
	}

	@Override
	public long count() {
		return instance.count();
	}

	@Override
	public List<Product> findPage(int page, int pageSize) {
		return instance.findPage(page, pageSize);
	}

	@Override
	public List<Product> findNewProduct(int limit) {
		return instance.findNewProduct(limit);
	}

	@Override
	public List<Product> findTopReview(int limit) {
		return instance.findTopReview(limit);
	}

	@Override
	public List<Product> findTopFavorite(int limit) {
		return instance.findTopFavorite(limit);
	}

	@Override
	public List<Product> findByCategoryIdinPage(int categoryId, int page, int pageSize) {
		return instance.findByCategoryIdinPage(categoryId, page, pageSize);
	}

	@Override
	public List<Product> findProductsByFilter(ProductFilter filter, int page, int pageSize) {
		return instance.findProductsByFilter(filter, page, pageSize);
	}

	@Override
	public List<Product> findTopFavoriteinPage(int page, int pageSize) {
		return instance.findTopFavoriteinPage(page, pageSize);
	}

	@Override
	public int countProductsByFilter(ProductFilter filter) {
		return instance.countProductsByFilter(filter);
	}
}
