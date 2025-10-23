package ute.dao.inter;

import java.util.List;
import ute.entities.Review;

public interface ReviewDao {
    void insert(Review review);

    void update(Review review);

    void delete(Long id);

    Review findById(Long id);

    List<Review> findByProductId(Long productId);

    Double getAverageRatingByProductId(Long productId);

    Long getReviewCountByProductId(Long productId);

    List<Review> findByUserId(Long userId);
}