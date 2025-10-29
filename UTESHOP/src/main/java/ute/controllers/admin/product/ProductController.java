package ute.controllers.admin.product;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
import ute.utils.Constant;
import ute.utils.FileStorage;
import ute.service.admin.Impl.BrandServiceImpl;
import ute.service.admin.inter.BrandService;

@MultipartConfig(
        fileSizeThreshold = 10240,    // 10KB
        maxFileSize = 10485760,       // 10MB
        maxRequestSize = 20971520     // 20MB
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
            String[] columns = {"ID", "Tên sản phẩm", "Mô tả", "Giá", "Tồn kho", "Đã bán", "Đánh giá", "Danh mục", "Thương hiệu", "Ngày nhập"};
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
                row.createCell(3).setCellValue(product.getUnitPrice() != null ? product.getUnitPrice().doubleValue() : 0);
                row.createCell(4).setCellValue(product.getStockQuantity() != null ? product.getStockQuantity().intValue() : 0);
                row.createCell(5).setCellValue(product.getSoldCount() != null ? product.getSoldCount().longValue() : 0);
                row.createCell(6).setCellValue(product.getReviewCount() + " (" + product.getRating() + "/5)");
                row.createCell(7).setCellValue(product.getCategory() != null ? product.getCategory().getCategoryName() : "N/A");
                row.createCell(8).setCellValue(product.getBrand() != null ? product.getBrand().getBrandName() : "N/A");
                row.createCell(9).setCellValue(product.getImportDate() != null ? product.getImportDate().toString() : "");
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
            return;  // Kết thúc response ở đây
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
            // FIX: Thêm loadDropdowns để dropdown có data khi mở form
            loadDropdowns(req);

            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    Long id = Long.parseLong(idStr);
                    Product product = productService.findById(id);
                    if (product != null) {
                        req.setAttribute("product", product);
                    } else {
                        req.setAttribute("error", "Sản phẩm không tồn tại!");
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "ID không hợp lệ!");
                }
            }
            // Nếu có error, vẫn forward để hiển thị
            req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
        } else if (uri.contains("/api/admin/products/view")) {
            String idStr = req.getParameter("id");
            try {
                Product product = productService.findById(Long.parseLong(idStr));
                req.setAttribute("product", product);
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID không hợp lệ!");
            }
            req.getRequestDispatcher("/WEB-INF/views/admin/products/view.jsp").forward(req, resp);
        } else if (uri.contains("/admin/products/delete")) {
            String idStr = req.getParameter("id");
            try {
                productService.delete(Long.parseLong(idStr));
            } catch (NumberFormatException e) {
                // Log error nếu cần, nhưng vẫn redirect
            }
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
                try {
                    id = Long.parseLong(idStr);
                    product = productService.findById(id);
                    if (product == null) {
                        req.setAttribute("error", "Sản phẩm không tồn tại!");
                        loadDropdowns(req);
                        req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                        return;
                    }
                    tempProduct = product;
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "ID không hợp lệ!");
                    loadDropdowns(req);
                    req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                    return;
                }
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

            Long categoryId;
            try {
                categoryId = Long.parseLong(categoryIdStr.trim());
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID danh mục không hợp lệ!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }
            Categories category = categoriesService.findById(categoryId);
            if (category == null) {
                req.setAttribute("error", "Danh mục không tồn tại!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            Long brandId;
            try {
                brandId = Long.parseLong(brandIdStr.trim());
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID thương hiệu không hợp lệ!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }
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

            // Handle xóa ảnh cũ (chỉ khi edit) - CHỈ XÓA TRONG DB (thêm try-catch để tránh 500 nếu method chưa ready)
            if (id != null && product != null) {  // Chỉ edit
                String[] deletedImageIdStrs = req.getParameterValues("deletedImageIds");
                if (deletedImageIdStrs != null && deletedImageIdStrs.length > 0) {
                    List<Long> deletedImageIds = Arrays.stream(deletedImageIdStrs)
                            .map(Long::parseLong)
                            .collect(Collectors.toList());

                    // Xóa entity Image trong DB (không xóa file vật lý)
                    for (Long imageId : deletedImageIds) {
                        try {
                            Image imageToDelete = productService.findImageById(imageId);
                            if (imageToDelete != null && imageToDelete.getProduct().getProductID().equals(id)) {
                                productService.deleteImage(imageToDelete);
                            }
                        } catch (Exception e) {
                            // Log lỗi xóa ảnh (không crash toàn bộ), có thể bỏ qua nếu method chưa implement
                            System.err.println("Lỗi xóa ảnh ID " + imageId + ": " + e.getMessage());
                        }
                    }

                    // Reload product sau xóa để lấy images mới nhất
                    product = productService.findById(id);
                    if (product.getImages() == null) {
                        product.setImages(new ArrayList<>());
                    }
                }
            }

            // Handle file upload - HỖ TRỢ NHIỀU ẢNH MỚI (không unique tên file, dùng tên gốc sau sanitize)
            Collection<Part> imageParts = req.getParts().stream()
                    .filter(part -> "images".equals(part.getName()))  // Lọc parts tên "images"
                    .collect(Collectors.toList());

            boolean fileUploadSuccess = true;  // Giả sử thành công ban đầu
            List<Image> newImages = new ArrayList<>();

            if (!imageParts.isEmpty()) {
                FileStorage productStorage = new FileStorage(req.getServletContext(), Constant.UPLOAD_DIR_PRODUCT);

                for (Part filePart : imageParts) {
                    if (filePart == null || filePart.getSize() == 0)
                        continue; // Bỏ qua file rỗng

                    String submittedFileName = filePart.getSubmittedFileName();
                    if (submittedFileName == null || submittedFileName.isEmpty())
                        continue; // Bỏ qua file rỗng

                    // Validate type và size
                    String contentType = filePart.getContentType();
                    if (!contentType.startsWith("image/")) {
                        req.setAttribute("error", "Chỉ chấp nhận file hình ảnh!");
                        loadDropdowns(req);
                        req.setAttribute("product", tempProduct);
                        req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                        return;
                    }
                    if (filePart.getSize() > 5 * 1024 * 1024) {
                        req.setAttribute("error", "File quá lớn (tối đa 5MB)!");
                        loadDropdowns(req);
                        req.setAttribute("product", tempProduct);
                        req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                        return;
                    }
                    Image img = new Image();
                    img.setDirImage(productStorage.save(filePart));
                    img.setProduct(product);
                    newImages.add(img);
                }
            }

            // Thêm ảnh mới vào product (sẽ persist trong service)
            if (fileUploadSuccess && !newImages.isEmpty()) {
                if (product.getImages() == null) {
                    product.setImages(new ArrayList<>());
                }
                product.getImages().addAll(newImages);
            }

            // Check for duplicate product name (exact match)
            List<Product> existingProducts = productService.findByName(productName.trim());
            boolean isDuplicate = false;
            for (Product existing : existingProducts) {
                if (existing.getProductName().equals(productName.trim()) && !Objects.equals(existing.getProductID(), id)) {
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
                productService.update(product);  // Service sẽ persist images
                message = "Product is Edited!";
            } else {
                productService.insert(product);  // Service sẽ persist images
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