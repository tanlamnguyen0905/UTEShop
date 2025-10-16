package ute.service.impl;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import ute.service.inter.MailService;
import ute.utils.OtpUtil;

public class MailServiceImpl implements MailService {
	private static final String USER;
	private static final String PASSWORD;

	static {
		USER = "uteshop86@gmail.com";
		PASSWORD = "vkbe ucym xajn fqwv";
	}

	@Override
	public void send(String to, String subject, String body) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USER, PASSWORD);
			}
		});

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(USER));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void sendOtp(String to) {
		String otp = OtpUtil.generateOtp();
		String subject = "UTESHOP - Mã xác thực OTP của bạn";
		String body = "Xin chào,\n\nMã OTP của bạn là: " + otp + "\nMã có hiệu lực trong 5 phút.\n\nUTESHOP Team";
		send(to, subject, body);
	}

}
