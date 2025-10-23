package ute.dao.inter;

import java.util.List;
import ute.entities.Favorite;

public interface FavoriteDao {
    Favorite insert(Favorite favorite);

    void deleteByUserAndProduct(Long userId, Long productId);

    boolean existsByUserAndProduct(Long userId, Long productId);

    List<Favorite> findByUserId(Long userId);
}