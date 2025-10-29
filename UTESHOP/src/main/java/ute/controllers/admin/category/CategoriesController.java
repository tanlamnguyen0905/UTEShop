package ute.controllers.admin.category;

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
import ute.utils.Constant;
import ute.utils.FileStorage;
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
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");

            Categories category = new Categories();

            String idStr = req.getParameter("id");
            String categoryName = req.getParameter("categoryName");
            String description = req.getParameter("description");

            Long id = (idStr != null && !idStr.isEmpty()) ? Long.parseLong(idStr) : null;
            if (id != null) {
                category = categoriesService.findById(id);
            }

            // Kiểm tra tên danh mục
            if (categoryName == null || categoryName.trim().isEmpty()) {
                req.setAttribute("error", "Tên danh mục không được để trống!");
                req.setAttribute("category", category);
                req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                return;
            }

            // Kiểm tra trùng tên
            Categories existing = categoriesService.findByNameExact(categoryName.trim());
            if (existing != null && !Objects.equals(existing.getCategoryID(), id)) {
                req.setAttribute("error", "Tên danh mục đã tồn tại! Vui lòng nhập tên khác!");
                req.setAttribute("category", category);
                req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                return;
            }

            // Gán dữ liệu cơ bản
            category.setCategoryName(categoryName.trim());
            category.setDescription(description != null ? description.trim() : null);

            // Xử lý upload ảnh
            Part filePart = req.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                try {
                    FileStorage catStorage = new FileStorage(req.getServletContext(), Constant.UPLOAD_DIR_CATEGORY);
                    String image = catStorage.save(filePart);
                    if (image == null)
                        throw new Exception("Không thể lưu file");
                    category.setImage(image);

                } catch (Exception e) {
                    e.printStackTrace();
                    req.setAttribute("error", "Lỗi upload ảnh: " + e.getMessage());
                    req.setAttribute("category", category);
                    req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            } else if (id != null && category.getImage() != null) {
                // Giữ ảnh cũ khi không upload mới (full path đã lưu)
                category.setImage(category.getImage());
            } else {
                // Ảnh mặc định khi thêm mới (full path nếu logo ở root, hoặc điều chỉnh nếu logo ở assets)
                category.setImage("logo.png");  // Giả sử logo.png ở root; nếu không, dùng "assets/images/logo.png"
            }

            // 💾 Lưu hoặc cập nhật
            try {
                if (id != null) {
                    categoriesService.update(category);
                    req.getSession().setAttribute("message", "Cập nhật danh mục thành công!");
                } else {
                    categoriesService.save(category);
                    req.getSession().setAttribute("message", "Thêm danh mục mới thành công!");
                }
                resp.sendRedirect(req.getContextPath() + "/api/admin/categories/searchpaginated");
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Lỗi khi lưu danh mục: " + e.getMessage());
                req.setAttribute("category", category);
                req.getRequestDispatcher("/WEB-INF/views/admin/categories/addOrEdit.jsp").forward(req, resp);
            }
        }
    }

}