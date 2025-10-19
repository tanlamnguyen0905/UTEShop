package ute.service.impl;

import ute.dao.impl.ProductDaoImpl;
import ute.dao.inter.ProductDao;
import ute.entities.Product;
import ute.service.inter.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private ProductDao productDao = new ProductDaoImpl();

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productDao.findById(id);
    }

    @Override
    public void save(Product product) {
        productDao.insert(product);
    }

    @Override
    public void delete(Long productID) {
        productDao.delete(productID);
    }

    @Override
    public void update(Product product) {
        productDao.update(product);
    }

    @Override
    public List<Product> findByName(String name) {
        return productDao.findByName(name);
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return productDao.findByCategoryId(categoryId);
    }

    @Override
    public List<Product> findLatest(int limit) {
        return productDao.findLatest(limit);
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
    public List<Product> findByNamePaginated(String name, int firstResult, int maxResults) {
        return productDao.findByNamePaginated(name, firstResult, maxResults);
    }

    @Override
    public long countByName(String name) {
        return productDao.countByName(name);
    }
}