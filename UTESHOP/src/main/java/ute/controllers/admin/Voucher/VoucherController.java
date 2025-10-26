package ute.controllers.admin.Voucher;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.entities.Voucher;
import ute.service.admin.inter.VoucherService;
import ute.service.admin.Impl.VoucherServiceImpl;

@WebServlet(urlPatterns = { "/api/admin/voucher/searchpaginated", "/api/admin/voucher/saveOrUpdate",
        "/api/admin/voucher/delete", "/api/admin/voucher/view" })
public class VoucherController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private VoucherService voucherService = new VoucherServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String uri = req.getRequestURI();

        if (uri.contains("/api/admin/voucher/searchpaginated")) {

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

            List<Voucher> voucherList;
            int totalVouchers;
            if (isSearch) {
                voucherList = voucherService.findByCodeVoucherPaginated(searchKeyword, firstResult, size);
                totalVouchers = (int) voucherService.countByCodeVoucher(searchKeyword);
            } else {
                voucherList = voucherService.findPage(page, size);
                totalVouchers = (int) voucherService.count();
            }
            int totalPages = (int) Math.ceil((double) totalVouchers / size);

            // Format dates as Strings for JSP display


            req.setAttribute("voucherList", voucherList);
            req.setAttribute("currentPage", page);
            req.setAttribute("totalPages", totalPages);
            req.setAttribute("size", size);
            req.setAttribute("searchKeyword", searchKeyword);

            req.getRequestDispatcher("/WEB-INF/views/admin/voucher/searchpaginated.jsp").forward(req, resp);

        } else if (uri.contains("/api/admin/voucher/saveOrUpdate")) {
            String idStr = req.getParameter("id");
            Voucher voucher = new Voucher();
            if (idStr != null && !idStr.isEmpty()) {
                voucher = voucherService.findById(Long.parseLong(idStr));
                if (voucher == null) {
                    req.setAttribute("error", "Không tìm thấy Voucher!");
                    req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            }
            // Format for input fields
            String startInput = voucher.getStartDate() != null ? voucher.getStartDate().format(INPUT_FORMATTER) : "";
            String endInput = voucher.getEndDate() != null ? voucher.getEndDate().format(INPUT_FORMATTER) : "";
            req.setAttribute("startInput", startInput);
            req.setAttribute("endInput", endInput);
            req.setAttribute("voucher", voucher);
            req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
        } else if (uri.contains("/api/admin/voucher/view")) {
            String idStr = req.getParameter("id");
            Voucher voucher = voucherService.findById(Long.parseLong(idStr));
            if (voucher == null) {
                req.setAttribute("error", "Không tìm thấy Voucher!");
                req.getRequestDispatcher("/WEB-INF/views/admin/voucher/searchpaginated.jsp").forward(req, resp);
                return;
            }
            // Format dates for display
            req.setAttribute("startFormatted", voucher.getStartDate() != null ? voucher.getStartDate().format(FORMATTER) : "Chưa xác định");
            req.setAttribute("endFormatted", voucher.getEndDate() != null ? voucher.getEndDate().format(FORMATTER) : "Chưa xác định");
            req.setAttribute("voucher", voucher);
            req.getRequestDispatcher("/WEB-INF/views/admin/voucher/view.jsp").forward(req, resp);
        } else if (uri.contains("api/admin/voucher/delete")) {
            String idStr = req.getParameter("id");
            voucherService.delete(Long.parseLong(idStr));
            resp.sendRedirect(req.getContextPath() + "/api/admin/voucher/searchpaginated");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();

        if (uri.contains("/api/admin/voucher/saveOrUpdate")) {
            Voucher voucher = new Voucher();

            // Get parameters
            String idStr = req.getParameter("id");
            String codeVoucher = req.getParameter("codeVoucher");
            String description = req.getParameter("description");
            String discountPercentStr = req.getParameter("discountPercent");
            String startDateStr = req.getParameter("startDate");
            String endDateStr = req.getParameter("endDate");
            String maxDiscountAmountStr = req.getParameter("maxDiscountAmount");

            Long id = null;
            if (idStr != null && !idStr.isEmpty()) {
                id = Long.parseLong(idStr);
                voucher = voucherService.findById(id);
                if (voucher == null) {
                    req.setAttribute("error", "Không tìm thấy Voucher!");
                    req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            }

            // Validate
            if (codeVoucher == null || codeVoucher.trim().isEmpty()) {
                req.setAttribute("error", "Mã Voucher không được để trống!");
                req.setAttribute("voucher", voucher);
                req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                return;
            }
            if (discountPercentStr == null || discountPercentStr.trim().isEmpty()) {
                req.setAttribute("error", "Phần trăm giảm giá không được để trống!");
                req.setAttribute("voucher", voucher);
                req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                return;
            }
            try {
                Double discountPercent = Double.parseDouble(discountPercentStr.trim());
                if (discountPercent < 0 || discountPercent > 100) {
                    req.setAttribute("error", "Phần trăm giảm giá phải từ 0 đến 100!");
                    req.setAttribute("voucher", voucher);
                    req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                    return;
                }
                voucher.setDiscountPercent(discountPercent);
            } catch (NumberFormatException e) {
                req.setAttribute("error", "Phần trăm giảm giá phải là số hợp lệ!");
                req.setAttribute("voucher", voucher);
                req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                return;
            }

            voucher.setCodeVoucher(codeVoucher.trim());
            voucher.setDescription(description != null ? description.trim() : null);

            // Parse dates
            if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                try {
                    voucher.setStartDate(LocalDateTime.parse(startDateStr.trim(), INPUT_FORMATTER));
                } catch (Exception e) {
                    req.setAttribute("error", "Ngày bắt đầu không hợp lệ! (Định dạng: yyyy-MM-ddTHH:mm)");
                    req.setAttribute("voucher", voucher);
                    req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            } else {
                req.setAttribute("error", "Ngày bắt đầu không được để trống!");
                req.setAttribute("voucher", voucher);
                req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                return;
            }

            if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                try {
                    voucher.setEndDate(LocalDateTime.parse(endDateStr.trim(), INPUT_FORMATTER));
                } catch (Exception e) {
                    req.setAttribute("error", "Ngày kết thúc không hợp lệ! (Định dạng: yyyy-MM-ddTHH:mm)");
                    req.setAttribute("voucher", voucher);
                    req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            } else {
                req.setAttribute("error", "Ngày kết thúc không được để trống!");
                req.setAttribute("voucher", voucher);
                req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                return;
            }

            // Validate end after start
            if (voucher.getEndDate().isBefore(voucher.getStartDate())) {
                req.setAttribute("error", "Ngày kết thúc phải sau ngày bắt đầu!");
                req.setAttribute("voucher", voucher);
                req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                return;
            }

            if (maxDiscountAmountStr != null && !maxDiscountAmountStr.trim().isEmpty()) {
                try {
                    Double maxDiscount = Double.parseDouble(maxDiscountAmountStr.trim());
                    if (maxDiscount < 0) {
                        req.setAttribute("error", "Số tiền giảm tối đa phải >= 0!");
                        req.setAttribute("voucher", voucher);
                        req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                        return;
                    }
                    voucher.setMaxDiscountAmount(maxDiscount);
                } catch (NumberFormatException e) {
                    req.setAttribute("error", "Số tiền giảm tối đa phải là số hợp lệ!");
                    req.setAttribute("voucher", voucher);
                    req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                    return;
                }
            } else {
                voucher.setMaxDiscountAmount(null);
            }

            // Check duplicate codeVoucher
            Voucher existing = voucherService.findByCodeVoucherExact(codeVoucher.trim());
            if (existing != null && !Objects.equals(existing.getVoucherID(), id)) {
                req.setAttribute("error", "Mã Voucher đã tồn tại! Vui lòng nhập mã khác!");
                req.setAttribute("voucher", voucher);
                req.getRequestDispatcher("/WEB-INF/views/admin/voucher/addOrEdit.jsp").forward(req, resp);
                return;
            }

            String message;
            if (id != null) {
                voucherService.update(voucher);
                message = "Voucher đã được cập nhật!";
            } else {
                voucherService.insert(voucher);
                message = "Voucher đã được thêm mới!";
            }

            req.getSession().setAttribute("message", message);
            resp.sendRedirect(req.getContextPath() + "/api/admin/voucher/searchpaginated");
        }
    }
}