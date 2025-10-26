package ute.controllers.admin.product;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ute.entities.Product;
import ute.entities.Categories;
import ute.entities.Brand;
import ute.entities.Image;
import ute.service.admin.Impl.ProductServiceImpl;
import ute.service.admin.inter.ProductService;
import ute.service.impl.CategoriesServiceImpl;
import ute.service.inter.CategoriesService;
import ute.service.admin.Impl.BrandServiceImpl;
import ute.service.admin.inter.BrandService;

@MultipartConfig(fileSizeThreshold = 10240, // 10KB
        maxFileSize = 10485760, // 10MB
        maxRequestSize = 20971520 // 20MB
)
@WebServlet(urlPatterns = { "/api/admin/products/searchpaginated", "/api/admin/products/saveOrUpdate",
        "/api/admin/products/delete", "/api/admin/products/view", "/api/admin/products/export" })
public class ProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductService productService = new ProductServiceImpl();
    private CategoriesService categoriesService = new CategoriesServiceImpl();
    private BrandService brandService = new BrandServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();

        if (uri.contains("/api/admin/products/export")) {
            // Xuất file Excel cho tất cả sản phẩm (hoặc filtered nếu cần)
            String searchKeyword = req.getParameter("searchKeyword");
            List<Product> products;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                products = productService.findByName(searchKeyword.trim());
            } else {
                products = productService.findAll();
            }

            // Tạo workbook Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Danh sách sản phẩm");

            // Tạo header row
            Row headerRow = sheet.createRow(0);
            String[] columns = { "ID", "Tên sản phẩm", "Mô tả", "Giá", "Tồn kho", "Đã bán", "Đánh giá", "Danh mục",
                    "Thương hiệu", "Ngày nhập" };
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
                CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cell.setCellStyle(headerStyle);
            }

            // Đổ dữ liệu
            int rowNum = 1;
            for (Product product : products) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(product.getProductID());
                row.createCell(1).setCellValue(product.getProductName());
                row.createCell(2).setCellValue(product.getDescribe() != null ? product.getDescribe() : "");
                row.createCell(3)
                        .setCellValue(product.getUnitPrice() != null ? product.getUnitPrice().doubleValue() : 0);
                row.createCell(4)
                        .setCellValue(product.getStockQuantity() != null ? product.getStockQuantity().intValue() : 0);
                row.createCell(5).setCellValue(product.getSoldCount() != null ? product.getSoldCount().longValue() : 0);
                row.createCell(6).setCellValue(product.getReviewCount() + " (" + product.getRating() + "/5)");
                row.createCell(7)
                        .setCellValue(product.getCategory() != null ? product.getCategory().getCategoryName() : "N/A");
                row.createCell(8).setCellValue(product.getBrand() != null ? product.getBrand().getBrandName() : "N/A");
                row.createCell(9)
                        .setCellValue(product.getImportDate() != null ? product.getImportDate().toString() : "");
            }

            // Auto-size columns
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Set response headers cho download
            String fileName = "san_pham_" + System.currentTimeMillis() + ".xlsx";
            resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Expires", "0");

            // Write to output stream
            try (OutputStream out = resp.getOutputStream()) {
                workbook.write(out);
            }
            return; // Kết thúc response ở đây
        }

        if (uri.contains("/api/admin/products/searchpaginated")) {

            int page = 1;
            int size = 6;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
            }
            if (req.getParameter("size") != null) {
                size = Integer.parseInt(req.getParameter("size"));
            }
            String searchKeyword = req.getParameter("searchKeyword");
            List<Product> productList;
            long totalProducts;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                productList = productService.findByNamePaginated(searchKeyword.trim(), (page - 1) * size, size);
                totalProducts = productService.countByName(searchKeyword.trim());
            } else {
                productList = productService.findPage(page, size);
                totalProducts = (int) productService.count();
            }
            int totalPages = (int) Math.ceil((double) totalProducts / size);
            req.setAttribute("productList", productList);
            req.setAttribute("totalProducts", totalProducts);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("size", size);
            req.setAttribute("searchKeyword", searchKeyword);
            req.getRequestDispatcher("/WEB-INF/views/admin/products/searchpaginated.jsp").forward(req, resp);

        } else if (uri.contains("/api/admin/products/saveOrUpdate")) {
            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                Product product = productService.findById(Long.parseLong(idStr));
                req.setAttribute("product", product);
            }
            req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
        } else if (uri.contains("/api/admin/products/view")) {
            String idStr = req.getParameter("id");
            Product product = productService.findById(Long.parseLong(idStr));
            req.setAttribute("product", product);
            req.getRequestDispatcher("/WEB-INF/views/admin/products/view.jsp").forward(req, resp);
        } else if (uri.contains("/admin/products/delete")) {
            String idStr = req.getParameter("id");
            productService.delete(Long.parseLong(idStr));
            resp.sendRedirect(req.getContextPath() + "/api/admin/products/searchpaginated");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();

        if (uri.contains("/admin/products/saveOrUpdate")) {
            Product product = new Product();
            String idStr = req.getParameter("id");
            String productName = req.getParameter("productName");
            String describe = req.getParameter("describe");
            String unitPriceStr = req.getParameter("unitPrice");
            String stockQuantityStr = req.getParameter("stockQuantity");
            String categoryIdStr = req.getParameter("categoryId");
            String brandIdStr = req.getParameter("brandId");

            Long id = null;
            Product tempProduct = new Product();
            if (idStr != null && !idStr.isEmpty()) {
                id = Long.parseLong(idStr);
                product = productService.findById(id);
                if (product == null) {
                    req.setAttribute("error", "Sản phẩm không tồn tại!");
                    loadDropdowns(req);
                    req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                    return;
                }
                tempProduct = product;
            }

            if (productName == null || productName.trim().isEmpty()) {
                req.setAttribute("error", "Tên sản phẩm không được để trống!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            Double unitPrice;
            try {
                unitPrice = Double.parseDouble(unitPriceStr);
            } catch (NumberFormatException e) {
                req.setAttribute("error", "Giá sản phẩm không hợp lệ!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            Integer stockQuantity;
            try {
                stockQuantity = Integer.parseInt(stockQuantityStr);
            } catch (NumberFormatException e) {
                req.setAttribute("error", "Số lượng tồn kho không hợp lệ!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            if (unitPrice <= 0) {
                req.setAttribute("error", "Giá sản phẩm phải lớn hơn 0!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            if (stockQuantity < 0) {
                req.setAttribute("error", "Số lượng tồn kho không được âm!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            Long categoryId = Long.parseLong(categoryIdStr.trim());
            Categories category = categoriesService.findById(categoryId);
            if (category == null) {
                req.setAttribute("error", "Danh mục không tồn tại!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            Long brandId = Long.parseLong(brandIdStr.trim());
            Brand brand = brandService.findById(brandId);
            if (brand == null) {
                req.setAttribute("error", "Thương hiệu không tồn tại!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            product.setProductName(productName.trim());
            product.setDescribe(describe != null ? describe.trim() : null);
            product.setUnitPrice(unitPrice);
            product.setStockQuantity(stockQuantity);
            product.setCategory(category);
            product.setBrand(brand);

            // Handle file upload
            Part filePart = req.getPart("image");
            boolean fileUploadSuccess = false;
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = filePart.getSubmittedFileName();
                if (fileName != null && !fileName.trim().isEmpty()) {
                    // Validate file type (basic check for images)
                    String contentType = filePart.getContentType();
                    if (contentType != null && contentType.startsWith("image/")) {
                        // Get webapp root path for reliable file storage
                        String webAppRoot = getServletContext().getRealPath("/");
                        if (webAppRoot == null) {
                            // Fallback for environments where getRealPath returns null (e.g., some cloud
                            // setups)
                            req.setAttribute("error",
                                    "Không thể lưu file do môi trường triển khai. Vui lòng liên hệ admin.");
                            loadDropdowns(req);
                            req.setAttribute("product", tempProduct);
                            req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                            return;
                        }

                        // Ensure uploads directory exists under assets/images/products
                        String uploadPath = webAppRoot + File.separator + "assets" + File.separator + "images"
                                + File.separator + "products";
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                            if (!uploadDir.mkdirs()) {
                                req.setAttribute("error", "Không thể tạo thư mục lưu trữ. Kiểm tra quyền truy cập.");
                                loadDropdowns(req);
                                req.setAttribute("product", tempProduct);
                                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req,
                                        resp);
                                return;
                            }
                        }

                        // Generate unique filename to avoid conflicts
                        String fileExtension = "";
                        int lastDotIndex = fileName.lastIndexOf(".");
                        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
                            fileExtension = fileName.substring(lastDotIndex);
                        }
                        String uniqueFileName = System.currentTimeMillis() + "_"
                                + fileName.replaceAll("[^a-zA-Z0-9.-]", "_"); // Sanitize filename
                        String filePath = uploadPath + File.separator + uniqueFileName;

                        // Save the file
                        try {
                            filePart.write(filePath);
                            // Verify file was written successfully
                            File savedFile = new File(filePath);
                            if (savedFile.exists() && savedFile.length() > 0) {
                                fileUploadSuccess = true;
                                // Create Image entity
                                Image image = new Image();
                                image.setDirImage("images/products/" + uniqueFileName);
                                image.setProduct(product);
                                // Add to product's images list
                                if (product.getImages() == null) {
                                    product.setImages(new ArrayList<>());
                                }
                                product.getImages().add(image);
                            } else {
                                // Clean up empty file
                                if (savedFile.exists()) {
                                    savedFile.delete();
                                }
                                req.setAttribute("error", "Lỗi khi lưu file ảnh. Vui lòng thử lại.");
                                loadDropdowns(req);
                                req.setAttribute("product", tempProduct);
                                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req,
                                        resp);
                                return;
                            }
                        } catch (IOException e) {
                            // Clean up on error
                            File errorFile = new File(filePath);
                            if (errorFile.exists()) {
                                errorFile.delete();
                            }
                            req.setAttribute("error", "Lỗi IO khi lưu file: " + e.getMessage());
                            loadDropdowns(req);
                            req.setAttribute("product", tempProduct);
                            req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                            return;
                        }
                    } else {
                        req.setAttribute("error", "Chỉ chấp nhận file ảnh (image/*)!");
                        loadDropdowns(req);
                        req.setAttribute("product", tempProduct);
                        req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                        return;
                    }
                } else {
                    req.setAttribute("error", "Tên file không hợp lệ!");
                    loadDropdowns(req);
                    req.setAttribute("product", tempProduct);
                    req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            }

            // Check for duplicate product name (exact match)
            List<Product> existingProducts = productService.findByName(productName.trim());
            boolean isDuplicate = false;
            for (Product existing : existingProducts) {
                if (existing.getProductName().equals(productName.trim())
                        && !Objects.equals(existing.getProductID(), id)) {
                    isDuplicate = true;
                    break;
                }
            }
            if (isDuplicate) {
                req.setAttribute("error", "Tên sản phẩm đã tồn tại! Vui lòng nhập tên khác!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            String message;
            if (id != null) {
                productService.update(product);
                message = "Product is Edited!";
            } else {
                productService.insert(product);
                message = "Product is Saved!";
            }

            req.getSession().setAttribute("message", message);
            resp.sendRedirect(req.getContextPath() + "/api/admin/products/searchpaginated");
        }
    }

    private void loadDropdowns(HttpServletRequest req) {
        List<Categories> categoriesList = categoriesService.findAll();
        List<Brand> brandsList = brandService.findAll();
        req.setAttribute("categoriesList", categoriesList);
        req.setAttribute("brandsList", brandsList);
    }
}