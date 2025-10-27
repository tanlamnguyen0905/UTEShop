package ute.controllers;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Added HttpSession import

// imports cleaned up
import ute.entities.Product;
import ute.dto.ProductDTO;
import ute.entities.Review;
import ute.service.impl.ProductServiceImpl;
import ute.service.impl.ReviewServiceImpl;
import ute.service.inter.ProductService;
import ute.service.inter.ReviewService;

@WebServlet(urlPatterns = { "/detailProduct" })
public class ProductDetailController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductService productService;
    private ReviewService reviewService;
    private ute.service.inter.FavoriteService favoriteService;

    @Override
    public void init() throws ServletException {
        // EntityManager em = JPAConfig.getEntityManager();
        this.productService = new ProductServiceImpl();
        this.reviewService = new ReviewServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long productId = Long.parseLong(req.getParameter("productID"));
            System.out.println("ma so san pham" + productId);

            // Get product details
            Product product = productService.findById(productId);
            if (product == null) {
                resp.sendRedirect(req.getContextPath() + "/404");
                return;
            }

            ProductDTO productDTO = ProductDTO.fromEntity(product);

            // Get product reviews
            List<Review> reviews = reviewService.getProductReviews(productId);
            double averageRating = reviewService.getProductAverageRating(productId);
            Long reviewCount = reviewService.getProductReviewCount(productId);

            // Check favorite status if user logged in
            HttpSession session = req.getSession(false);
            boolean isFavorite = false;
            if (session != null) {
                ute.entities.Users u = (ute.entities.Users) session.getAttribute("user");
                if (u == null)
                    u = (ute.entities.Users) session.getAttribute("currentUser");
                if (u != null) {
                    if (favoriteService == null)
                        favoriteService = new ute.service.impl.FavoriteServiceImpl();
                    isFavorite = favoriteService.isFavorite(u.getUserID(), productId);
                }
            }
            // expose favorite flag to JSP
            req.setAttribute("isFavorite", isFavorite);

            // Set attributes for JSP
            req.setAttribute("productDTO", productDTO);
            req.setAttribute("reviews", reviews);
            req.setAttribute("averageRating", averageRating);
            req.setAttribute("reviewCount", reviewCount);

            // Forward to detail page
            req.getRequestDispatcher("/WEB-INF/views/web/detailProduct.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/404");
        }
    }
}