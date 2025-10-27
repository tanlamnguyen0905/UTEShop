package ute.controllers.admin.banner;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import ute.dto.BannerDTO;
import ute.entities.Banner;
import ute.service.impl.BannerServiceImpl;
import ute.service.inter.BannerService;
import ute.utils.Constant;

@WebServlet(urlPatterns = { "/api/admin/banner/*" })
@MultipartConfig(fileSizeThreshold = 10240, maxFileSize = 10485760, maxRequestSize = 20971520)
public class BannerController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final BannerService bannerService = new BannerServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo(); // e.g. /view, /delete, /saveOrUpdate
        if (path == null || path.equals("/")) {
            handleList(req, resp);
        } else if (path.equals("/view")) {
            handleView(req, resp);
        } else if (path.equals("/saveOrUpdate")) {
            handleAddOrEdit(req, resp);
        } else if (path.equals("/delete")) {
            handleDelete(req, resp);
        } else {
            handleList(req, resp);
        }
    }

    private void handleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = parseInt(req.getParameter("page"), 1);
        int size = parseInt(req.getParameter("size"), 6);
        String search = req.getParameter("searchKeyword");
        boolean isSearch = search != null && !search.trim().isEmpty();

        int first = (page - 1) * size;
        List<Banner> bannerList = isSearch
                ? bannerService.findByNamePaginated(search.trim(), first, size)
                : bannerService.findPage(page, size);
        int totalBanner = isSearch
                ? (int) bannerService.countByName(search)
                : (int) bannerService.count();
        System.out.println("kichs thuoc cuar danh sach banner" + bannerList.size());
        List<BannerDTO> bannerListDTO = bannerService.MapToBannerDTO(bannerList);
        req.setAttribute("bannerList", bannerListDTO);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", Math.ceil((double) totalBanner / size));
        req.setAttribute("searchKeyword", search);

        req.getRequestDispatcher("/WEB-INF/views/admin/banner/searchpaginated.jsp").forward(req, resp);
    }

    private void handleView(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");

        if (idStr == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID banner");
            return;
        }
        Banner banner = bannerService.findById(Long.parseLong(idStr));
        if (banner == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Banner không tồn tại");
            return;
        }
        System.out.println("banner có giá trị -----------------------------------------------");
        BannerDTO dto = BannerDTO.fromEntity(banner);
        System.out.println("DTO  -----------------------------------------------DTO");
        req.setAttribute("banner", dto);
        System.out.println("finall DTo -----------------------------------------------DTO");
        req.getRequestDispatcher("/WEB-INF/views/admin/banner/view.jsp").forward(req, resp);
    }

    private void handleAddOrEdit(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            Banner banner = bannerService.findById(Long.parseLong(idStr));
            req.setAttribute("banner", banner);
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/banner/addOrEdit.jsp").forward(req, resp);
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            bannerService.delete(Long.parseLong(idStr));
        }
        resp.sendRedirect(req.getContextPath() + "/api/admin/banner");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if ("/saveOrUpdate".equals(path)) {
            saveOrUpdate(req, resp);
        } else {
            handleList(req, resp);
        }
    }

    private void saveOrUpdate(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String idStr = req.getParameter("id");
        String bannerName = req.getParameter("bannerName");
        String description = req.getParameter("description");

        Long id = (idStr != null && !idStr.isEmpty()) ? Long.parseLong(idStr) : null;

        // Nếu là edit → lấy banner từ DB, nếu là add → tạo mới
        Banner banner = (id != null) ? bannerService.findById(id) : new Banner();
        boolean isEdit = (banner.getBannerID() != null);

        // ⚠️ Kiểm tra tên
        if (bannerName == null || bannerName.trim().isEmpty()) {
            req.setAttribute("error", "Tên banner không được để trống!");
            req.setAttribute("banner", banner);
            req.getRequestDispatcher("/WEB-INF/views/admin/banner/addOrEdit.jsp").forward(req, resp);
            return;
        }

        // ⚠️ Kiểm tra trùng tên
        Banner existing = bannerService.findByNameExact(bannerName.trim());
        if (existing != null && !Objects.equals(existing.getBannerID(), id)) {
            req.setAttribute("error", "Tên banner đã tồn tại, vui lòng chọn tên khác!");
            req.setAttribute("banner", banner);
            req.getRequestDispatcher("/WEB-INF/views/admin/banner/addOrEdit.jsp").forward(req, resp);
            return;
        }

        // Gán thông tin cơ bản
        banner.setBannerName(bannerName.trim());
        banner.setDescription(description);

        // 🖼️ Xử lý upload ảnh
        Part filePart = req.getPart("image");
        if (filePart != null && filePart.getSize() > 0) {
            try {
                String uploadDir = Constant.Dir;
                File dir = new File(uploadDir);
                if (!dir.exists())
                    dir.mkdirs();

                // Tên file an toàn, tránh ký tự đặc biệt
                String fileName = System.currentTimeMillis() + "_" +
                        filePart.getSubmittedFileName().replaceAll("[^a-zA-Z0-9.]", "_");

                filePart.write(uploadDir + File.separator + fileName);
                banner.setBannerImage(fileName);

                System.out.println("✅ Ảnh đã upload: " + fileName);
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("error", "Lỗi upload ảnh: " + e.getMessage());
                req.setAttribute("banner", banner);
                req.getRequestDispatcher("/WEB-INF/views/admin/banner/addOrEdit.jsp").forward(req, resp);
                return;
            }
        } else if (!isEdit) {
            // Nếu là banner mới mà không upload ảnh
            banner.setBannerImage("images/banner/default.png");
        }

        // 💾 Lưu hoặc cập nhật banner
        try {
            if (isEdit) {
                bannerService.update(banner);
                req.getSession().setAttribute("message", "Cập nhật banner thành công!");
            } else {
                bannerService.save(banner);
                req.getSession().setAttribute("message", "Thêm banner mới thành công!");
            }

            resp.sendRedirect(req.getContextPath() + "/api/admin/banner");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi khi lưu banner: " + e.getMessage());
            req.setAttribute("banner", banner);
            req.getRequestDispatcher("/WEB-INF/views/admin/banner/addOrEdit.jsp").forward(req, resp);
        }
    }

    private int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }
}
