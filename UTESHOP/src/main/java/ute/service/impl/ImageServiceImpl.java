package ute.service.impl;

import ute.entities.Image;
import ute.service.inter.ImageService;
import ute.dao.impl.ImageDaoImpl;

public class ImageServiceImpl implements ImageService {

    private ImageDaoImpl imgDao = new ImageDaoImpl();

    @Override
    public void insert(Image image) {
        // TODO Auto-generated method stub
        imgDao.insert(image);

    }

    @Override
    public void update(Image image) {
        // TODO Auto-generated method stub
        imgDao.update(image);
    }

}
