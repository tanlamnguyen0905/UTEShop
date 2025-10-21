package ute.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ute.dao.impl.ProductDaoImpl;
import ute.dto.ProductDTO;
import ute.entities.Product;
import ute.utils.ProductFilter;
import ute.utils.ProductPage;

public class ProductServiceImpl implements ute.service.inter.ProductService {

	private static ProductDaoImpl instance = new ProductDaoImpl();

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

	@Override
	public List<Product> findNewProduct(int limit) {
		// TODO Auto-generated method stub
		return instance.findNewProduct(limit);
	}

	@Override
	public List<Product> findTopReview(int limit) {
		// TODO Auto-generated method stub
		return instance.findTopReview(limit);
	}

	@Override
	public List<Product> findTopFavorite(int limit) {
		// TODO Auto-generated method stub
		return instance.findTopFavorite(limit);
	}

	@Override
	public List<Product> findByCategoryIdinPage(int categoryId, int page, int pageSize) {
		// TODO Auto-generated method stub
		return instance.findByCategoryIdinPage(categoryId, page, pageSize);
	}

	@Override
	public List<Product> findProductsByFilter(ProductFilter filter, int page, int pageSize) {
		// TODO Auto-generated method stub
		return instance.findProductsByFilter(filter, page, pageSize);
	}

	@Override
	public List<Product> findTopFavoriteinPage(int page, int pageSize) {
		// TODO Auto-generated method stub
		return instance.findTopFavoriteinPage(page, pageSize);
	}

	@Override
	public int countProductsByFilter(ProductFilter filter) {
		// TODO Auto-generated method stub
		return instance.countProductsByFilter(filter);
	}

	@Override
	public ProductPage getProductsPageByFilter(ProductFilter filter) {
		int page = filter.getCurrentPage() != null ? filter.getCurrentPage() : 1;
		int pageSize = filter.getPageSize() != null ? filter.getPageSize() : 20;
		List<Product> products = instance.findProductsByFilter(filter, page, pageSize);
		int total = instance.countProductsByFilter(filter);
		int totalPages = (int) Math.ceil((double) total / pageSize);
		return ProductPage.builder()
				.products(products)
				.total(total)
				.totalPages(totalPages)
				.currentPage(page)
				.pageSize(pageSize)
				.build();
	}

	@Override
	public List<ProductDTO> MapToProductDTO(List<Product> products) {
		if (products == null) {
			return null;
		}
		return products.stream()
				.filter(Objects::nonNull)
				.map(ProductDTO::fromEntity)
				.collect(Collectors.toList());
	}
}
