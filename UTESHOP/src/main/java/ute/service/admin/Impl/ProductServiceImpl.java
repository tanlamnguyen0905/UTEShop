package ute.service.admin.Impl;

import java.util.List;

import ute.dao.admin.Impl.ProductDaoImpl;
import ute.dao.admin.inter.ProductDao;
import ute.dto.ProductDTO;
import ute.entities.Product;
import ute.service.admin.inter.ProductService;
import ute.utils.ProductFilter;
import ute.utils.ProductPage;

public class ProductServiceImpl implements ProductService {
    private ProductDao productDao;

    public ProductServiceImpl() {
        this.productDao = new ProductDaoImpl();
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
    public List<Product> findByCategoryIdinPage(int categoryId, int page, int pageSize) {
        return productDao.findByCategoryIdinPage(categoryId, page, pageSize);
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
        // Implementation can be added here if ProductPage is a wrapper for paginated results with filter
        List<Product> products = findProductsByFilter(filter, 1, 10); // Example, adjust as needed
        ProductPage page = new ProductPage();
        page.setProducts(products);
        page.setTotalPages((int) Math.ceil((double) countProductsByFilter(filter) / 10));
        return page;
    }

    @Override
    public List<ProductDTO> MapToProductDTO(List<Product> products) {
        // Implementation for mapping to DTO can be added here
        // For now, return empty list; replace with actual mapping logic
        return List.of();
    }

    @Override
    public List<Product> findByNamePaginated(String name, int firstResult, int maxResults) {
        return productDao.findByNamePaginated(name, firstResult, maxResults);
    }

    @Override
    public long countByName(String name) {
        return productDao.countByName(name);
    }
}