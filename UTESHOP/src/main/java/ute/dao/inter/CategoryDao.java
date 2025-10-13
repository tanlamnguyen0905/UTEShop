package ute.dao.inter;

import java.util.List;

import ute.entities.Categories;
import ute.entities.Product;

public interface CategoryDao {

    // Lấy danh sách tất cả sản phẩm
    List<Categories> findAll();
}
