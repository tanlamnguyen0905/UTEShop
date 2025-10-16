package ute.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ute.utils.JwtUtil;
import java.io.IOException;

@WebFilter(urlPatterns = {"/api/*"})
public class JwtFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (JwtUtil.validateToken(token)) {
                // Token hợp lệ → lấy thông tin người dùng
                String username = JwtUtil.extractUsername(token);
                String role = JwtUtil.extractRole(token);
                Long userId = JwtUtil.extractUserId(token);

                System.out.println("✅ User hợp lệ: " + username + " | role=" + role + " | id=" + userId);

                // Cho phép request đi tiếp
                chain.doFilter(request, response);
                return;
            } else {
                // Token sai
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("{\"error\": \"Token không hợp lệ hoặc đã hết hạn\"}");
                return;
            }
        }

        // Không có header Authorization
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.getWriter().write("{\"error\": \"Thiếu header Authorization hoặc Bearer token\"}");
    }
}
