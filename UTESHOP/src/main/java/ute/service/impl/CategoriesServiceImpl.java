package ute.service.impl;

import ute.dao.impl.CategoriesDaoImpl;
import ute.dao.inter.CategoriesDao;
import ute.entities.Categories;
import ute.service.inter.CategoriesService;

import java.util.List;

public class CategoriesServiceImpl implements CategoriesService {
    private CategoriesDao categoriesDao = new CategoriesDaoImpl();

    @Override
    public List<Categories> findAll() {
        return categoriesDao.findAll();
    }

    @Override
    public Categories findById(Long id) {
        return categoriesDao.findById(id);
    }

    @Override
    public void save(Categories category) {
        categoriesDao.insert(category);
    }

    @Override
    public void delete(Long categoryID) {
        categoriesDao.delete(categoryID);
    }

    @Override
    public void update(Categories category) {
        categoriesDao.update(category);
    }

    @Override
    public List<Categories> findByName(String name) {
        return categoriesDao.findByName(name);
    }

    @Override
    public Categories findByNameExact(String name) {
        return categoriesDao.findByNameExact(name);
    }

    @Override
    public long count() {
        return categoriesDao.count();
    }

    @Override
    public List<Categories> findPage(int page, int pageSize) {
        return categoriesDao.findPage(page, pageSize);
    }

    @Override
    public List<Categories> findByNamePaginated(String name, int firstResult, int maxResults) {
        return categoriesDao.findByNamePaginated(name, firstResult, maxResults);
    }

    @Override
    public long countByName(String name) {
        return categoriesDao.countByName(name);
    }
}