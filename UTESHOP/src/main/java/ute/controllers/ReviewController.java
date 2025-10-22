package ute.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import ute.entities.Product;
import ute.entities.Review;
import ute.entities.Users;
import ute.service.impl.ProductServiceImpl;
import ute.service.impl.ReviewServiceImpl;
import ute.service.inter.ProductService;
import ute.service.inter.ReviewService;

@WebServlet(urlPatterns = { "/review/add" })
public class ReviewController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReviewService reviewService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.reviewService = new ReviewServiceImpl();
        this.productService = new ProductServiceImpl();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        try {
            HttpSession session = req.getSession();
            Users user = (Users) session.getAttribute("user");
            if (user == null) {
                out.print("{\"success\":false, \"message\":\"Not authenticated\"}");
                return;
            }

            String productIdStr = req.getParameter("productId");
            String ratingStr = req.getParameter("rating");
            String comment = req.getParameter("comment");

            if (productIdStr == null || ratingStr == null || comment == null) {
                out.print("{\"success\":false, \"message\":\"Missing parameters\"}");
                return;
            }

            Long productId = Long.parseLong(productIdStr);
            double rating = Double.parseDouble(ratingStr);

            Product product = productService.findById(productId);
            if (product == null) {
                out.print("{\"success\":false, \"message\":\"Product not found\"}");
                return;
            }

            Review review = Review.builder()
                    .content(comment)
                    .rating(rating)
                    .createAt(LocalDateTime.now())
                    .user(user)
                    .product(product)
                    .build();

            reviewService.addReview(review);

            out.print("{\"success\":true, \"message\":\"Review submitted\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false, \"message\":\"Server error\"}");
        }
    }
}
