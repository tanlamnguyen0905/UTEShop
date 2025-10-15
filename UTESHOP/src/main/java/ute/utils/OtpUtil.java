package ute.utils;

import java.security.SecureRandom;

public class OtpUtil {
	private static final SecureRandom random = new SecureRandom();

	public static String generateOtp() {
		int otp = 100000 + random.nextInt(900000); // 6 chữ số
		return String.valueOf(otp);
	}
}
