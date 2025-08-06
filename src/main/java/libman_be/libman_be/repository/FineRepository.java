package libman_be.libman_be.repository;

import libman_be.libman_be.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FineRepository extends JpaRepository<Fine, Long> {
    Optional<Fine> findByBookLoanId(Long bookLoanId);
}