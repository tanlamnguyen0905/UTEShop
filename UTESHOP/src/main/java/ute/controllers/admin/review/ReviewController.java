package ute.controllers.admin.review;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ute.entities.Product;
import ute.entities.Review;
import ute.dto.ProductWithAvgRatingDTO;
import ute.entities.Users;
import ute.service.admin.Impl.UserServiceImpl;
import ute.service.admin.inter.ReviewService;
import ute.service.admin.Impl.ReviewServiceImpl;
import ute.service.impl.ProductServiceImpl;
import ute.service.inter.ProductService;
import ute.utils.Constant;
import ute.utils.FileStorage;
import ute.service.admin.inter.UserService;

@WebServlet(urlPatterns = { "/api/admin/review/searchpaginated", "/api/admin/review/saveOrUpdate",
        "/api/admin/review/delete", "/api/admin/review/view", "/api/admin/review/average", "/api/admin/review/exportExcel" })
@MultipartConfig
public class ReviewController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private  ProductService productService = new ProductServiceImpl();
    private  UserService userService = new UserServiceImpl();
    private ReviewService reviewService = new ReviewServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String uri = req.getRequestURI();

        if (uri.contains("/api/admin/review/searchpaginated")) {

            int page = 1;
            int size = 6;

            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
            }
            if (req.getParameter("size") != null) {
                size = Integer.parseInt(req.getParameter("size"));
            }

            String searchKeyword = req.getParameter("searchKeyword");
            String ratingFilter = req.getParameter("ratingFilter"); // Lọc theo rating (1-5 or all)
            boolean isSearch = searchKeyword != null && !searchKeyword.trim().isEmpty();
            boolean isRatingFilter = ratingFilter != null && !ratingFilter.equals("all");

            int firstResult = (page - 1) * size;

            List<Review> reviewList;
            int totalReviews;
            if (isSearch || isRatingFilter) {
                reviewList = reviewService.findByContentOrRatingPaginated(searchKeyword, ratingFilter, firstResult, size);
                totalReviews = (int) reviewService.countByContentOrRating(searchKeyword, ratingFilter);
            } else {
                reviewList = reviewService.findPage(page, size);
                totalReviews = (int) reviewService.count();
            }
            int totalPages = (int) Math.ceil((double) totalReviews / size);

            req.setAttribute("reviewList", reviewList);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("size", size);
            req.setAttribute("searchKeyword", searchKeyword);
            req.setAttribute("ratingFilter", ratingFilter != null ? ratingFilter : "all");

            req.getRequestDispatcher("/WEB-INF/views/admin/review/searchpaginated.jsp").forward(req, resp);

        } else if (uri.contains("/api/admin/review/saveOrUpdate")) {
            String idStr = req.getParameter("id");
            Review review = new Review();
            if (idStr != null && !idStr.isEmpty()) {
                review = reviewService.findById(Long.parseLong(idStr));
                if (review == null) {
                    req.setAttribute("error", "Không tìm thấy Review!");
                    req.getRequestDispatcher("/WEB-INF/views/admin/review/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            }
            req.setAttribute("review", review);
            req.getRequestDispatcher("/WEB-INF/views/admin/review/addOrEdit.jsp").forward(req, resp);
        } else if (uri.contains("/api/admin/review/view")) {
            String idStr = req.getParameter("id");
            Review review = reviewService.findById(Long.parseLong(idStr));
            if (review == null) {
                req.setAttribute("error", "Không tìm thấy Review!");
                req.getRequestDispatcher("/WEB-INF/views/admin/review/searchpaginated.jsp").forward(req, resp);
                return;
            }
            // Format createAt
            req.setAttribute("createAtFormatted", review.getCreatedAt() != null ? review.getCreatedAt().format(FORMATTER) : "Chưa xác định");
            req.setAttribute("review", review);
            req.getRequestDispatcher("/WEB-INF/views/admin/review/view.jsp").forward(req, resp);
        } else if (uri.contains("/api/admin/review/average")) {
            List<ProductWithAvgRatingDTO> avgRatings = reviewService.getAverageRatingPerProduct();
            req.setAttribute("avgRatings", avgRatings);
            req.getRequestDispatcher("/WEB-INF/views/admin/review/average.jsp").forward(req, resp);
        } else if (uri.contains("/api/admin/review/exportExcel")) {
            List<Review> allReviews = reviewService.findAll(); // Giả sử service có findAll()
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Reviews");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Content");
            header.createCell(2).setCellValue("Image");
            header.createCell(3).setCellValue("Rating");
            header.createCell(4).setCellValue("Create At");
            header.createCell(5).setCellValue("User ID");
            header.createCell(6).setCellValue("Product ID");

            int rowNum = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Review r : allReviews) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(r.getReviewID());
                row.createCell(1).setCellValue(r.getContent());
                row.createCell(2).setCellValue(r.getImage());
                row.createCell(3).setCellValue(r.getRating());
                row.createCell(4).setCellValue(r.getCreatedAt() != null ? r.getCreatedAt().format(formatter) : "");
                row.createCell(5).setCellValue(r.getUser() != null ? String.valueOf(r.getUser().getUserID()) : "");
                row.createCell(6).setCellValue(r.getProduct() != null ? String.valueOf(r.getProduct().getProductID()) : "");
            }

            resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            resp.setHeader("Content-Disposition", "attachment; filename=reviews.xlsx");
            ServletOutputStream outputStream = resp.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            return;
        } else if (uri.contains("/api/admin/review/delete")) {
            String idStr = req.getParameter("id");
            reviewService.delete(Long.parseLong(idStr));
            resp.sendRedirect(req.getContextPath() + "/api/admin/review/searchpaginated");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        if (uri.contains("/api/admin/review/saveOrUpdate")) {
            Review review = new Review();

            String idStr = req.getParameter("id");
            String content = req.getParameter("content");
            String ratingStr = req.getParameter("rating");
            String userIdStr = req.getParameter("userId");
            String productIdStr = req.getParameter("productId");

            Long id = null;
            if (idStr != null && !idStr.isEmpty()) {
                id = Long.parseLong(idStr);
                review = reviewService.findById(id);
                if (review == null) {
                    req.setAttribute("error", "Không tìm thấy Review!");
                    req.getRequestDispatcher("/WEB-INF/views/admin/review/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            }

            // Validate
            if (content == null || content.trim().isEmpty()) {
                req.setAttribute("error", "Nội dung không được để trống!");
                req.setAttribute("review", review);
                req.getRequestDispatcher("/WEB-INF/views/admin/review/addOrEdit.jsp").forward(req, resp);
                return;
            }
            if (ratingStr == null || ratingStr.trim().isEmpty()) {
                req.setAttribute("error", "Rating không được để trống!");
                req.setAttribute("review", review);
                req.getRequestDispatcher("/WEB-INF/views/admin/review/addOrEdit.jsp").forward(req, resp);
                return;
            }
            try {
                Double rating = Double.parseDouble(ratingStr.trim());
                if (rating < 1 || rating > 5) {
                    req.setAttribute("error", "Rating phải từ 1 đến 5!");
                    req.setAttribute("review", review);
                    req.getRequestDispatcher("/WEB-INF/views/admin/review/addOrEdit.jsp").forward(req, resp);
                    return;
                }
                review.setRating(rating);
            } catch (NumberFormatException e) {
                req.setAttribute("error", "Rating phải là số hợp lệ!");
                req.setAttribute("review", review);
                req.getRequestDispatcher("/WEB-INF/views/admin/review/addOrEdit.jsp").forward(req, resp);
                return;
            }
            if (userIdStr == null || userIdStr.trim().isEmpty()) {
                req.setAttribute("error", "User ID không được để trống!");
                req.setAttribute("review", review);
                req.getRequestDispatcher("/WEB-INF/views/admin/review/addOrEdit.jsp").forward(req, resp);
                return;
            }
            if (productIdStr == null || productIdStr.trim().isEmpty()) {
                req.setAttribute("error", "Product ID không được để trống!");
                req.setAttribute("review", review);
                req.getRequestDispatcher("/WEB-INF/views/admin/review/addOrEdit.jsp").forward(req, resp);
                return;
            }

            review.setContent(content.trim());

            Users user = userService.findUserById(Long.parseLong(userIdStr.trim()));
            if (user == null) {
                req.setAttribute("error", "Không tìm thấy User với ID này!");
                req.setAttribute("review", review);
                req.getRequestDispatcher("/WEB-INF/views/admin/review/addOrEdit.jsp").forward(req, resp);
                return;
            }
            review.setUser(user);

            Product product = productService.findById(Long.parseLong(productIdStr.trim()));
            if (product == null) {
                req.setAttribute("error", "Không tìm thấy Product với ID này!");
                req.setAttribute("review", review);
                req.getRequestDispatcher("/WEB-INF/views/admin/review/addOrEdit.jsp").forward(req, resp);
                return;
            }
            review.setProduct(product);

            review.setCreatedAt(LocalDateTime.now()); // Auto set current time

            Part filePart = req.getPart("image");
            String image = null;
            if (filePart != null && filePart.getSize() > 0) {
                FileStorage reviewStorage = new FileStorage(req.getServletContext(), Constant.UPLOAD_DIR_REVIEW);
                image = reviewStorage.save(filePart);
            } else if (id != null) {
                image = review.getImage();
            }
            review.setImage(image);

            String message;
            if (id != null) {
                reviewService.update(review);
                message = "Review đã được cập nhật!";
            } else {
                reviewService.insert(review);
                message = "Review đã được thêm mới!";
            }

            req.getSession().setAttribute("message", message);
            resp.sendRedirect(req.getContextPath() + "/api/admin/review/searchpaginated");
        }
    }
}