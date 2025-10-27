package ute.service.inter;

public interface ReviewService {

    // Review statistics
    Long getTotalReviewCount();
    Double getAverageRating();
    Double getAverageRatingByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate);
}
