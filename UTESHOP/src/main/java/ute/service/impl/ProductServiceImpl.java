package ute.service.impl;

import java.util.List;
import java.util.Map;

import ute.dao.impl.OrderDetailDaoImpl;
import ute.dao.impl.ProductDaoImpl;
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