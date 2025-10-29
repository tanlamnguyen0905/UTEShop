package ute.controllers;

import java.io.File;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import ute.entities.Product;
import ute.entities.Review;
import ute.entities.Users;
import ute.service.impl.ProductServiceImpl;
import ute.service.impl.ReviewServiceImpl;
import ute.service.inter.ProductService;
import ute.service.inter.ReviewService;
import ute.utils.Constant;
import ute.utils.FileStorage;

import com.google.gson.JsonObject;

@WebServlet(urlPatterns = {"/review/submit", "/review/add"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 5,        // 5 MB
    maxRequestSize = 1024 * 1024 * 10     // 10 MB
)
public class ReviewController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReviewService reviewService = new ReviewServiceImpl();
    private ProductService productService = new ProductServiceImpl();
    
    private boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith) || 
               request.getRequestURI().contains("/review/add");
    }
    
    private void sendJsonResponse(HttpServletResponse response, boolean success, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject json = new JsonObject();
        json.addProperty("success", success);
        json.addProperty("message", message);
        response.getWriter().write(json.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        boolean isAjax = isAjaxRequest(request);
        HttpSession session = request.getSession(false);
        
        // Kiểm tra đăng nhập
        if (session == null || session.getAttribute("currentUser") == null) {
            if (isAjax) {
                sendJsonResponse(response, false, "Vui lòng đăng nhập để đánh giá sản phẩm");
            } else {
                response.sendRedirect(request.getContextPath() + "/auth/login");
            }
            return;
        }
        
        Users currentUser = (Users) session.getAttribute("currentUser");
        
        try {
            // Lấy thông tin từ form
            String productIdStr = request.getParameter("productId");
            String ratingStr = request.getParameter("rating");
            String content = request.getParameter("content");
            
            if (productIdStr == null || productIdStr.isEmpty()) {
                if (isAjax) {
                    sendJsonResponse(response, false, "Thiếu thông tin sản phẩm!");
                } else {
                    session.setAttribute("error", "Thiếu thông tin sản phẩm!");
                    response.sendRedirect(request.getContextPath() + "/user/profile#orders");
                }
                return;
            }
            
            if (ratingStr == null || ratingStr.isEmpty()) {
                if (isAjax) {
                    sendJsonResponse(response, false, "Vui lòng chọn số sao đánh giá!");
                } else {
                    session.setAttribute("error", "Vui lòng chọn số sao đánh giá!");
                    response.sendRedirect(request.getContextPath() + "/user/profile#orders");
                }
                return;
            }
            
            Long productId = Long.parseLong(productIdStr);
            Double rating = Double.parseDouble(ratingStr);
            
            // Validate rating
            if (rating < 1 || rating > 5) {
                if (isAjax) {
                    sendJsonResponse(response, false, "Đánh giá phải từ 1 đến 5 sao!");
                } else {
                    session.setAttribute("error", "Đánh giá phải từ 1 đến 5 sao!");
                    response.sendRedirect(request.getContextPath() + "/user/profile#orders");
                }
                return;
            }
            
            // Kiểm tra product tồn tại
            Product product = productService.findById(productId);
            if (product == null) {
                if (isAjax) {
                    sendJsonResponse(response, false, "Không tìm thấy sản phẩm!");
                } else {
                    session.setAttribute("error", "Không tìm thấy sản phẩm!");
                    response.sendRedirect(request.getContextPath() + "/user/profile#orders");
                }
                return;
            }
            
            // Kiểm tra đã đánh giá chưa
            Review existingReview = reviewService.getUserProductReview(currentUser.getUserID(), productId);
            if (existingReview != null) {
                if (isAjax) {
                    sendJsonResponse(response, false, "Bạn đã đánh giá sản phẩm này rồi!");
                } else {
                    session.setAttribute("error", "Bạn đã đánh giá sản phẩm này rồi!");
                    response.sendRedirect(request.getContextPath() + "/user/profile#orders");
                }
                return;
            }
            
            // Xử lý upload ảnh
            String imageFileName = null;
            Part filePart = request.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                try {
                    // Validate file type
                    String contentType = filePart.getContentType();
                    if (!contentType.startsWith("image/")) {
                        if (isAjax) {
                            sendJsonResponse(response, false, "Chỉ chấp nhận file hình ảnh!");
                        } else {
                            session.setAttribute("error", "Chỉ chấp nhận file hình ảnh!");
                            response.sendRedirect(request.getContextPath() + "/user/profile#orders");
                        }
                        return;
                    }
                    
                    FileStorage reviewStorage = new FileStorage(request.getServletContext(), Constant.UPLOAD_DIR_REVIEW);
                    imageFileName = reviewStorage.save(filePart);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            // Tạo review mới - dùng product đã load (merge sẽ xử lý)
            Review review = Review.builder()
                    .user(currentUser)
                    .product(product)
                    .rating(rating)
                    .content(content != null && !content.trim().isEmpty() ? content.trim() : null)
                    .image(imageFileName) // Lưu tên file ảnh
                    .build();
            
            reviewService.addReview(review);
            
            if (isAjax) {
                sendJsonResponse(response, true, "Đánh giá thành công! Cảm ơn bạn đã đóng góp ý kiến.");
            } else {
                session.setAttribute("success", "Đánh giá thành công! Cảm ơn bạn đã đóng góp ý kiến.");
                response.sendRedirect(request.getContextPath() + "/user/profile#orders");
            }
            
        } catch (NumberFormatException e) {
            if (isAjax) {
                sendJsonResponse(response, false, "Dữ liệu không hợp lệ!");
            } else {
                session.setAttribute("error", "Dữ liệu không hợp lệ!");
                response.sendRedirect(request.getContextPath() + "/user/profile#orders");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (isAjax) {
                sendJsonResponse(response, false, "Lỗi: " + e.getMessage());
            } else {
                session.setAttribute("error", "Lỗi: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/user/profile#orders");
            }
        }
    }
}
