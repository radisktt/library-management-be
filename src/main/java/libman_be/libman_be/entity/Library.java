package libman_be.libman_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "libraries")
@Getter
@Setter
public class Library {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String location;

    // Một thư viện chứa nhiều sách
    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    private List<Book> books;
}
