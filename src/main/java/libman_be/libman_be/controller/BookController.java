package libman_be.libman_be.controller;

import libman_be.libman_be.dto.BaseResponse;
import libman_be.libman_be.dto.BookDTO;
import libman_be.libman_be.dto.response.BookResponseDTO;
import libman_be.libman_be.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<BaseResponse<BookDTO>> createBook(@RequestBody BookDTO bookRequest) {
        return ResponseEntity.ok(bookService.create(bookRequest));
    }
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<BookResponseDTO>> updateBook(@PathVariable Long id, @RequestBody BookDTO bookRequest) {
        return ResponseEntity.ok(bookService.update(id, bookRequest));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<String>> deleteBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.delete(id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<BookResponseDTO>> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }
    @GetMapping
    public ResponseEntity<BaseResponse<List<BookResponseDTO>>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAll());
    }

}
