package ute.service.admin.Impl;

import ute.dao.admin.Impl.ReviewDaoImpl;
import ute.dao.admin.inter.ReviewDao;
import ute.entities.Review;
import ute.dto.ProductWithAvgRatingDTO;
import ute.service.admin.inter.ReviewService;
import java.util.List;

public class ReviewServiceImpl implements ReviewService {
    private ReviewDao reviewDao = new ReviewDaoImpl();

    @Override
    public void insert(Review review) {
        reviewDao.insert(review);
    }

    @Override
    public void update(Review review) {
        reviewDao.update(review);
    }

    @Override
    public void delete(Review review) {
        reviewDao.delete(review);
    }

    @Override
    public void delete(Long id) {
        reviewDao.delete(id);
    }

    @Override
    public Review findById(Long id) {
        return reviewDao.findById(id);
    }

    @Override
    public List<Review> findByContentOrRatingPaginated(String content, String rating, int firstResult, int maxResults) {
        return reviewDao.findByContentOrRatingPaginated(content, rating, firstResult, maxResults);
    }

    @Override
    public long countByContentOrRating(String content, String rating) {
        return reviewDao.countByContentOrRating(content, rating);
    }

    @Override
    public List<Review> findPage(int page, int size) {
        return reviewDao.findPage(page, size);
    }

    @Override
    public long count() {
        return reviewDao.count();
    }

    @Override
    public List<Review> findAll() {
        return reviewDao.findAll();
    }

    @Override
    public List<ProductWithAvgRatingDTO> getAverageRatingPerProduct() {
        return reviewDao.getAverageRatingPerProduct();
    }
}