package ute.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.utils.JwtUtil;
import java.io.IOException;
import java.util.List;

@WebFilter(urlPatterns = {"/*"})
public class JwtFilter implements Filter {

    // Các route được bỏ qua (public, static resources, login/register)
    private static final List<String> EXCLUDED_PATHS = List.of(
        "/auth/", "/public/", "/assets/", "/uploads/", "/css/", "/js/", "/images/"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getRequestURI();

        // 1️⃣ Bỏ qua các đường dẫn public
        if (isExcludedPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 2️⃣ Chỉ kiểm tra token cho /api/*
        if (!path.startsWith(req.getContextPath() + "/api/")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(res, HttpServletResponse.SC_UNAUTHORIZED, "Thiếu header Authorization hoặc Bearer token");
            return;
        }

        String token = authHeader.substring(7);

        if (!JwtUtil.validateToken(token)) {
            sendError(res, HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ hoặc đã hết hạn");
            return;
        }

        // 3️⃣ Token hợp lệ → trích xuất thông tin người dùng
        String username = JwtUtil.extractUsername(token);
        String role = JwtUtil.extractRole(token);
        Long userId = JwtUtil.extractUserId(token);

        System.out.println("✅ JWT hợp lệ | user=" + username + " | role=" + role + " | id=" + userId);

        // 4️⃣ Kiểm tra quyền truy cập theo role
        if (!hasAccess(path, role)) {
            sendError(res, HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập API này");
            return;
        }

        // ✅ Cho phép đi tiếp
        chain.doFilter(request, response);
    }

    // ✅ Bỏ qua các route công khai (public, static, login/register)
    private boolean isExcludedPath(String path) {
        for (String prefix : EXCLUDED_PATHS) {
            if (path.contains(prefix)) return true;
        }
        return false;
    }

    // ✅ Kiểm tra quyền truy cập theo role
    private boolean hasAccess(String path, String role) {
        path = path.toLowerCase();

        switch (role.toUpperCase()) {
            case "ADMIN":
                return true; // Admin có toàn quyền
            case "MANAGER":
                return path.contains("/api/manager") || path.contains("/api/user");
            case "SHIPPER":
                return path.contains("/api/shipper");
            case "USER":
                return path.contains("/api/user");
            default:
                return false;
        }
    }

    // ✅ Gửi JSON lỗi
    private void sendError(HttpServletResponse res, int status, String message) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
