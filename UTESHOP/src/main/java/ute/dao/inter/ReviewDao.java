package ute.dao.inter;

import java.time.LocalDateTime;
import java.util.List;
import ute.entities.Review;

public interface ReviewDao {
	// Basic CRUD
	void insert(Review review);
	void update(Review review);
	void delete(Long id);
	Review findById(Long id);
	List<Review> findAll();
	
	// Dashboard-specific queries
	
	// Get total review count
	Long getTotalReviewCount();
	
	// Get average rating across all reviews
	Double getAverageRating();
	
	// Get average rating within date range
	Double getAverageRatingByDateRange(LocalDateTime startDate, LocalDateTime endDate);
	
	// Get reviews by product
	List<Review> findByProductId(Long productId);
	
	// Get reviews by user
	List<Review> findByUserId(Long userId);
}
