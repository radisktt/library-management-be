package libman_be.libman_be.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ExtendDueDateRequest {
    private LocalDate dueDate;
    private Long days;
}
