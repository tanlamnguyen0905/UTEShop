package ute.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import ute.dao.impl.ProductDaoImpl;
import ute.dto.ProductDTO;
import ute.entities.Product;
import ute.utils.ProductFilter;
import ute.utils.ProductPage;
import ute.dao.impl.OrderDetailDaoImpl;
import ute.dao.inter.OrderDetailDao;
import ute.dao.inter.ProductDao;
import ute.service.inter.ProductService;

public class ProductServiceImpl implements ProductService {
    
    private final ProductDao productDao;
    private final OrderDetailDao orderDetailDao;
    
    public ProductServiceImpl() {
        this.productDao = new ProductDaoImpl();
        this.orderDetailDao = new OrderDetailDaoImpl();
    }

	@Override
	public void insert(Product product) {
		productDao.insert(product);
	}

	@Override
	public void update(Product product) {
		productDao.update(product);
	}

	@Override
	public void delete(Long id) {
		productDao.delete(id.intValue());
	}

	@Override
	public Product findById(Long id) {
		return productDao.findById(id.intValue());
	}

	@Override
	public List<Product> findAll() {
		return productDao.findAll();
	}

	@Override
	public List<Product> findByName(String name) {
		return productDao.findByName(name);
	}

	@Override
	public List<Product> findByCategoryId(int categoryId) {
		return productDao.findByCategoryId(categoryId);
	}

	@Override
	public List<Product> findBestSeller(int limit) {
		return productDao.findBestSeller(limit);
	}

	@Override
	public List<Product> findByPriceRange(double minPrice, double maxPrice) {
		return productDao.findByPriceRange(minPrice, maxPrice);
	}

	@Override
	public long count() {
		return productDao.count();
	}

	@Override
	public List<Product> findPage(int page, int pageSize) {
		return productDao.findPage(page, pageSize);
	}

	@Override
	public List<Product> findNewProduct(int limit) {
		return productDao.findNewProduct(limit);
	}

	@Override
	public List<Product> findTopReview(int limit) {
		return productDao.findTopReview(limit);
	}

	@Override
	public List<Product> findTopFavorite(int limit) {
		return productDao.findTopFavorite(limit);
	}

	@Override
	public List<Product> findByCategoryIdinPage(int categoryId, int page, int pageSize) {
		return productDao.findByCategoryIdinPage(categoryId, page, pageSize);
	}

	@Override
	public List<Product> findProductsByFilter(ProductFilter filter, int page, int pageSize) {
		return productDao.findProductsByFilter(filter, page, pageSize);
	}

	@Override
	public List<Product> findTopFavoriteinPage(int page, int pageSize) {
		return productDao.findTopFavoriteinPage(page, pageSize);
	}

	@Override
	public int countProductsByFilter(ProductFilter filter) {
		return productDao.countProductsByFilter(filter);
	}

	@Override
	public ProductPage getProductsPageByFilter(ProductFilter filter) {
		int page = filter.getCurrentPage() != null ? filter.getCurrentPage() : 1;
		int pageSize = filter.getPageSize() != null ? filter.getPageSize() : 20;
		List<Product> products = productDao.findProductsByFilter(filter, page, pageSize);
		int total = productDao.countProductsByFilter(filter);
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

    @Override
    public Long getTotalProductCount() {
        return productDao.count();
    }

    @Override
    public Long getActiveProductCount() {
        return productDao.getActiveProductCount();
    }

    @Override
    public List<Map<String, Object>> getTopProductsByQuantity(int limit) {
        return orderDetailDao.getTopProductsByQuantity(limit);
    }

    @Override
    public List<Map<String, Object>> getTopProductsByRevenue(int limit) {
        return orderDetailDao.getTopProductsByRevenue(limit);
    }

    @Override
    public List<Map<String, Object>> getSalesByCategory() {
        return orderDetailDao.getSalesByCategory();
    }
}