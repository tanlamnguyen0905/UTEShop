package ute.dao.inter;

import ute.entities.Image;

public interface ImageDao {
    // Thêm danh mục mới
    void insert(Image image);

    // Cập nhật danh mục
    void update(Image image);

}
