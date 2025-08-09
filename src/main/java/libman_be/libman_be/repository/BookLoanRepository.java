package libman_be.libman_be.repository;

import io.lettuce.core.dynamic.annotation.Param;
import libman_be.libman_be.entity.BookLoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {
    List<BookLoan> findByUserIdAndStatus(Long userId, BookLoan.LoanStatus status);
    List<BookLoan> findByBookIdAndStatus(Long bookId, BookLoan.LoanStatus status);
    Page<BookLoan> findByUserIdAndStatus(Long userId, BookLoan.LoanStatus status, Pageable pageable);
    Page<BookLoan> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT b FROM BookLoan b WHERE b.dueDate <= :dueDateBefore AND b.status <> 'RETURNED'")
    List<BookLoan> findBookLoansByDueDateBeforeAndStatusNotReturned(@Param("dueDateBefore") LocalDate dueDateBefore);
}