package libman_be.libman_be.repository;

import libman_be.libman_be.entity.BookLoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {
    List<BookLoan> findByUserIdAndStatus(Long userId, BookLoan.LoanStatus status);
    List<BookLoan> findByBookIdAndStatus(Long bookId, BookLoan.LoanStatus status);
    Page<BookLoan> findByUserIdAndStatus(Long userId, BookLoan.LoanStatus status, Pageable pageable);
    Page<BookLoan> findByUserId(Long userId, Pageable pageable);
}