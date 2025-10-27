package ute.controllers.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ute.entities.Cart;
import ute.entities.Users;
import ute.entities.Voucher;
import ute.service.admin.Impl.VoucherServiceImpl;
import ute.service.admin.inter.VoucherService;
import ute.service.impl.CartServiceImpl;

/**
 * API Controller để xử lý các thao tác liên quan đến voucher cho user
 */
@WebServlet(urlPatterns = {"/api/vouchers/available", "/api/vouchers/validate", "/api/voucher/validate", "/api/voucher/apply"})
public class VoucherController extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private final VoucherService voucherService;
    private final CartServiceImpl cartService;
    private final Gson gson;
    
    public VoucherController() {
        this.voucherService = new VoucherServiceImpl();
        this.cartService = new CartServiceImpl();
        this.gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        
        PrintWriter out = resp.getWriter();
        String uri = req.getRequestURI();
        
        try {
            if (uri.contains("/api/vouchers/available")) {
                // Lấy danh sách voucher còn hiệu lực
                handleGetAvailableVouchers(req, resp, out);
                
            } else if (uri.contains("/api/vouchers/validate")) {
                // Validate mã voucher
                handleValidateVoucher(req, resp, out);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(Map.of("success", false, "message", "Lỗi hệ thống: " + e.getMessage())));
        }
    }
    
    /**
     * Lấy danh sách voucher còn hiệu lực (không cần đăng nhập)
     */
    private void handleGetAvailableVouchers(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) {
        try {
            LocalDateTime now = LocalDateTime.now();
            
            // Lấy tất cả voucher (có thể tối ưu bằng cách thêm method trong DAO)
            List<Voucher> allVouchers = voucherService.findPage(1, 100);
            
            // Lọc voucher còn hiệu lực: startDate <= now <= endDate
            List<Map<String, Object>> availableVouchers = allVouchers.stream()
                .filter(v -> {
                    boolean started = v.getStartDate() != null && 
                                    (v.getStartDate().isBefore(now) || v.getStartDate().isEqual(now));
                    boolean notExpired = v.getEndDate() != null && 
                                       (v.getEndDate().isAfter(now) || v.getEndDate().isEqual(now));
                    return started && notExpired;
                })
                .map(v -> {
                    Map<String, Object> voucherMap = new HashMap<>();
                    voucherMap.put("voucherID", v.getVoucherID());
                    voucherMap.put("codeVoucher", v.getCodeVoucher() != null ? v.getCodeVoucher() : "");
                    voucherMap.put("description", v.getDescription() != null ? v.getDescription() : "");
                    voucherMap.put("discountPercent", v.getDiscountPercent() != null ? v.getDiscountPercent() : 0.0);
                    voucherMap.put("maxDiscountAmount", v.getMaxDiscountAmount());
                    
                    // Format date properly
                    String endDateStr = "";
                    if (v.getEndDate() != null) {
                        endDateStr = v.getEndDate().toString();
                    }
                    voucherMap.put("endDate", endDateStr);
                    
                    return voucherMap;
                })
                .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("vouchers", availableVouchers);
            
            out.print(gson.toJson(response));
            
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(Map.of("success", false, "message", "Không thể lấy danh sách voucher: " + e.getMessage())));
        }
    }
    
    /**
     * Validate mã voucher và trả về thông tin giảm giá
     */
    private void handleValidateVoucher(HttpServletRequest req, HttpServletResponse resp, PrintWriter out) {
        try {
            String voucherCode = req.getParameter("code");
            String totalAmountStr = req.getParameter("totalAmount");
            
            if (voucherCode == null || voucherCode.trim().isEmpty()) {
                out.print(gson.toJson(Map.of("success", false, "message", "Vui lòng nhập mã giảm giá")));
                return;
            }
            
            // Tìm voucher theo mã
            Voucher voucher = voucherService.findByCodeVoucherExact(voucherCode.trim());
            
            if (voucher == null) {
                out.print(gson.toJson(Map.of("success", false, "message", "Mã giảm giá không tồn tại")));
                return;
            }
            
            // Kiểm tra thời hạn: startDate <= now <= endDate
            LocalDateTime now = LocalDateTime.now();
            if (voucher.getStartDate() != null && now.isBefore(voucher.getStartDate())) {
                out.print(gson.toJson(Map.of("success", false, "message", "Mã giảm giá chưa có hiệu lực")));
                return;
            }
            
            if (voucher.getEndDate() != null && now.isAfter(voucher.getEndDate())) {
                out.print(gson.toJson(Map.of("success", false, "message", "Mã giảm giá đã hết hạn")));
                return;
            }
            
            // Tính toán số tiền giảm giá
            Double totalAmount = 0.0;
            if (totalAmountStr != null && !totalAmountStr.trim().isEmpty()) {
                try {
                    totalAmount = Double.parseDouble(totalAmountStr);
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
            
            Double discountAmount = totalAmount * (voucher.getDiscountPercent() / 100);
            
            // Áp dụng giới hạn giảm giá tối đa nếu có
            if (voucher.getMaxDiscountAmount() != null && discountAmount > voucher.getMaxDiscountAmount()) {
                discountAmount = voucher.getMaxDiscountAmount();
            }
            
            // Trả về thông tin voucher hợp lệ
            Map<String, Object> voucherInfo = new HashMap<>();
            voucherInfo.put("voucherID", voucher.getVoucherID());
            voucherInfo.put("codeVoucher", voucher.getCodeVoucher());
            voucherInfo.put("description", voucher.getDescription());
            voucherInfo.put("discountPercent", voucher.getDiscountPercent());
            voucherInfo.put("maxDiscountAmount", voucher.getMaxDiscountAmount());
            voucherInfo.put("discountAmount", discountAmount);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Áp dụng mã giảm giá thành công!");
            response.put("voucher", voucherInfo);
            
            out.print(gson.toJson(response));
            
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(gson.toJson(Map.of("success", false, "message", "Không thể validate voucher")));
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        
        String uri = req.getRequestURI();
        HttpSession session = req.getSession();
        
        // Lấy user nếu có (có thể null)
        Users currentUser = (Users) session.getAttribute("currentUser");
        
        if (uri.contains("/api/voucher/validate") || uri.contains("/api/voucher/apply")) {
            handleApplyVoucher(req, resp, currentUser, session);
        }
    }
    
    /**
     * Áp dụng voucher từ checkout page (cho cả user đã đăng nhập và guest)
     */
    private void handleApplyVoucher(HttpServletRequest req, HttpServletResponse resp, Users currentUser, HttpSession session) 
            throws IOException {
        try {
            // Đọc request body
            String voucherCode = req.getParameter("voucherCode");
            
            if (voucherCode == null || voucherCode.trim().isEmpty()) {
                sendErrorResponse(resp, "Vui lòng nhập mã giảm giá");
                return;
            }
            
            // Tìm voucher theo mã
            Voucher voucher = voucherService.findByCodeVoucherExact(voucherCode.trim());
            
            if (voucher == null) {
                sendErrorResponse(resp, "Mã giảm giá không tồn tại");
                return;
            }
            
            // Kiểm tra thời gian hiệu lực: startDate <= now <= endDate
            LocalDateTime now = LocalDateTime.now();
            if (voucher.getStartDate() != null && now.isBefore(voucher.getStartDate())) {
                sendErrorResponse(resp, "Mã giảm giá chưa có hiệu lực");
                return;
            }
            
            if (voucher.getEndDate() != null && now.isAfter(voucher.getEndDate())) {
                sendErrorResponse(resp, "Mã giảm giá đã hết hạn");
                return;
            }
            
            // Lấy totalPrice từ giỏ hàng hoặc từ request parameter
            double totalPrice = 0.0;
            double shippingFee = 25000.0;
            
            if (currentUser != null) {
                // User đã đăng nhập - lấy từ giỏ hàng trong DB
                Cart cart = cartService.getOrCreateCart(currentUser.getUserID());
                if (cart == null || cart.getCartDetails() == null || cart.getCartDetails().isEmpty()) {
                    sendErrorResponse(resp, "Giỏ hàng trống");
                    return;
                }
                totalPrice = cart.getTotalPrice();
            } else {
                // Guest user - lấy từ request parameter hoặc session
                String totalPriceParam = req.getParameter("totalPrice");
                if (totalPriceParam != null && !totalPriceParam.trim().isEmpty()) {
                    try {
                        totalPrice = Double.parseDouble(totalPriceParam);
                    } catch (NumberFormatException e) {
                        sendErrorResponse(resp, "Giá trị đơn hàng không hợp lệ");
                        return;
                    }
                }
                
                // Nếu không có totalPrice từ parameter, kiểm tra session
                if (totalPrice == 0.0) {
                    Object cartTotalObj = session.getAttribute("cartTotal");
                    if (cartTotalObj != null) {
                        totalPrice = (Double) cartTotalObj;
                    }
                }
                
                if (totalPrice == 0.0) {
                    sendErrorResponse(resp, "Không thể xác định giá trị đơn hàng");
                    return;
                }
            }
            
            // Tính toán số tiền giảm (chỉ áp dụng cho giá sản phẩm, không bao gồm phí ship)
            double discountAmount = totalPrice * (voucher.getDiscountPercent() / 100);
            
            // Áp dụng giảm giá tối đa nếu có
            if (voucher.getMaxDiscountAmount() != null && voucher.getMaxDiscountAmount() > 0) {
                discountAmount = Math.min(discountAmount, voucher.getMaxDiscountAmount());
            }
            
            // Tính tổng cuối cùng: giá sản phẩm + phí ship - giảm giá
            double finalTotal = Math.max(0, totalPrice + shippingFee - discountAmount);
            
            // Lưu voucher vào session
            session.setAttribute("appliedVoucher", voucher);
            session.setAttribute("discountAmount", discountAmount);
            
            // Trả về kết quả
            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.addProperty("message", "Áp dụng mã giảm giá thành công!");
            response.addProperty("voucherCode", voucher.getCodeVoucher());
            response.addProperty("voucherDescription", voucher.getDescription());
            response.addProperty("discountPercent", voucher.getDiscountPercent());
            response.addProperty("discountAmount", discountAmount);
            response.addProperty("maxDiscountAmount", voucher.getMaxDiscountAmount());
            response.addProperty("totalPrice", totalPrice);
            response.addProperty("shippingFee", shippingFee);
            response.addProperty("finalTotal", finalTotal);
            response.addProperty("voucherId", voucher.getVoucherID());
            
            resp.getWriter().write(gson.toJson(response));
            
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(resp, "Có lỗi xảy ra: " + e.getMessage());
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        
        HttpSession session = req.getSession();
        
        // Xóa voucher khỏi session
        session.removeAttribute("appliedVoucher");
        session.removeAttribute("discountAmount");
        
        JsonObject response = new JsonObject();
        response.addProperty("success", true);
        response.addProperty("message", "Đã xóa mã giảm giá");
        
        resp.getWriter().write(gson.toJson(response));
    }
    
    /**
     * Gửi error response
     */
    private void sendErrorResponse(HttpServletResponse resp, String message) throws IOException {
        JsonObject response = new JsonObject();
        response.addProperty("success", false);
        response.addProperty("error", message);
        resp.getWriter().write(gson.toJson(response));
    }
}

