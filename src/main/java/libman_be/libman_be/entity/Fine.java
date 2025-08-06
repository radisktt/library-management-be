package libman_be.libman_be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "fines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "book_loan_id", nullable = false)
    private BookLoan bookLoan;

    @Column(nullable = false)
    private Double amount;

    private LocalDate issuedDate;

    private boolean paid;
}