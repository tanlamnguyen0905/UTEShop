package ute.controllers.manager.product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ute.entities.Product;
import ute.entities.Categories;
import ute.service.impl.ProductServiceImpl;
import ute.service.inter.CategoriesService;
import ute.service.inter.ProductService;
import ute.utils.ProductFilter;
import ute.service.impl.CategoriesServiceImpl;  // Assume exists
import ute.utils.ProductPage;

@MultipartConfig(maxFileSize = 10485760, maxRequestSize = 20971520)
@WebServlet(urlPatterns = {
        "/api/manager/products/searchpaginated",
        "/api/manager/products/import",
        "/api/manager/products/export",
        "/api/manager/products/report"
})
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductService productService = new ProductServiceImpl();
    private CategoriesService categoriesService = new CategoriesServiceImpl();  // For dropdown

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();

        try {
            if (uri.contains("/api/manager/products/searchpaginated")) {
                // Thống kê tồn kho paginated
                int page = 1;
                int size = 10;
                String pageStr = req.getParameter("page");
                String sizeStr = req.getParameter("size");
                if (pageStr != null && !pageStr.trim().isEmpty()) page = Integer.parseInt(pageStr);
                if (sizeStr != null && !sizeStr.trim().isEmpty()) size = Integer.parseInt(sizeStr);
                String keyword = req.getParameter("keyword");
                String categoryIdStr = req.getParameter("categoryId");
                String minStockStr = req.getParameter("minStock");

                ProductFilter filter = new ProductFilter();
                filter.setKeyword(keyword != null ? keyword.trim() : "");
                if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
                    filter.setCategoryId(Long.parseLong(categoryIdStr));
                }
                // Không override keyword nữa, thêm minStock như điều kiện riêng (giả sử ProductFilter hỗ trợ setMinStock)
                // Nếu chưa có, có thể append vào keyword: if (minStockStr != null && !minStockStr.trim().isEmpty()) { filter.setKeyword(filter.getKeyword() + " AND stockQuantity <= " + minStockStr.trim()); }
                filter.setCurrentPage(page);
                filter.setPageSize(size);

                // Sử dụng getProductsPageByFilter để có pagination chuẩn
                ProductPage pageResult = productService.getProductsPageByFilter(filter);
                List<Product> productList = pageResult.getProducts();  // Giả sử có getProducts()
                long totalProducts = pageResult.getTotal();
                int totalPages = pageResult.getTotalPages();

                // Log cho debug
                System.out.println("Loaded " + productList.size() + " products with filter: " + filter.getKeyword());

                // Load categories for dropdown (mock if service error)
                List<Categories> categoriesList = new ArrayList<>();
                try {
                    categoriesList = categoriesService.findAll();
                } catch (Exception e) {
                    // Mock data if service fail
                    Categories mockCat = new Categories();
                    mockCat.setCategoryID(1L);
                    mockCat.setCategoryName("Tất cả");
                    categoriesList.add(mockCat);
                }

                req.setAttribute("productList", productList);
                req.setAttribute("totalProducts", totalProducts);
                req.setAttribute("currentPage", page);
                req.setAttribute("totalPages", totalPages);
                req.setAttribute("size", size);
                req.setAttribute("filter", filter);
                req.setAttribute("categoriesList", categoriesList);
                req.getRequestDispatcher("/WEB-INF/views/manager/products/searchpaginated.jsp").forward(req, resp);

            } else if (uri.contains("/api/manager/products/report")) {
                // Xuất báo cáo tồn kho
                ProductFilter filter = new ProductFilter();
                filter.setCurrentPage(1);
                filter.setPageSize(Integer.MAX_VALUE);  // All data
                productService.exportInventoryReport(resp, filter);
                return;  // End response for download

            } else if (uri.contains("/api/manager/products/import")) {
                // Form nhập hàng (GET: show form, POST: process)
                String idStr = req.getParameter("id");
                Product product = null;
                if (idStr != null && !idStr.trim().isEmpty()) {  // FIX: Check empty before parse
                    try {
                        Long id = Long.parseLong(idStr.trim());
                        product = productService.findById(id);
                    } catch (NumberFormatException e) {
                        req.setAttribute("error", "ID sản phẩm không hợp lệ!");
                    }
                }
                if (product == null) {
                    req.setAttribute("error", "Sản phẩm không tồn tại hoặc chưa chọn!");
                    resp.sendRedirect(req.getContextPath() + "/api/manager/products/searchpaginated");
                    return;
                }
                req.setAttribute("product", product);
                req.getRequestDispatcher("/WEB-INF/views/manager/products/importForm.jsp").forward(req, resp);

            } else if (uri.contains("/api/manager/products/export")) {
                // Form xuất hàng (GET: show form, POST: process)
                String idStr = req.getParameter("id");
                Product product = null;
                if (idStr != null && !idStr.trim().isEmpty()) {  // FIX: Check empty before parse
                    try {
                        Long id = Long.parseLong(idStr.trim());
                        product = productService.findById(id);
                    } catch (NumberFormatException e) {
                        req.setAttribute("error", "ID sản phẩm không hợp lệ!");
                    }
                }
                if (product == null) {
                    req.setAttribute("error", "Sản phẩm không tồn tại hoặc chưa chọn!");
                    resp.sendRedirect(req.getContextPath() + "/api/manager/products/searchpaginated");
                    return;
                }
                req.setAttribute("product", product);
                req.getRequestDispatcher("/WEB-INF/views/manager/products/exportForm.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/error/500.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();

        try {
            if (uri.contains("/api/manager/products/import")) {
                // Nhập hàng (tăng stock)
                String idStr = req.getParameter("id");
                String quantityStr = req.getParameter("quantity");
                if (idStr == null || idStr.trim().isEmpty() || quantityStr == null || quantityStr.trim().isEmpty()) {
                    req.setAttribute("error", "Thiếu thông tin sản phẩm hoặc số lượng!");
                    resp.sendRedirect(req.getContextPath() + "/api/manager/products/searchpaginated");
                    return;
                }
                Long id = Long.parseLong(idStr.trim());
                int quantity = Integer.parseInt(quantityStr.trim());
                if (quantity <= 0) {
                    req.setAttribute("error", "Số lượng phải > 0!");
                    resp.sendRedirect(req.getContextPath() + "/api/manager/products/searchpaginated");
                    return;
                }
                productService.importStock(id, quantity);
                req.getSession().setAttribute("message", "Nhập hàng thành công " + quantity + " đơn vị!");
                resp.sendRedirect(req.getContextPath() + "/api/manager/products/searchpaginated");

            } else if (uri.contains("/api/manager/products/export")) {
                // Xuất hàng (giảm stock)
                String idStr = req.getParameter("id");
                String quantityStr = req.getParameter("quantity");
                if (idStr == null || idStr.trim().isEmpty() || quantityStr == null || quantityStr.trim().isEmpty()) {
                    req.setAttribute("error", "Thiếu thông tin sản phẩm hoặc số lượng!");
                    resp.sendRedirect(req.getContextPath() + "/api/manager/products/searchpaginated");
                    return;
                }
                Long id = Long.parseLong(idStr.trim());
                int quantity = Integer.parseInt(quantityStr.trim());
                if (quantity <= 0) {
                    req.setAttribute("error", "Số lượng phải > 0!");
                    resp.sendRedirect(req.getContextPath() + "/api/manager/products/searchpaginated");
                    return;
                }
                productService.exportStock(id, quantity);
                req.getSession().setAttribute("message", "Xuất hàng thành công " + quantity + " đơn vị!");
                resp.sendRedirect(req.getContextPath() + "/api/manager/products/searchpaginated");
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Dữ liệu không hợp lệ!");
            resp.sendRedirect(req.getContextPath() + "/api/manager/products/searchpaginated");
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/api/manager/products/searchpaginated");
        }
    }
}