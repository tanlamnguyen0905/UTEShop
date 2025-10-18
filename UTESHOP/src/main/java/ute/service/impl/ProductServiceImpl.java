package ute.service.impl;

import java.util.List;

import ute.entities.Product;

public class ProductServiceImpl implements ute.service.inter.ProductService {

	private static ProductServiceImpl instance = new ProductServiceImpl();

	@Override
	public void insert(Product product) {
		// TODO Auto-generated method stub
		instance.insert(product);
	}

	@Override
	public void update(Product product) {
		// TODO Auto-generated method stub
		instance.update(product);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		instance.delete(id);
	}

	@Override
	public Product findById(int id) {
		// TODO Auto-generated method stub
		return instance.findById(id);
	}

	@Override
	public List<Product> findAll() {
		// TODO Auto-generated method stub
		return instance.findAll();
	}

	@Override
	public List<Product> findByName(String name) {
		// TODO Auto-generated method stub
		return instance.findByName(name);
	}

	@Override
	public List<Product> findByCategoryId(int categoryId) {
		// TODO Auto-generated method stub
		return instance.findByCategoryId(categoryId);
	}

	@Override
	public List<Product> findLatest(int limit) {
		// TODO Auto-generated method stub
		return instance.findLatest(limit);
	}

	@Override
	public List<Product> findBestSeller(int limit) {
		// TODO Auto-generated method stub
		return instance.findBestSeller(limit);
	}

	@Override
	public List<Product> findByPriceRange(double minPrice, double maxPrice) {
		// TODO Auto-generated method stub
		return instance.findByPriceRange(minPrice, maxPrice);
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return instance.count();
	}

	@Override
	public List<Product> findPage(int page, int pageSize) {
		// TODO Auto-generated method stub
		return instance.findPage(page, pageSize);
	}
	
}
