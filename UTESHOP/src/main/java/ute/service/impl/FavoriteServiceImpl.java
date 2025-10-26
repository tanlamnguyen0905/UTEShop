package ute.service.impl;

import java.util.List;

import ute.dao.impl.FavoriteDaoImpl;
import ute.dao.inter.FavoriteDao;
import ute.dto.ProductDTO;
import ute.entities.Favorite;
import ute.entities.Product;
import ute.service.inter.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao;

    public FavoriteServiceImpl() {
        this.favoriteDao = new FavoriteDaoImpl();
    }

    @Override
    public Favorite addFavorite(Favorite favorite) {
        return favoriteDao.insert(favorite);
    }

    @Override
    public void removeFavorite(Long userId, Long productId) {
        favoriteDao.deleteByUserAndProduct(userId, productId);
    }

    @Override
    public boolean isFavorite(Long userId, Long productId) {
        return favoriteDao.existsByUserAndProduct(userId, productId);
    }

    @Override
    public List<ProductDTO> findByUserId(Long userId) {
        // TODO Auto-generated method stub
        List<Product> products = favoriteDao.findByUserId(userId);
        System.out.println("=== DEBUG FAVORITE ===");
        System.out.println("SỐ sản phẩm : " + products.size());

        ProductServiceImpl service = new ProductServiceImpl();
        List<ProductDTO> listDTO = service.MapToProductDTO(products);
        if (listDTO == null || listDTO.isEmpty())
            System.out.println("sản phẩm null hoảc empty : ");
        else
            System.out.println("SỐ sản phẩm DTO : " + listDTO.size());
        return listDTO;
    }

    @Override
    public void clearFavorite(Long userId) {
        // TODO Auto-generated method stub
        favoriteDao.clearFavorite(userId);
    }

}
