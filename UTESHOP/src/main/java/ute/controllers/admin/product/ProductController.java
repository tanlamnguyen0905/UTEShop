package ute.controllers.admin.product;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import ute.entities.Categories;
import ute.entities.Product;
import ute.service.inter.CategoriesService;
import ute.service.impl.CategoriesServiceImpl;
import ute.service.inter.ProductService;
import ute.service.impl.ProductServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/admin/products/searchpaginated", "/admin/products/saveOrUpdate",
        "/admin/products/delete", "/admin/products/view" })
public class ProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductService productService = new ProductServiceImpl();
    private CategoriesService categoriesService = new CategoriesServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String uri = req.getRequestURI();

        if (uri.contains("searchpaginated")) {

            int page = 1;
            int size = 6;

            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
            }
            if (req.getParameter("size") != null) {
                size = Integer.parseInt(req.getParameter("size"));
            }

            String searchKeyword = req.getParameter("searchKeyword");
            if (searchKeyword != null) {
                searchKeyword = searchKeyword.trim();
            }

            // Tính offset (vị trí bắt đầu)
            int firstResult = (page - 1) * size;

            List<Product> productList = productService.findByNamePaginated(searchKeyword, firstResult, size);

            // Đếm tổng số bản ghi để tính tổng trang
            int totalProducts = (int) productService.countByName(searchKeyword);
            int totalPages = (int) Math.ceil((double) totalProducts / size);

            req.setAttribute("productList", productList);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("size", size);
            req.setAttribute("searchKeyword", searchKeyword);

            req.getRequestDispatcher("/views/admin/products/searchpaginated.jsp").forward(req, resp);

        } else if (uri.contains("saveOrUpdate")) {
            String idStr = req.getParameter("id");
            List<Categories> categoryList = categoriesService.findAll();
            if (idStr != null && !idStr.isEmpty()) {
                // dang o che do edit -> nguoc lai la add
                Product product = productService.findById(Long.parseLong(idStr));
                req.setAttribute("product", product);
            }
            req.setAttribute("categoryList", categoryList);
            req.getRequestDispatcher("/views/admin/products/addOrEdit.jsp").forward(req, resp);
        } else if (uri.contains("view")) {
            String idStr = req.getParameter("id");
            Product product = productService.findById(Long.parseLong(idStr));
            req.setAttribute("product", product);
            req.getRequestDispatcher("/views/admin/products/view.jsp").forward(req, resp);
        } else if (uri.contains("delete")) {
            String idStr = req.getParameter("id");
            productService.delete(Long.parseLong(idStr));
            resp.sendRedirect(req.getContextPath() + "/admin/products/searchpaginated");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String uri = req.getRequestURI();

        if (uri.contains("saveOrUpdate")) {
            Product product = new Product();

            String idStr = req.getParameter("id");
            String productName = req.getParameter("productName");
            String describe = req.getParameter("describe");
            String unitPriceStr = req.getParameter("unitPrice");
            String categoryIdStr = req.getParameter("categoryId");

            // Nếu có id -> update
            Long id = null;
            if (idStr != null && !idStr.isEmpty()) {
                id = Long.parseLong(idStr);
                product = productService.findById(id);
            }


            product.setProductName(productName);
            product.setDescribe(describe);

            if (unitPriceStr != null && !unitPriceStr.isEmpty()) {
                try {
                    product.setUnitPrice(Double.parseDouble(unitPriceStr));
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "Giá tiền không hợp lệ!");
                }
            }

            Categories category = null;
            if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                try {
                    long categoryId = Long.parseLong(categoryIdStr);
                    category = categoriesService.findById(categoryId);
                    product.setCategory(category);
                } catch (Exception e) {
                    req.setAttribute("error", "Danh mục không hợp lệ!");
                }
            } else {
                req.setAttribute("error", "Vui lòng chọn danh mục!");
            }

            // forward lại form nếu lỗi
            if (req.getAttribute("error") != null) {
                List<Categories> categoryList = categoriesService.findAll();
                req.setAttribute("categoryList", categoryList);
                req.setAttribute("product", product);
                req.getRequestDispatcher("/views/admin/products/addOrEdit.jsp").forward(req, resp);
                return;
            }

            //Lưu sản phẩm vào db nếu không lỗi
            String message;
            if (idStr != null && !idStr.isEmpty()) {
                productService.update(product);
                message = "Product is Edited!";
            } else {
                productService.save(product);
                message = "Product is Saved!";
            }

            req.getSession().setAttribute("message", message);
            resp.sendRedirect(req.getContextPath() + "/admin/products/searchpaginated");
        }
    }
}