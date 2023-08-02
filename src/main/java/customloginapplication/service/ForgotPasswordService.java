package customloginapplication.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import customloginapplication.model.ForgotPasswordToken;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ForgotPasswordService {
    
	@Autowired
	JavaMailSender javaMailSender;
	
    private final int MINUTES = 10;
	
	public String generateToken() {
		return UUID.randomUUID().toString();
	}
	
	public LocalDateTime expireTimeRange() {
		return LocalDateTime.now().plusMinutes(MINUTES);
	}
	
	public void sendEmail(String to, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException {
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		String emailContent = "<p>Hello</p>"
				              + "Click the link the below to reset password"
				              + "<p><a href=\""+ emailLink + "\">Change My Password</a></p>"
				              + "<br>"
				              + "Ignore this Email if you did not made the request";
		helper.setText(emailContent, true);
		helper.setFrom("codingtechniques15@gmail.com", "Coding Techniques Support");
		helper.setSubject(subject);
		helper.setTo(to);
		javaMailSender.send(message);
	}
	
	public boolean isExpired (ForgotPasswordToken forgotPasswordToken) {
		return LocalDateTime.now().isAfter(forgotPasswordToken.getExpireTime());
	}
	
	public String checkValidity (ForgotPasswordToken forgotPasswordToken, Model model) {
		
		if (forgotPasswordToken == null) {
			model.addAttribute("error", "Invalid Token");
			return "error-page";
		}
		
		else if (forgotPasswordToken.isUsed()) {
			model.addAttribute("error", "the token is already used");
			return "error-page";
		}
		
		else if (isExpired(forgotPasswordToken)) {
			model.addAttribute("error", "the token is expired");
			return "error-page";
		}
		else {
			return "reset-password";
		}
		
		
	}
	
	
	
}
