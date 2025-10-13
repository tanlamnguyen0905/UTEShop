package ute.dao.inter;

import java.util.List;

import ute.entities.Categories;

public interface CategoryDao {

    List<Categories> findAll();
}
