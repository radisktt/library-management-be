package libman_be.libman_be.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublisherDTO {
    private Long id;
    private String name;
    private String address;
}
