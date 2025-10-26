package ute.dao.admin.inter;

import ute.entities.Image;

public interface ImageDao {
    Image findImageById(Long id);
    void deleteImage(Image image);
}
