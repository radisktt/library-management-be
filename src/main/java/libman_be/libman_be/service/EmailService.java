package libman_be.libman_be.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String verificationLink) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Verify Your Email Address");
        helper.setText(
                "<h1>Welcome to ZZ Library</h1>" +
                        "<p>Please click the link below to verify your email address:</p>" +
                        "<a href=\"" + verificationLink + "\">Verify Email</a>",
                true
        );
//        System.out.println(verificationLink);
        mailSender.send(message);
        log.info("Email sent successfully");
    }
}