package libman_be.libman_be.service;

import libman_be.libman_be.dto.BookLoanDTO;
import libman_be.libman_be.dto.response.BookLoanResponseDTO;
import libman_be.libman_be.dto.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookLoanService {
    BaseResponse<List<BookLoanResponseDTO>> borrowBooks(BookLoanDTO dto);
    BaseResponse<BookLoanResponseDTO> returnBook(Long loanId);
    BaseResponse<Page<BookLoanResponseDTO>> getLoansByUser(Long userId, Pageable pageable);
    BaseResponse<BookLoanResponseDTO> calculateFine(Long loanId);
    BaseResponse<BookLoanResponseDTO> payFine(Long loanId);
}