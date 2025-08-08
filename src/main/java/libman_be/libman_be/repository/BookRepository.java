package libman_be.libman_be.repository;

import io.lettuce.core.dynamic.annotation.Param;
import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.BookDTO;
import libman_be.libman_be.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);

    @Query("SELECT b FROM Book b")
    Page<Book> findAllBook(Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.authors a where a.id = :authorId")
    Page<Book> findByAuthorId(@Param("authorId") Long authorId, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.authors a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :authorName, '%'))")
    Page<Book> findByAuthorName(@Param("authorName") String authorName, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    Page<Book> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :categoryName, '%'))")
    Page<Book> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE b.publisher.id = :publisherId")
    Page<Book> findByPublisherId(@Param("publisherId") Long publisherId, Pageable pageable);

    @Query("SELECT b FROM Book b WHERE LOWER(b.publisher.name) LIKE LOWER(CONCAT('%', :publisherName, '%'))")
    Page<Book> findByPublisherName(@Param("publisherName") String publisherName, Pageable pageable);
}