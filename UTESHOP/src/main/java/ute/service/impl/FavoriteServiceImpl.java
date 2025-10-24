package ute.service.impl;

import java.util.List;

import ute.dao.impl.FavoriteDaoImpl;
import ute.dao.inter.FavoriteDao;
import ute.entities.Favorite;
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
    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteDao.findByUserId(userId);
    }

}
