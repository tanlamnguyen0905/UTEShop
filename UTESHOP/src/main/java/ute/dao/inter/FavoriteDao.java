package ute.dao.inter;

import java.util.List;

import ute.dto.ProductDTO;
import ute.entities.Favorite;
import ute.entities.Product;

public interface FavoriteDao {
    Favorite insert(Favorite favorite);

    void deleteByUserAndProduct(Long userId, Long productId);

    boolean existsByUserAndProduct(Long userId, Long productId);

    List<ProductDTO> findByUserId(Long userId);
}
