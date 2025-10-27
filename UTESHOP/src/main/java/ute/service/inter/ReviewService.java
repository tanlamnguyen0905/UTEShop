package ute.service.inter;

import java.util.List;
import ute.entities.Review;

public interface ReviewService {
    void addReview(Review review);
    void updateReview(Review review);
    void deleteReview(Long reviewId);

    List<Review> getProductReviews(Long productId);
    double getProductAverageRating(Long productId);
    Long getProductReviewCount(Long productId);
    
    Review getUserProductReview(Long userId, Long productId);
    
    boolean hasUserPurchasedProduct(Long userId, Long productId);
    Long getTotalReviewCount();
    Double getAverageRating();
    Double getAverageRatingByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate);
}