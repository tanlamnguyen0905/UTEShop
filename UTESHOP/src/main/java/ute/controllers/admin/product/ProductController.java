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
import ute.utils.Constant;
import ute.service.admin.Impl.BrandServiceImpl;
import ute.service.admin.Impl.ImageServiceImpl;
import ute.service.admin.inter.BrandService;
import ute.service.admin.inter.ImageService;

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
        resp.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();

        if (uri.contains("/api/admin/products/saveOrUpdate")) {

            Product product = new Product();
            Product tempProduct = new Product();
            String idStr = req.getParameter("id");
            String productName = req.getParameter("productName");
            String describe = req.getParameter("describe");
            String unitPriceStr = req.getParameter("unitPrice");
            String stockQuantityStr = req.getParameter("stockQuantity");
            String categoryIdStr = req.getParameter("categoryId");
            String brandIdStr = req.getParameter("brandId");

            Long id = null;
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

            // ========== VALIDATION ==========
            if (productName == null || productName.trim().isEmpty()) {
                req.setAttribute("error", "Tên sản phẩm không được để trống!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            double unitPrice;
            try {
                unitPrice = Double.parseDouble(unitPriceStr);
                if (unitPrice <= 0)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                req.setAttribute("error", "Giá sản phẩm phải là số dương!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            int stockQuantity;
            try {
                stockQuantity = Integer.parseInt(stockQuantityStr);
                if (stockQuantity < 0)
                    throw new NumberFormatException();
            } catch (NumberFormatException e) {
                req.setAttribute("error", "Số lượng tồn kho phải là số không âm!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            Long categoryId = Long.parseLong(categoryIdStr);
            Categories category = categoriesService.findById(categoryId);
            if (category == null) {
                req.setAttribute("error", "Danh mục không tồn tại!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            Long brandId = Long.parseLong(brandIdStr);
            Brand brand = brandService.findById(brandId);
            if (brand == null) {
                req.setAttribute("error", "Thương hiệu không tồn tại!");
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            // ========== GÁN GIÁ TRỊ ==========
            product.setProductName(productName.trim());
            product.setDescribe(describe != null ? describe.trim() : null);
            product.setUnitPrice(unitPrice);
            product.setStockQuantity(stockQuantity);
            product.setCategory(category);
            product.setBrand(brand);

            // ========== UPLOAD ẢNH ==========
            // 🖼️ Xử lý upload ảnh sản phẩm
            // ========== UPLOAD ẢNH ==========
            Part filePart = req.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                try {

                    // ✅ Đường dẫn vật lý thật
                    String uploadDir = Constant.Dir;
                    File dir = new File(uploadDir);
                    if (!dir.exists() && !dir.mkdirs()) {
                        req.setAttribute("error", "Không thể tạo thư mục lưu ảnh!");
                        loadDropdowns(req);
                        req.setAttribute("product", tempProduct);
                        req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                        return;
                    }

                    // ✅ Tên file an toàn
                    String originalFileName = filePart.getSubmittedFileName();
                    String safeFileName = System.currentTimeMillis() + "_" +
                            originalFileName.replaceAll("[^a-zA-Z0-9.]", "_");

                    String filePath = uploadDir + File.separator + safeFileName;
                    System.out.println("file lưu ở     " + filePath);
                    filePart.write(filePath);

                    File savedFile = new File(filePath);
                    if (!savedFile.exists() || savedFile.length() == 0) {
                        System.out.println("file lưu kihoong thnafh công     ");
                        req.setAttribute("error", "Lỗi khi lưu file ảnh!");
                        loadDropdowns(req);
                        req.setAttribute("product", tempProduct);
                        req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                        return;
                    }

                    // ✅ Thêm vào danh sách ảnh của sản phẩm
                    Image image = new Image();
                    image.setDirImage(safeFileName);
                    image.setProduct(product);

                    if (product.getImages() == null) {
                        product.setImages(new ArrayList<>());
                    }
                    product.getImages().add(image);

                    System.out.println("✅ Ảnh đã được thêm vào product: " + image.getDirImage());

                } catch (Exception e) {
                    e.printStackTrace();
                    req.setAttribute("error", "Lỗi upload ảnh: " + e.getMessage());
                    loadDropdowns(req);
                    req.setAttribute("product", tempProduct);
                    req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            }

            // ========== KIỂM TRA TRÙNG TÊN ==========
            List<Product> existingProducts = productService.findByName(productName.trim());
            for (Product existing : existingProducts) {
                if (!Objects.equals(existing.getProductID(), id)
                        && existing.getProductName().equalsIgnoreCase(productName.trim())) {
                    req.setAttribute("error", "Tên sản phẩm đã tồn tại!");
                    loadDropdowns(req);
                    req.setAttribute("product", tempProduct);
                    req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            }

            // ========== LƯU VÀ PHẢN HỒI ==========
            try {
                if (id != null) {
                    productService.update(product);
                    req.getSession().setAttribute("message", "Cập nhật sản phẩm thành công!");
                } else {
                    productService.insert(product);
                    req.getSession().setAttribute("message", "Thêm sản phẩm mới thành công!");
                }
                resp.sendRedirect(req.getContextPath() + "/api/admin/products/searchpaginated");
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Lỗi khi lưu sản phẩm: " + e.getMessage());
                loadDropdowns(req);
                req.setAttribute("product", tempProduct);
                req.getRequestDispatcher("/WEB-INF/views/admin/products/addOrEdit.jsp").forward(req, resp);
            }
        }

    }

    private void loadDropdowns(HttpServletRequest req) {
        List<Categories> categoriesList = categoriesService.findAll();
        List<Brand> brandsList = brandService.findAll();
        req.setAttribute("categoriesList", categoriesList);
        req.setAttribute("brandsList", brandsList);
    }
}