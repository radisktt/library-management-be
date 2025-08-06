package libman_be.libman_be.dto.response;

import libman_be.libman_be.dto.response.BookResponseDTO;
import libman_be.libman_be.entity.BookLoan;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookLoanResponseDTO {
    private Long id;
    private Long userId;
    private String username;
    private Long bookId;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BookLoan.LoanStatus status;
    private FineDTO fine;

    @Data
    public static class FineDTO {
        private Long id;
        private Double amount;
        private LocalDate issuedDate;
        private boolean paid;
    }
}