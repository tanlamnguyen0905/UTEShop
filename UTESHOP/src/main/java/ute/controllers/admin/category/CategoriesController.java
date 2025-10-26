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

@WebServlet(urlPatterns = { "/api/admin/categories/searchpaginated", "/api/admin/categories/saveOrUpdate",
        "/api/admin/categories/delete", "/api/admin/categories/view" })
@MultipartConfig(fileSizeThreshold = 10240, // 10KB
        maxFileSize = 10485760, // 10MB
        maxRequestSize = 20971520 // 20MB
)
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
                Categories category = categoriesService.findById(Long.parseLong(idStr));
                req.setAttribute("category", category);
            }
            req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
        } else if (uri.contains("/api/admin/categories/view")) {
            String idStr = req.getParameter("id");
            Categories category = categoriesService.findById(Long.parseLong(idStr));
            req.setAttribute("category", category);
            req.getRequestDispatcher("/views//admin/categories/view.jsp").forward(req, resp);
        } else if (uri.contains("delete")) {
            String idStr = req.getParameter("id");
            categoriesService.delete(Long.parseLong(idStr));
            resp.sendRedirect(req.getContextPath() + "/api/admin/categories/searchpaginated");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        if (uri.contains("/api/admin/categories/saveOrUpdate")) {
            Categories category = new Categories();

            // Get text parameters from the multipart form
            String idStr = req.getParameter("id");
            String categoryName = req.getParameter("categoryName");
            String description = req.getParameter("description");

            Long id = null;
            if (idStr != null && !idStr.isEmpty()) {
                id = Long.parseLong(idStr);
                category = categoriesService.findById(id);
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

            // Handle file upload
            Part filePart = req.getPart("image"); // Get the file part
            String image = null;
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
                            webAppRoot = System.getProperty("java.io.tmpdir");
                            req.setAttribute("error",
                                    "Không thể lưu file do môi trường triển khai. Vui lòng liên hệ admin.");
                            req.setAttribute("category", category);
                            req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req,
                                    resp);
                            return;
                        }

                        // Ensure uploads directory exists
                        String uploadPath = webAppRoot + File.separator + "assets" + File.separator + "images"
                                + File.separator + "categories";
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists()) {
                            if (!uploadDir.mkdirs()) {
                                req.setAttribute("error", "Không thể tạo thư mục lưu trữ. Kiểm tra quyền truy cập.");
                                req.setAttribute("category", category);
                                req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req,
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
                                image = "images/categories/" + uniqueFileName; // Store relative path in the database
                            } else {
                                // Clean up empty file
                                if (savedFile.exists()) {
                                    savedFile.delete();
                                }
                                req.setAttribute("error", "Lỗi khi lưu file ảnh. Vui lòng thử lại.");
                                req.setAttribute("category", category);
                                req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req,
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
                            req.setAttribute("category", category);
                            req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req,
                                    resp);
                            return;
                        }
                    } else {
                        req.setAttribute("error", "Chỉ chấp nhận file ảnh (image/*)!");
                        req.setAttribute("category", category);
                        req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                        return;
                    }
                } else {
                    req.setAttribute("error", "Tên file không hợp lệ!");
                    req.setAttribute("category", category);
                    req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            }
            if (!fileUploadSuccess && id != null) {
                // If no new image is uploaded, keep the existing image
                image = category.getImage();
            }

            category.setImage(image);

            // Check for duplicate category name
            Categories existing = categoriesService.findByNameExact(categoryName);
            if (existing != null && !Objects.equals(existing.getCategoryID(), id)) {
                req.setAttribute("error", "Tên danh mục đã tồn tại! Vui lòng nhập tên khác!");
                req.setAttribute("category", category);
                req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                return;
            }

            String message;
            if (id != null) {
                categoriesService.update(category);
                message = "Category is Edited!";
            } else {
                categoriesService.save(category);
                message = "Category is Saved!";
            }

            req.getSession().setAttribute("message", message);
            resp.sendRedirect(req.getContextPath() + "/api/admin/categories/searchpaginated");
        }
    }
}