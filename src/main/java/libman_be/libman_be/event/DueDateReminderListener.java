package libman_be.libman_be.event;

import jakarta.mail.MessagingException;
import libman_be.libman_be.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DueDateReminderListener {

    private final EmailService emailService;

    public DueDateReminderListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleDueDateReminderEvent(DueDateReminderEvent event) throws MessagingException {
        log.info("Due date reminder event received");
        emailService.sendDueDateReminderEmail(
                event.getEmail(),
                event.getUserName(),
                event.getBookTitle(),
                event.getDueDate()
        );
    }

    @Async
    @EventListener
    public void handleOverdueNotificationEvent(OverdueNotificationEvent event) throws MessagingException {
        emailService.sendOverdueNotificationEmail(
                event.getEmail(),
                event.getUserName(),
                event.getBookTitle(),
                event.getDueDate(),
                event.getFineAmount()
        );
    }
}