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
		instance.insert(product);
	}

	@Override
	public void update(Product product) {
		instance.update(product);
	}

	@Override
	public void delete(Long id) {
		// delegate to dao which accepts int in some places - convert if necessary
		instance.delete(id.intValue());
	}

	@Override
	public Product findById(Long id) {
		return instance.findById(id.intValue());
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
