package ute.controllers;

import java.io.IOException;

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

@WebServlet(urlPatterns = {"/review/submit"})
public class ReviewController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReviewService reviewService = new ReviewServiceImpl();
    private ProductService productService = new ProductServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        
        // Kiểm tra đăng nhập
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        Users currentUser = (Users) session.getAttribute("currentUser");
        
        try {
            // Lấy thông tin từ form
            String productIdStr = request.getParameter("productId");
            String ratingStr = request.getParameter("rating");
            String content = request.getParameter("content");
            
            if (productIdStr == null || productIdStr.isEmpty()) {
                session.setAttribute("error", "Thiếu thông tin sản phẩm!");
                response.sendRedirect(request.getContextPath() + "/user/profile#orders");
                return;
            }
            
            if (ratingStr == null || ratingStr.isEmpty()) {
                session.setAttribute("error", "Vui lòng chọn số sao đánh giá!");
                response.sendRedirect(request.getContextPath() + "/user/profile#orders");
                return;
            }
            
            Long productId = Long.parseLong(productIdStr);
            Double rating = Double.parseDouble(ratingStr);
            
            // Validate rating
            if (rating < 1 || rating > 5) {
                session.setAttribute("error", "Đánh giá phải từ 1 đến 5 sao!");
                response.sendRedirect(request.getContextPath() + "/user/profile#orders");
                return;
            }
            
            // Lấy product
            Product product = productService.findById(productId);
            if (product == null) {
                session.setAttribute("error", "Không tìm thấy sản phẩm!");
                response.sendRedirect(request.getContextPath() + "/user/profile#orders");
                return;
            }
            
            // Kiểm tra đã đánh giá chưa
            Review existingReview = reviewService.getUserProductReview(currentUser.getUserID(), productId);
            if (existingReview != null) {
                session.setAttribute("error", "Bạn đã đánh giá sản phẩm này rồi!");
                response.sendRedirect(request.getContextPath() + "/user/profile#orders");
                return;
            }
            
            // Tạo review mới
            Review review = Review.builder()
                    .user(currentUser)
                    .product(product)
                    .rating(rating)
                    .content(content != null && !content.trim().isEmpty() ? content.trim() : null)
                    .image(null) // Không xử lý upload ảnh để đơn giản
                    .build();
            
            reviewService.addReview(review);
            
            session.setAttribute("success", "Đánh giá thành công! Cảm ơn bạn đã đóng góp ý kiến.");
            response.sendRedirect(request.getContextPath() + "/user/profile#orders");
            
        } catch (NumberFormatException e) {
            session.setAttribute("error", "Dữ liệu không hợp lệ!");
            response.sendRedirect(request.getContextPath() + "/user/profile#orders");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/user/profile#orders");
        }
    }
}
