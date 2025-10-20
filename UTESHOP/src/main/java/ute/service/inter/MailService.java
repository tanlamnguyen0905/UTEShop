package ute.service.inter;

public interface MailService {
	void send(String to, String subject, String body);
}
