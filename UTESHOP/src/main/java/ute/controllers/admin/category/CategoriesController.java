package ute.controllers.admin.category;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import ute.entities.Categories;
import ute.service.inter.CategoriesService;
import ute.service.impl.CategoriesServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/admin/categories/searchpaginated", "/admin/categories/saveOrUpdate",
        "/admin/categories/delete", "/admin/categories/view" })
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

            // Tính offset (vị trí bắt đầu)
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
                // dang o che do edit -> nguoc lai la add
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

            String idStr = req.getParameter("id");
            String categoryName = req.getParameter("categoryName");
            String description = req.getParameter("description");
            String image = req.getParameter("image"); // Note: For file upload, need multipart handling

            // Nếu có id -> update
            Long id = null;
            if (idStr != null && !idStr.isEmpty()) {
                id = Long.parseLong(idStr);
                category = categoriesService.findById(id);
            }

            // Gán giá trị tạm thời để giữ lại form nếu lỗi
            category.setCategoryName(categoryName);
            category.setDescription(description);
            category.setImage(image); // For now, assume text; for file, need upload logic

            // Kiểm tra tên trùng
            Categories existing = categoriesService.findByNameExact(categoryName);
            if (existing != null && !Objects.equals(existing.getCategoryID(), id)) {
                req.setAttribute("error", "Tên danh mục đã tồn tại! Vui lòng nhập tên khác!");
            }

            // forward lại form nếu lỗi
            if (req.getAttribute("error") != null) {
                req.setAttribute("category", category);
                req.getRequestDispatcher("/views/admin/categories/addOrEdit.jsp").forward(req, resp);
                return;
            }

            //Lưu danh mục vào db nếu không lỗi
            String message;
            if (idStr != null && !idStr.isEmpty()) {
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