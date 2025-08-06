package libman_be.libman_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookLoanDTO {
    private Long userId;
    private List<Long> bookIds;
    private LocalDate borrowDate;
    private LocalDate dueDate;
}