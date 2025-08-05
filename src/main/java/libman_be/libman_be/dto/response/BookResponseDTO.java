package libman_be.libman_be.dto.response;

import libman_be.libman_be.dto.AuthorDTO;
import libman_be.libman_be.dto.CategoryDTO;
import libman_be.libman_be.dto.LibraryDTO;
import libman_be.libman_be.dto.PublisherDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookResponseDTO {
    private Long id;
    private String title;
    private String isbn;
    private LocalDate publicationDate;
    private Long pageCount;
    private List<CategoryDTO> categories;
    private PublisherDTO publisher;
    private Set<AuthorDTO> authors;
    private LibraryDTO library;
    private Long quantity;
}
