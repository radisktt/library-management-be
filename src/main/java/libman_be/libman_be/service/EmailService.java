package libman_be.libman_be.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

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
    public void sendDueDateReminderEmail(String to, String userName, String bookTitle, LocalDate dueDate) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Reminder: Book Due Date Approaching");
        String content = String.format(
                "<h3>Dear %s,</h3>" +
                        "<p>This is a reminder that the book <b>%s</b> you borrowed is due on <b>%s</b>.</p>" +
                        "<p>Please return it by the due date to avoid any fines.</p>" +
                        "<p>Thank you,<br>ZZ Library</p>",
                userName, bookTitle, dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        );
        helper.setText(content, true);
        mailSender.send(message);
        log.info("Due date reminder email sent successfully to {}", to);
    }
    public void sendOverdueNotificationEmail(String to, String userName, String bookTitle, LocalDate dueDate, double fineAmount) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Overdue Notice: Book Return Required");
        String content = String.format(
                "<h3>Dear %s,</h3>" +
                        "<p>The book <b>%s</b> you borrowed was due on <b>%s</b> and is now overdue.</p>" +
                        "<p>A fine of <b>%.2f VND</b> has been applied. Please return the book and settle the fine as soon as possible.</p>" +
                        "<p>Thank you,<br>ZZ Library</p>",
                userName, bookTitle, dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE), fineAmount
        );
        helper.setText(content, true);
        mailSender.send(message);
        log.info("Overdue notification email sent successfully to {}", to);
    }
}