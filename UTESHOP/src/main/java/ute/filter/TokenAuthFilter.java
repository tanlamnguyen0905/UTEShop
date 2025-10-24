package ute.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.utils.JwtUtil;

/**
 * Unified JWT Token Authentication Filter
 * - Validate JWT token cho API endpoints
 * - Role-based access control
 * - CORS support
 */
@WebFilter(urlPatterns = { "/*" })
public class TokenAuthFilter implements Filter {

    private final Gson gson = new Gson();

    // Các route được bỏ qua (public, static resources, login/register)
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/auth/", "/public/", "/assets/", "/uploads/",
            "/css/", "/js/", "/images/", "/user/", "/home",
            "/WEB-INF/", ".jsp", ".css", ".js", ".png", ".jpg", ".ico",
            "/api/cart/", "/api/address/" // Session-based APIs (không cần JWT)
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // CORS headers
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        // Handle preflight requests
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 1️⃣ Bỏ qua các đường dẫn public và static resources
        if (isExcludedPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 2️⃣ Chỉ kiểm tra token cho /api/*
        if (!path.startsWith(req.getContextPath() + "/api/")) {
            chain.doFilter(request, response);
            return;
        }

        // 3️⃣ Get Authorization header
        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendError(res, 401, "Missing or invalid Authorization header");
            return;
        }

        // 4️⃣ Extract and validate token
        String token = authHeader.substring(7);

        if (!JwtUtil.validateToken(token)) {
            sendError(res, 401, "Invalid or expired token");
            return;
        }

        // 5️⃣ Extract user info from token
        String username = JwtUtil.extractUsername(token);
        String role = JwtUtil.extractRole(token);
        Long userId = JwtUtil.extractUserId(token);

        // Log for debugging
        System.out
                .println("✅ JWT Valid | user=" + username + " | role=" + role + " | id=" + userId + " | path=" + path);

        // 6️⃣ Role-based access control
        if (!hasAccess(path, role)) {
            sendError(res, 403, "Access denied. You don't have permission to access this resource.");
            return;
        }

        // 7️⃣ Set user attributes for controller
        req.setAttribute("tokenUsername", username);
        req.setAttribute("tokenRole", role);
        req.setAttribute("tokenUserId", userId);

        // ✅ Proceed to controller
        chain.doFilter(request, response);
    }

    /**
     * Check if path should skip token validation
     */
    private boolean isExcludedPath(String path) {
        for (String prefix : EXCLUDED_PATHS) {
            if (path.contains(prefix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Role-based access control
     */
    private boolean hasAccess(String path, String role) {
        if (role == null) {
            return false;
        }

        String pathLower = path.toLowerCase();
        String roleUpper = role.toUpperCase();

        switch (roleUpper) {
            case "ADMIN":
                return true; // Admin has full access

            case "MANAGER":
                // Manager can access manager and user APIs
                return pathLower.contains("/api/manager") ||
                        pathLower.contains("/api/user");

            case "SHIPPER":
                // Shipper can only access shipper APIs
                return pathLower.contains("/api/shipper");

            case "USER":
                // User can only access user APIs
                return pathLower.contains("/api/user");

            default:
                return false;
        }
    }

    /**
     * Send JSON error response
     */
    private void sendError(HttpServletResponse res, int status, String message) throws IOException {
        res.setStatus(status);
        res.setContentType("application/json;charset=UTF-8");

        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("error", message);

        PrintWriter out = res.getWriter();
        out.print(gson.toJson(error));
    }
}
