package libman_be.libman_be.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LibraryDTO {
    private Long id;
    private String name;
    private String location;
}
