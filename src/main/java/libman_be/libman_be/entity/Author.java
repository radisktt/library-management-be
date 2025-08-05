package libman_be.libman_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "authors")
@Getter
@Setter
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String biography;

    // Một tác giả có thể viết nhiều sách
    @ManyToMany(mappedBy = "authors")
    private List<Book> books;
}
