package ute.dao.inter;

import java.time.LocalDateTime;
import java.util.List;
import ute.entities.Review;

public interface ReviewDao {

	void insert(Review review);
	void update(Review review);
	void delete(Long id);
	Review findById(Long id);
	List<Review> findByProductId(Long productId);
	List<Review> findByUserId(Long userId);
	List<Review> findAll();
	
	// Get total review count
	Long getTotalReviewCount();
	
	// Get average rating across all reviews
	Double getAverageRating();
	
	// Get average rating within date range
	Double getAverageRatingByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    Double getAverageRatingByProductId(Long productId);

    Long getReviewCountByProductId(Long productId);
    
    // Kiểm tra user đã review sản phẩm chưa
    Review findByUserIdAndProductId(Long userId, Long productId);
    
    // Kiểm tra user có mua sản phẩm chưa (từ order đã hoàn tất)
    boolean hasUserPurchasedProduct(Long userId, Long productId);
}
