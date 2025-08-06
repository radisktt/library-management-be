package libman_be.libman_be.service.impl;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.BookLoanDTO;
import libman_be.libman_be.dto.response.BookLoanResponseDTO;
import libman_be.libman_be.entity.Book;
import libman_be.libman_be.entity.BookLoan;
import libman_be.libman_be.entity.User;
import libman_be.libman_be.exception.BookException;
import libman_be.libman_be.exception.UserException;
import libman_be.libman_be.mapper.BookMapper;
import libman_be.libman_be.mapper.BookloanMapper;
import libman_be.libman_be.repository.BookLoanRepository;
import libman_be.libman_be.repository.BookRepository;
import libman_be.libman_be.repository.FineRepository;
import libman_be.libman_be.repository.UserRepository;
import libman_be.libman_be.service.BookLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
public class BookLoanServiceImpl implements BookLoanService {
    private static final int DEFAULT_MAX_BORROW_DAYS = 14;
    private static final double FINE_PER_DAY = 5000.0;

    @Autowired
    private BookLoanRepository bookLoanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookloanMapper bookloanMapper;

    @Override
    @Transactional
    public BaseResponse<List<BookLoanResponseDTO>> borrowBooks(BookLoanDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserException.UserNotFoundException("User with id " + dto.getUserId() + " not found"));
        if(dto.getBookIds() == null || dto.getBookIds().isEmpty()) {
            throw new BookException.BookNotFoundException("Book id is empty");
        }
        List<BookLoanResponseDTO> newLoans = new ArrayList<>();
        LocalDate borrowDate = dto.getBorrowDate() != null ? dto.getBorrowDate() : LocalDate.now();
        LocalDate dueDate = dto.getDueDate() != null ? dto.getDueDate() : borrowDate.plusDays(DEFAULT_MAX_BORROW_DAYS);

        for (Long bookId : dto.getBookIds()) {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new BookException.BookNotFoundException("Book with id " + bookId + " not found"));
            if (book.getQuantity() <= 0){
                throw new BookException.BookNotAvailableException("Book is not available");
            }
            book.setQuantity(book.getQuantity() - 1);
            bookRepository.save(book);
            BookLoan bookLoan = new BookLoan();
            bookLoan.setBook(book);
            bookLoan.setUser(user);
            bookLoan.setBorrowDate(borrowDate);
            bookLoan.setDueDate(dueDate);
            bookLoan.setStatus(BookLoan.LoanStatus.BORROWED);

            bookLoanRepository.save(bookLoan);
            newLoans.add(bookloanMapper.toBookLoanResponseDTO(bookLoan));
        }


        return BaseResponse.<List<BookLoanResponseDTO>>builder()
                .status("success")
                .message("Books borrowed successfully")
                .data(newLoans)
                .build();
    }

    @Override
    @Transactional
    public BaseResponse<BookLoanResponseDTO> returnBook(Long loanId) {
        BookLoan loan = bookLoanRepository.findById(loanId)
                .orElseThrow(() -> new BookException.BookNotFoundException("Book with id " + loanId + " not found"));
        if(loan.getStatus() == BookLoan.LoanStatus.RETURNED){
            throw new RuntimeException("Book is returned");
        }
        loan.setStatus(BookLoan.LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());

        Book book = loan.getBook();
        book.setQuantity(book.getQuantity() + 1);
        bookRepository.save(book);

        BookLoan updatedLoan = bookLoanRepository.save(loan);
        BookLoanResponseDTO responseDTO = bookloanMapper.toBookLoanResponseDTO(updatedLoan);
        return BaseResponse.<BookLoanResponseDTO>builder()
                .status("success")
                .message("Book returned successfully")
                .data(responseDTO)
                .build();
    }

    @Override
    public BaseResponse<Page<BookLoanResponseDTO>> getLoansByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException.UserNotFoundException("User with id " + userId + " not found"));
        Page<BookLoan> loans = bookLoanRepository.findByUserId(user.getId(), pageable);
        Page<BookLoanResponseDTO> response = loans.map(bookloanMapper::toBookLoanResponseDTO);

        return BaseResponse.<Page<BookLoanResponseDTO>>builder()
                .status("success")
                .message("Loans retrieved successfully")
                .data(response)
                .build();
    }

    @Override
    public BaseResponse<BookLoanResponseDTO> calculateFine(Long loanId) {
        return null;
    }

    @Override
    public BaseResponse<BookLoanResponseDTO> payFine(Long loanId) {
        return null;
    }
}
