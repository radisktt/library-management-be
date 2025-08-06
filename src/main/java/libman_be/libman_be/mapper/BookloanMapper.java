package libman_be.libman_be.mapper;

import libman_be.libman_be.dto.BookLoanDTO;
import libman_be.libman_be.dto.response.BookLoanResponseDTO;
import libman_be.libman_be.entity.BookLoan;
import org.springframework.stereotype.Component;

@Component
public class BookloanMapper {
    public BookLoanResponseDTO toBookLoanResponseDTO(BookLoan bookLoan) {
        BookLoanResponseDTO dto = new BookLoanResponseDTO();
        dto.setId(bookLoan.getId());
        dto.setUserId(bookLoan.getUser().getId());
        dto.setUsername(bookLoan.getUser().getName());
        dto.setBookId(bookLoan.getBook().getId());
        dto.setBookTitle(bookLoan.getBook().getTitle());
        dto.setBorrowDate(bookLoan.getBorrowDate());
        dto.setDueDate(bookLoan.getDueDate());
        dto.setReturnDate(bookLoan.getReturnDate());
        dto.setStatus(bookLoan.getStatus());
        if(bookLoan.getFine() != null) {
            BookLoanResponseDTO.FineDTO fineDTO = new BookLoanResponseDTO.FineDTO();
            fineDTO.setId(bookLoan.getFine().getId());
            fineDTO.setAmount(bookLoan.getFine().getAmount());
            fineDTO.setIssuedDate(bookLoan.getFine().getIssuedDate());
            fineDTO.setPaid(bookLoan.getFine().isPaid());
            dto.setFine(fineDTO);
        }
        return dto;
    }
}
