package ute.service.impl;

import java.util.List;
import ute.dao.impl.ReviewDaoImpl;
import ute.dao.inter.ReviewDao;
import ute.entities.Review;
import ute.service.inter.ReviewService;

public class ReviewServiceImpl implements ReviewService {
    private ReviewDao reviewDao;

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
}