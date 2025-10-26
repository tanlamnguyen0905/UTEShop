package ute.service.admin.inter;

import ute.entities.Image;

import java.util.List;

public interface ImageService {
    Image findImageById(Long id);
    void deleteImage(Image image);
}
