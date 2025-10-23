package ute.service.admin.Impl;

import java.util.List;

import ute.dao.admin.Impl.BrandDaoImpl;
import ute.dao.admin.inter.BrandDao;
import ute.entities.Brand;
import ute.service.admin.inter.BrandService;

public class BrandServiceImpl implements BrandService {
    private BrandDao brandDao;

    public BrandServiceImpl() {
        this.brandDao = new BrandDaoImpl();
    }

    @Override
    public void insert(Brand brand) {
        brandDao.insert(brand);
    }

    @Override
    public void update(Brand brand) {
        brandDao.update(brand);
    }

    @Override
    public void delete(Long id) {
        brandDao.delete(id.intValue());
    }

    @Override
    public Brand findById(Long id) {
        return brandDao.findById(id.intValue());
    }

    @Override
    public List<Brand> findAll() {
        return brandDao.findAll();
    }

    @Override
    public List<Brand> findByName(String name) {
        return brandDao.findByName(name);
    }

    @Override
    public List<Brand> findByNamePaginated(String name, int firstResult, int maxResults) {
        return brandDao.findByNamePaginated(name, firstResult, maxResults);
    }

    @Override
    public long countByName(String name) {
        return brandDao.countByName(name);
    }

    @Override
    public long count() {
        return brandDao.count();
    }

    @Override
    public List<Brand> findPage(int page, int pageSize) {
        return brandDao.findPage(page, pageSize);
    }
}