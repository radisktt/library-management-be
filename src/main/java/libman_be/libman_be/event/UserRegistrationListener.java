package libman_be.libman_be.event;

import jakarta.mail.MessagingException;
import libman_be.libman_be.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class UserRegistrationListener {

    @Value("${spring.application.base-url}")
    private String baseUrl;

    private final EmailService emailService;

    private final RedisTemplate<String, String> redisTemplate;

    public UserRegistrationListener(EmailService emailService, RedisTemplate<String, String> redisTemplate) {
        this.emailService = emailService;
        this.redisTemplate = redisTemplate;
    }

    @EventListener
    @Async
    public void handleUserRegistrationEvent(UserRegistrationEvent event) throws MessagingException {
        String email = event.getUser().getEmail();
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("verification:"+token, email,5, TimeUnit.MINUTES);
        String verificationLink = baseUrl + "/api/auth/verify?token=" + token;

        emailService.sendVerificationEmail(email,verificationLink);
    }

}
