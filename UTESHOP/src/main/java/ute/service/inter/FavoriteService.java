package ute.service.inter;

import java.util.List;

import ute.dto.ProductDTO;
import ute.entities.Favorite;

public interface FavoriteService {
    Favorite addFavorite(Favorite favorite);

    void removeFavorite(Long userId, Long productId);

    boolean isFavorite(Long userId, Long productId);

    List<ProductDTO> findByUserId(Long userId);

}
