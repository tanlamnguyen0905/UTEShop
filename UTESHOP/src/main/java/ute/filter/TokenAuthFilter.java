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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import ute.utils.JwtUtil;

/**
 * Unified JWT Token Authentication Filter - Validate JWT token cho API
 * endpoints - Role-based access control - CORS support
 */
@WebFilter(urlPatterns = { "/*" })
public class TokenAuthFilter implements Filter {

	private final Gson gson = new Gson();

	// Các route được bỏ qua (public, static resources, login/register)
	private static final List<String> EXCLUDED_PATHS = List.of("/auth/", "/public/", "/assets/", "/uploads/", "/css/",
			"/js/", "/images/", "/user/", "/home", "/WEB-INF/", ".jsp", ".css", ".js", ".png", ".jpg", ".ico",
			"/api/cart/", "/api/favorite/", "/api/address/", // Session-based APIs (không cần JWT)
			"/api/vouchers/", "/api/voucher/" // Public voucher APIs cho checkout
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

		// Bỏ qua các đường dẫn public và static resources
		if (isExcludedPath(path)) {
			chain.doFilter(request, response);
			return;
		}

		// Chỉ kiểm tra token cho /api/*
		if (!path.startsWith(req.getContextPath() + "/api/")) {
			chain.doFilter(request, response);
			return;
		}

		// Lấy token từ nhiều nguồn (ưu tiên: Cookie > Header > Session)
		String token = null;

		// Kiểm tra cookie trước (tự động gửi với mọi request)
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("authToken".equals(cookie.getName())) {
					token = cookie.getValue();
					System.out.println("[AUTH] Token from Cookie");
					break;
				}
			}
		}

		// Nếu không có cookie, kiểm tra Authorization header
		if (token == null) {
			String authHeader = req.getHeader("Authorization");
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				token = authHeader.substring(7);
				System.out.println("[AUTH] Token from Header");
			}
		}

		// Nếu không có token, báo lỗi
		if (token == null) {
			sendError(req, res, 401, "Missing authentication token");
			return;
		}

		// Validate token
		if (!JwtUtil.validateToken(token)) {
			sendError(req, res, 401, "Invalid or expired token");
			return;
		}

		// Extract user info from token
		String username = JwtUtil.extractUsername(token);
		String role = JwtUtil.extractRole(token);
		Long userId = JwtUtil.extractUserId(token);

		// Log for debugging
		System.out.println(" JWT Valid | user=" + username + " | role=" + role + " | id=" + userId + " | path=" + path);

		// Role-based access control
		if (!hasAccess(path, role)) {
			sendError(req, res, 403, "Access denied. You do not have permission to access this resource.");
			return;
		}

		// Set user attributes for controller
		req.setAttribute("tokenUsername", username);
		req.setAttribute("tokenRole", role);
		req.setAttribute("tokenUserId", userId);

		// Proceed to controller
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
	 * Role-based access control Hierarchy: ADMIN > MANAGER > SHIPPER > USER
	 */
	private boolean hasAccess(String path, String role) {
		if (role == null) {
			return false;
		}

		String pathLower = path.toLowerCase();
		String roleUpper = role.toUpperCase();

		switch (roleUpper) {
		case "ADMIN":
			// Admin has full access to everything
			return true;

		case "MANAGER":
			// Manager has access to: manager APIs + admin chat + user APIs
			return pathLower.contains("/api/manager") || pathLower.contains("/api/admin/chat")
					|| pathLower.contains("/api/user") || pathLower.contains("/api/order");

		case "SHIPPER":
			// Shipper has access to: shipper APIs + user APIs
			return pathLower.contains("/api/shipper") || pathLower.contains("/api/user")
					|| pathLower.contains("/api/order");

		case "USER":
			// User can only access user APIs
			return pathLower.contains("/api/user") || pathLower.contains("/api/order");

		default:
			return false;
		}
	}

	/**
	 * Send error response (JSON for API clients, HTML page for browsers)
	 */
	private void sendError(HttpServletResponse res, int status, String message) throws IOException {
		res.setStatus(status);

		// Nếu không có ServletRequest, trả JSON mặc định
		res.setContentType("application/json;charset=UTF-8");
		Map<String, Object> error = new HashMap<>();
		error.put("success", false);
		error.put("error", message);

		PrintWriter out = res.getWriter();
		out.print(gson.toJson(error));
	}

	/**
	 * Send error response with request context (can forward to error pages)
	 */
	private void sendError(HttpServletRequest req, HttpServletResponse res, int status, String message)
			throws IOException, ServletException {
		// Kiểm tra xem request có từ browser không (Accept header chứa text/html)
		String acceptHeader = req.getHeader("Accept");
		boolean isBrowserRequest = acceptHeader != null && acceptHeader.contains("text/html");

		if (isBrowserRequest) {
			// Forward đến error page đẹp cho browser
			res.setStatus(status);
			req.setAttribute("errorMessage", message);

			String errorPage = "/WEB-INF/views/error/" + status + ".jsp";
			try {
				req.getRequestDispatcher(errorPage).forward(req, res);
			} catch (Exception e) {
				// Fallback to JSON nếu không tìm thấy error page
				sendError(res, status, message);
			}
		} else {
			// Trả JSON cho API clients
			sendError(res, status, message);
		}
	}
}
