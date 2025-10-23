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
import ute.service.impl.CategoriesServiceImpl;

@WebServlet(urlPatterns = { "/admin/categories/searchpaginated", "/admin/categories/saveOrUpdate",
        "/admin/categories/delete", "/admin/categories/view" })
@MultipartConfig
public class CategoriesController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CategoriesService categoriesService = new CategoriesServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String uri = req.getRequestURI();

        if (uri.contains("/admin/categories/searchpaginated")) {

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

            req.getRequestDispatcher("/views/admin/categories/searchpaginated.jsp").forward(req, resp);

        } else if (uri.contains("/admin/categories/saveOrUpdate")) {
            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                Categories category = categoriesService.findById(Long.parseLong(idStr));
                req.setAttribute("category", category);
            }
            req.getRequestDispatcher("/views/admin/categories/addOrEdit.jsp").forward(req, resp);
        } else if (uri.contains("/admin/categories/view")) {
            String idStr = req.getParameter("id");
            Categories category = categoriesService.findById(Long.parseLong(idStr));
            req.setAttribute("category", category);
            req.getRequestDispatcher("/views/admin/categories/view.jsp").forward(req, resp);
        } else if (uri.contains("delete")) {
            String idStr = req.getParameter("id");
            categoriesService.delete(Long.parseLong(idStr));
            resp.sendRedirect(req.getContextPath() + "/admin/categories/searchpaginated");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        if (uri.contains("/admin/categories/saveOrUpdate")) {
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
                req.getRequestDispatcher("/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                return;
            }

            category.setCategoryName(categoryName.trim());
            category.setDescription(description != null ? description.trim() : null);

            // Handle file upload
            Part filePart = req.getPart("image"); // Get the file part
            String image = null;
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = filePart.getSubmittedFileName();
                // Save the file to a directory (e.g., /assets/uploads/)
                String uploadPath = getServletContext().getRealPath("/assets/uploads");
                String filePath = uploadPath + "/" + fileName;
                filePart.write(filePath);
                image = "uploads/" + fileName; // Store relative path in the database
            } else if (id != null) {
                // If no new image is uploaded, keep the existing image
                image = category.getImage();
            }

            category.setImage(image);

            // Check for duplicate category name
            Categories existing = categoriesService.findByNameExact(categoryName);
            if (existing != null && !Objects.equals(existing.getCategoryID(), id)) {
                req.setAttribute("error", "Tên danh mục đã tồn tại! Vui lòng nhập tên khác!");
                req.setAttribute("category", category);
                req.getRequestDispatcher("/views/admin/categories/addOrEdit.jsp").forward(req, resp);
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
            resp.sendRedirect(req.getContextPath() + "/admin/categories/searchpaginated");
        }
    }
}