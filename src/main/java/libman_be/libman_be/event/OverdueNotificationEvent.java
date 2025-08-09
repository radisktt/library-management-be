package libman_be.libman_be.event;

import java.time.LocalDate;

public class OverdueNotificationEvent {
    private final String email;
    private final String userName;
    private final String bookTitle;
    private final LocalDate dueDate;
    private final double fineAmount;

    public OverdueNotificationEvent(String email, String userName, String bookTitle, LocalDate dueDate, double fineAmount) {
        this.email = email;
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.dueDate = dueDate;
        this.fineAmount = fineAmount;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }
}