package ute.controllers.admin.brand;

import java.io.File;
import java.io.IOException;
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
import ute.entities.Brand;
import ute.service.admin.Impl.BrandServiceImpl;
import ute.service.admin.inter.BrandService;
import ute.utils.Constant;

@MultipartConfig(fileSizeThreshold = 10240, // 10KB
        maxFileSize = 10485760, // 10MB
        maxRequestSize = 20971520 // 20MB
)

@WebServlet(urlPatterns = { "/api/admin/brands/searchpaginated", "/api/admin/brands/saveOrUpdate",
        "/api/admin/brands/delete", "/api/admin/brands/view" })
public class BrandController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private BrandService brandService = new BrandServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();

        if (uri.contains("/api/admin/brands/searchpaginated")) {
            int page = 1;
            int size = 6;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
            }
            if (req.getParameter("size") != null) {
                size = Integer.parseInt(req.getParameter("size"));
            }
            String searchKeyword = req.getParameter("searchKeyword");
            List<Brand> brandList;
            long totalBrands;
            if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
                brandList = brandService.findByNamePaginated(searchKeyword.trim(), (page - 1) * size, size);
                totalBrands = brandService.countByName(searchKeyword.trim());
            } else {
                brandList = brandService.findPage(page, size);
                totalBrands = (int) brandService.count();
            }
            int totalPages = (int) Math.ceil((double) totalBrands / size);
            req.setAttribute("brandList", brandList);
            req.setAttribute("totalBrands", totalBrands);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("size", size);
            req.setAttribute("searchKeyword", searchKeyword);
            req.getRequestDispatcher("/WEB-INF/views/admin/brands/searchpaginated.jsp").forward(req, resp);

        } else if (uri.contains("/api/admin/brands/saveOrUpdate")) {
            String idStr = req.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    Long id = Long.parseLong(idStr);
                    Brand brand = brandService.findById(id);
                    if (brand != null) {
                        req.setAttribute("brand", brand);
                    } else {
                        req.setAttribute("error", "Thương hiệu không tồn tại!");
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "ID không hợp lệ!");
                }
            }
            req.getRequestDispatcher("/WEB-INF/views/admin/brands/addOrEdit.jsp").forward(req, resp);
        } else if (uri.contains("/api/admin/brands/view")) {
            String idStr = req.getParameter("id");
            try {
                Brand brand = brandService.findById(Long.parseLong(idStr));
                req.setAttribute("brand", brand);
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID không hợp lệ!");
            }
            req.getRequestDispatcher("/WEB-INF/views/admin/brands/view.jsp").forward(req, resp);
        } else if (uri.contains("/api/admin/brands/delete")) {
            String idStr = req.getParameter("id");
            try {
                brandService.delete(Long.parseLong(idStr));
            } catch (NumberFormatException e) {
                // Log error nếu cần
            }
            resp.sendRedirect(req.getContextPath() + "/api/admin/brands/searchpaginated");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (req.getRequestURI().contains("/api/admin/brands/saveOrUpdate")) {
            String idStr = req.getParameter("id");
            String brandName = req.getParameter("brandName");
            String description = req.getParameter("description");
            String deleteLogo = req.getParameter("deleteLogo"); // Checkbox xóa logo cũ

            Long id = null;
            Brand tempBrand = new Brand();
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    id = Long.parseLong(idStr);
                    tempBrand = brandService.findById(id);
                    if (tempBrand == null) {
                        req.setAttribute("error", "Thương hiệu không tồn tại!");
                        req.getRequestDispatcher("/WEB-INF/views/admin/brands/addOrEdit.jsp").forward(req, resp);
                        return;
                    }
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "ID không hợp lệ!");
                    req.getRequestDispatcher("/WEB-INF/views/admin/brands/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            }

            if (brandName == null || brandName.trim().isEmpty()) {
                req.setAttribute("error", "Tên thương hiệu không được để trống!");
                req.setAttribute("brand", tempBrand);
                req.getRequestDispatcher("/WEB-INF/views/admin/brands/addOrEdit.jsp").forward(req, resp);
                return;
            }

            // Check duplicate name
            List<Brand> existingBrands = brandService.findByName(brandName.trim());
            boolean isDuplicate = false;
            for (Brand existing : existingBrands) {
                if (existing.getBrandName().equals(brandName.trim()) && !Objects.equals(existing.getBrandID(), id)) {
                    isDuplicate = true;
                    break;
                }
            }
            if (isDuplicate) {
                req.setAttribute("error", "Tên thương hiệu đã tồn tại!");
                req.setAttribute("brand", tempBrand);
                req.getRequestDispatcher("/WEB-INF/views/admin/brands/addOrEdit.jsp").forward(req, resp);
                return;
            }

            Brand brand = (id != null) ? tempBrand : new Brand();
            brand.setBrandName(brandName.trim());
            brand.setDescription(description != null ? description.trim() : null);

            // Handle xóa logo cũ (nếu edit và checkbox checked)
            if (id != null && "on".equals(deleteLogo)) {
                brand.setBrandLogo(null); // Chỉ set null trong DB, không xóa file
            }

            // Handle upload logo mới (1 file)
            Part logoPart = req.getPart("brandLogo");
            boolean uploadSuccess = true;
            if (logoPart != null && logoPart.getSize() > 0) {

                try {
                    // String uploadPath = webAppRoot + File.separator + "images" + File.separator +
                    // "brands";
                    String uploadPath = System.currentTimeMillis() + "_" +
                            logoPart.getSubmittedFileName().replaceAll("[^a-zA-Z0-9.]", "_");

                    String uploadDir = Constant.Dir;
                    File dir = new File(uploadDir);
                    if (!dir.exists())
                        dir.mkdirs();
                    logoPart.write(uploadDir + File.separator + uploadPath);
                    brand.setBrandLogo(uploadPath);
                    brand.setBrandLogo(uploadPath);

                } catch (Exception e) {
                    e.printStackTrace();
                    req.getSession().setAttribute("error", "Lỗi khi lưu banner: " + e.getMessage());
                    req.getRequestDispatcher("/WEB-INF/views/admin/banner/addOrEdit.jsp").forward(req, resp);
                }

            }

            req.getSession().setAttribute("message", "Thành công");
            resp.sendRedirect(req.getContextPath() + "/api/admin/brands/searchpaginated");
        }
    }
}