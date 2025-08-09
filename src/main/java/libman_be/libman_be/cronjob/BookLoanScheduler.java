package libman_be.libman_be.cronjob;

import libman_be.libman_be.entity.Book;
import libman_be.libman_be.entity.BookLoan;
import libman_be.libman_be.entity.Fine;
import libman_be.libman_be.entity.User;
import libman_be.libman_be.event.DueDateReminderEvent;
import libman_be.libman_be.event.OverdueNotificationEvent;
import libman_be.libman_be.repository.BookLoanRepository;
import libman_be.libman_be.repository.FineRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class BookLoanScheduler {

    @Value("${spring.application.fine_per_day}")
    private double FINE_PER_DAY;

    private final FineRepository fineRepository;
    private final BookLoanRepository bookLoanRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public BookLoanScheduler(BookLoanRepository bookLoanRepository, ApplicationEventPublisher applicationEventPublisher, FineRepository fineRepository) {
        this.bookLoanRepository = bookLoanRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.fineRepository = fineRepository;
    }

    @Scheduled(cron = "0 51 10 * * ?")
    @Transactional
    public void checkOverdueLoansAndSendNotifications(){
        LocalDate today = LocalDate.now();
        List<BookLoan> loans =
                bookLoanRepository.findBookLoansByDueDateBeforeAndStatusNotReturned(today);

        for (BookLoan loan : loans) {
            LocalDate dueDate = loan.getDueDate();
            User user = loan.getUser();
            Book book = loan.getBook();
            if(dueDate.equals(today)){
                applicationEventPublisher.publishEvent(new DueDateReminderEvent(
                        user.getEmail(), user.getName(), book.getTitle(), dueDate
                ));
            }
            if (dueDate.isBefore(today)) {
                if (loan.getStatus()!= BookLoan.LoanStatus.OVERDUE){
                    loan.setStatus(BookLoan.LoanStatus.OVERDUE);
                    }
                Fine fine = loan.getFine();
                long daysOverdue = ChronoUnit.DAYS.between(dueDate, today);
                if (fine == null){
                    fine = new Fine();
                    fine.setBookLoan(loan);
                    fine.setAmount(daysOverdue * FINE_PER_DAY);
                    fine.setPaid(false);
                }
                bookLoanRepository.save(loan);
                fineRepository.save(fine);

                applicationEventPublisher.publishEvent(new OverdueNotificationEvent(
                        user.getEmail(), user.getName(), book.getTitle(), dueDate, fine.getAmount()
                ));
            }
        }

    }
}
