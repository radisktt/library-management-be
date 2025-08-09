package libman_be.libman_be.event;

import java.time.LocalDate;

public class DueDateReminderEvent {
    private final String email;
    private final String userName;
    private final String bookTitle;
    private final LocalDate dueDate;

    public DueDateReminderEvent(String email, String userName, String bookTitle, LocalDate dueDate) {
        this.email = email;
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.dueDate = dueDate;
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
}