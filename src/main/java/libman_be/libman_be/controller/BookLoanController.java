package libman_be.libman_be.controller;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.BookLoanDTO;
import libman_be.libman_be.dto.response.BookLoanResponseDTO;
import libman_be.libman_be.service.BookLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-loan")
public class BookLoanController {

    @Autowired
    private BookLoanService bookLoanService;

    @PostMapping
    public ResponseEntity<BaseResponse<List<BookLoanResponseDTO>>> borrowBooks(@RequestBody BookLoanDTO bookLoanDTO) {
        return ResponseEntity.ok(bookLoanService.borrowBooks(bookLoanDTO));
    }
    @PutMapping("/return/{loanId}")
    public ResponseEntity<BaseResponse<BookLoanResponseDTO>> returnBook(@PathVariable Long loanId) {
        return ResponseEntity.ok(bookLoanService.returnBook(loanId));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<Page<BookLoanResponseDTO>>> getLoansByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));
        return ResponseEntity.ok(bookLoanService.getLoansByUser(userId, pageable));
    }
}
