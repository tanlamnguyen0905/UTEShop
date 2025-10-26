package ute.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import ute.entities.Review;
import ute.service.impl.ReviewServiceImpl;
import ute.service.inter.ReviewService;

@WebServlet(urlPatterns = { "/review-demo" })
public class ReviewDemoController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        this.reviewService = new ReviewServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Demo data for testing
            Long productId = 1L; // Assuming product ID 1 exists
            
            // Get product reviews
            List<Review> reviews = reviewService.getProductReviews(productId);
            double averageRating = reviewService.getProductAverageRating(productId);
            Long reviewCount = reviewService.getProductReviewCount(productId);

            // Calculate rating distribution
            Map<Integer, Integer> ratingDistribution = new HashMap<>();
            for (int i = 1; i <= 5; i++) {
                ratingDistribution.put(i, 0);
            }
            for (Review review : reviews) {
                int rating = review.getRating().intValue();
                ratingDistribution.put(rating, ratingDistribution.get(rating) + 1);
            }

            // Set attributes for JSP
            req.setAttribute("reviews", reviews);
            req.setAttribute("averageRating", averageRating);
            req.setAttribute("totalReviews", reviewCount);
            req.setAttribute("ratingDistribution", ratingDistribution);

            // Forward to demo page
            req.getRequestDispatcher("/WEB-INF/views/web/review-demo.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/404");
        }
    }
}
