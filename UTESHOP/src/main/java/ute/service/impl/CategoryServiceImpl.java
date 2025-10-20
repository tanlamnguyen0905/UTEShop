package ute.service.impl;

import java.util.List;

import ute.dao.impl.CategoryDaoImpl;
import ute.entities.Categories;

public class CategoryServiceImpl implements ute.service.inter.CategoryService {

	private static CategoryDaoImpl instance = new CategoryDaoImpl();
	@Override
	public List<Categories> findAll() {
		// TODO Auto-generated method stub
		return instance.findAll();
	}
	

}
