package ute.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
	private static final String SECRET_KEY = "UTESHOP_SUPER_SECRET_KEY_2025_AUTH_JWT_SECURITY";
	private static final long EXPIRATION = 3600000; // 1 giờ (60 phút × 60 giây × 1000 ms)

	private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

	// ===================== TẠO TOKEN =====================
	public static String generateToken(String username, String role, Long userId) {
		return Jwts.builder().setSubject(username).addClaims(Map.of("role", role, "userId", userId))
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	// ===================== GIẢI MÃ TOKEN =====================
	public static Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public static String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	public static String extractRole(String token) {
		Object role = extractAllClaims(token).get("role");
		return role != null ? role.toString() : null;
	}

	public static Long extractUserId(String token) {
		Object id = extractAllClaims(token).get("userId");
		if (id == null)
			return null;
		try {
			return Long.parseLong(id.toString());
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// ===================== KIỂM TRA HỢP LỆ =====================
	public static boolean validateToken(String token) {
		try {
			extractAllClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false; // token sai hoặc hết hạn
		}
	}
}
