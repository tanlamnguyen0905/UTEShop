package ute.controllers.admin.category;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import ute.entities.Categories;
import ute.service.inter.CategoriesService;
import ute.service.impl.CategoriesServiceImpl;

@MultipartConfig(
        fileSizeThreshold = 10240,    // 10KB
        maxFileSize = 10485760,       // 10MB
        maxRequestSize = 20971520     // 20MB
)

@WebServlet(urlPatterns = { "/api/admin/categories/searchpaginated", "/api/admin/categories/saveOrUpdate",
        "/api/admin/categories/delete", "/api/admin/categories/view" })

public class CategoriesController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CategoriesService categoriesService = new CategoriesServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String uri = req.getRequestURI();

        if (uri.contains("/api/admin/categories/searchpaginated")) {

            int page = 1;
            int size = 6;

            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
            }
            if (req.getParameter("size") != null) {
                size = Integer.parseInt(req.getParameter("size"));
            }

            String searchKeyword = req.getParameter("searchKeyword");
            boolean isSearch = false;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                searchKeyword = searchKeyword.trim();
                isSearch = true;
            } else {
                searchKeyword = null;
            }

            int firstResult = (page - 1) * size;

            List<Categories> categoryList;
            int totalCategories;
            if (isSearch) {
                categoryList = categoriesService.findByNamePaginated(searchKeyword, firstResult, size);
                totalCategories = (int) categoriesService.countByName(searchKeyword);
            } else {
                categoryList = categoriesService.findPage(page, size);
                totalCategories = (int) categoriesService.count();
            }
            int totalPages = (int) Math.ceil((double) totalCategories / size);

            req.setAttribute("categoryList", categoryList);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("size", size);
            req.setAttribute("searchKeyword", searchKeyword);

            req.getRequestDispatcher("/WEB-INF/views/admin/categories/searchpaginated.jsp").forward(req, resp);

        } else if (uri.contains("/api/admin/categories/saveOrUpdate")) {
            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    Categories category = categoriesService.findById(Long.parseLong(idStr));
                    req.setAttribute("category", category);
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "ID không hợp lệ!");
                }
            }
            req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
        } else if (uri.contains("/api/admin/categories/view")) {
            String idStr = req.getParameter("id");
            try {
                Categories category = categoriesService.findById(Long.parseLong(idStr));
                req.setAttribute("category", category);
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID không hợp lệ!");
            }
            req.getRequestDispatcher("/WEB-INF/views/admin/categories/view.jsp").forward(req, resp);  // Fix path: bỏ double slash
        } else if (uri.contains("delete")) {
            String idStr = req.getParameter("id");
            try {
                categoriesService.delete(Long.parseLong(idStr));
            } catch (NumberFormatException e) {
                // Log nếu cần, nhưng vẫn redirect
            }
            resp.sendRedirect(req.getContextPath() + "/api/admin/categories/searchpaginated");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");  // FIX: Đảm bảo đọc UTF-8 cho tiếng Việt
        String uri = req.getRequestURI();

        if (uri.contains("/api/admin/categories/saveOrUpdate")) {
            Categories category = new Categories();

            // Get text parameters from the multipart form
            String idStr = req.getParameter("id");
            String categoryName = req.getParameter("categoryName");
            String description = req.getParameter("description");

            Long id = null;
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    id = Long.parseLong(idStr);
                    category = categoriesService.findById(id);
                    if (category == null) {
                        req.setAttribute("error", "Danh mục không tồn tại!");
                        req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                        return;
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "ID không hợp lệ!");
                    req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            }

            // Validate required fields
            if (categoryName == null || categoryName.trim().isEmpty()) {
                req.setAttribute("error", "Tên danh mục không được để trống!");
                req.setAttribute("category", category);
                req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                return;
            }

            category.setCategoryName(categoryName.trim());
            category.setDescription(description != null ? description.trim() : null);

            Part filePart = req.getPart("image"); // Get the file part
            String image = (id != null) ? category.getImage() : null; // Default to existing on update, null for new

            if (filePart != null && filePart.getSize() > 0) {
                String submittedFileName = filePart.getSubmittedFileName();
                if (submittedFileName == null || submittedFileName.isEmpty()) {
                    // No file, skip
                } else {
                    // Sanitize: Extract basename only, reject paths
                    String baseName = submittedFileName;
                    int lastSeparator = baseName.lastIndexOf(File.separatorChar);
                    if (lastSeparator > 0) {
                        baseName = baseName.substring(lastSeparator + 1);
                    }
                    // Remove invalid chars, keep extension (an toàn cho filesystem)
                    baseName = baseName.replaceAll("[^a-zA-Z0-9._-]", "_");

                    // Validate type (basic, only images)
                    String contentType = filePart.getContentType();
                    if (!contentType.startsWith("image/")) {
                        req.setAttribute("error", "Chỉ chấp nhận file hình ảnh!");
                        req.setAttribute("category", category);
                        req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                        return;
                    }

                    if (filePart.getSize() > 5 * 1024 * 1024) {
                        req.setAttribute("error", "File quá lớn (tối đa 5MB)!");
                        req.setAttribute("category", category);
                        req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                        return;
                    }

                    String uploadPath = ute.utils.Constant.Dir + File.separator + "images" + File.separator + "categories";
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                        req.setAttribute("error", "Không thể tạo thư mục images/categories!");
                        req.setAttribute("category", category);
                        req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                        return;
                    }

                    String fileName = baseName;
                    String filePath = uploadPath + File.separator + fileName;

                    try {
                        filePart.write(filePath);
                        File savedFile = new File(filePath);  // Verify save
                        if (savedFile.exists() && savedFile.length() > 0) {
                            image = "images/categories/" + fileName;
                        } else {
                            req.setAttribute("error", "Lỗi khi lưu file ảnh. Vui lòng thử lại.");
                            req.setAttribute("category", category);
                            req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                            return;
                        }
                    } catch (IOException e) {
                        req.setAttribute("error", "Lỗi khi lưu file: " + e.getMessage());
                        req.setAttribute("category", category);
                        req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                        return;
                    }
                }
            }

            category.setImage(image);

            // Check for duplicate category name
            Categories existing = categoriesService.findByNameExact(categoryName.trim());  // Trim ở đây để khớp
            if (existing != null && !Objects.equals(existing.getCategoryID(), id)) {
                req.setAttribute("error", "Tên danh mục đã tồn tại! Vui lòng nhập tên khác!");
                req.setAttribute("category", category);
                req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                return;
            }

            String message;
            if (id != null) {
                categoriesService.update(category);
                message = "Danh mục đã được cập nhật!";  // Thống nhất tiếng Việt
            } else {
                categoriesService.save(category);
                message = "Danh mục đã được lưu!";
            }

            req.getSession().setAttribute("message", message);
            resp.sendRedirect(req.getContextPath() + "/api/admin/categories/searchpaginated");
        }
    }
}