package libman_be.libman_be.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class BookDTO {
    private Long id;
    private String title;
    private String isbn;
    private LocalDate publicationDate;
    private Long pageCount;
    private List<Long> categoriesId;
    private Long publisherId;
    private Set<Long> authorIds;
    private Long libraryId;
    private Long quantity;
}