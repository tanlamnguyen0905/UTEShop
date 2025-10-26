package ute.service.admin.inter;

import ute.entities.Review;
import ute.dto.ProductWithAvgRatingDTO;
import java.util.List;

public interface ReviewService {
    void insert(Review review);
    void update(Review review);
    void delete(Review review);
    void delete(Long id);

    List<Review> findByContentOrRatingPaginated(String content, String rating, int firstResult, int maxResults);
    long countByContentOrRating(String content, String rating);
    List<Review> findPage(int page, int size);
    long count();
    Review findById(Long id);
    List<Review> findAll();
    List<ProductWithAvgRatingDTO> getAverageRatingPerProduct();
}