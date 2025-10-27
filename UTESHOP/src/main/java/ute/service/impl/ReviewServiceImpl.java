package ute.service.impl;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ute.dao.impl.ReviewDaoImpl;
import ute.dao.inter.ReviewDao;
import ute.entities.Review;
import ute.service.inter.ReviewService;

public class ReviewServiceImpl implements ReviewService {
    
    private final ReviewDao reviewDao;
    
    public ReviewServiceImpl() {
        this.reviewDao = new ReviewDaoImpl();
    }

    @Override
    public void addReview(Review review) {
        reviewDao.insert(review);
    }

    @Override
    public void updateReview(Review review) {
        reviewDao.update(review);
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewDao.delete(reviewId);
    }

    @Override
    public List<Review> getProductReviews(Long productId) {
        return reviewDao.findByProductId(productId);
    }

    @Override
    public double getProductAverageRating(Long productId) {
        Double avg = reviewDao.getAverageRatingByProductId(productId);
        return avg != null ? avg : 0.0;
    }

    @Override
    public Long getProductReviewCount(Long productId) {
        return reviewDao.getReviewCountByProductId(productId);
    }

    @Override
    public Review getUserProductReview(Long userId, Long productId) {
        return reviewDao.findByUserIdAndProductId(userId, productId);
    }

    @Override
    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
        return reviewDao.hasUserPurchasedProduct(userId, productId);
    }

    @Override
    public Long getTotalReviewCount() {
        return reviewDao.getTotalReviewCount();
    }

    @Override
    public Double getAverageRating() {
        return reviewDao.getAverageRating();
    }

    @Override
    public Double getAverageRatingByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        return reviewDao.getAverageRatingByDateRange(startDateTime, endDateTime);
    }
}