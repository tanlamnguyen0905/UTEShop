package ute.service.admin.Impl;

import ute.dao.admin.Impl.ImageDaoImpl;
import ute.dao.admin.inter.ImageDao;
import ute.entities.Image;
import ute.service.admin.inter.ImageService;

public class ImageServiceImpl implements ImageService {
    ImageDao imageDao = new ImageDaoImpl();
    @Override
    public Image findImageById(Long id) {
        return imageDao.findImageById(id);
    }

    @Override
    public void deleteImage(Image image) {
        imageDao.deleteImage(image);
    }
}
