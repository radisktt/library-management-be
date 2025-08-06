package libman_be.libman_be.exception;

import libman_be.libman_be.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {
    private ResponseEntity<BaseResponse<Object>> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(BaseResponse.builder()
                .status("error")
                .message(message)
                .data(null)
                .build());
    }
    //Conflict
    @ExceptionHandler({
            UserException.UserAlreadyExistsException.class,
            CategoryException.CategoryAlreadyExistsException.class,
            BookException.BookTitleAlreadyExistsException.class,
    })
    public ResponseEntity<BaseResponse<Object>> handleConflict(RuntimeException ex){
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }
    // 404 Not found
    @ExceptionHandler({
            UserException.UserNotFoundException.class,
            CategoryException.CategoryNotFoundException.class,
            BookException.BookNotFoundException.class,
            BookException.BookNotAvailableException.class,
            BookException.BookAlreadyBorrowedException.class,
            PublisherException.PublisherNotFoundException.class,
            AuthorException.AuthorNotFoundException.class,
            LibraryException.LibraryNotFoundException.class
    })
    public ResponseEntity<BaseResponse<Object>> handleNotFound(RuntimeException ex){
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
