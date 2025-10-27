package ute.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ute.dao.impl.ReviewDaoImpl;
import ute.dao.inter.ReviewDao;
import ute.service.inter.ReviewService;

public class ReviewServiceImpl implements ReviewService {
    
    private final ReviewDao reviewDao;
    
    public ReviewServiceImpl() {
        this.reviewDao = new ReviewDaoImpl();
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