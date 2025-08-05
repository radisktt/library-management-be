package libman_be.libman_be.repository;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.BookDTO;
import libman_be.libman_be.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
}