package libman_be.libman_be.exception;

import libman_be.libman_be.dto.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<BaseResponse<Object>> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(BaseResponse.builder()
                .status("error")
                .message(message)
                .data(null)
                .build());
    }

    // Conflict (HTTP 409)
    @ExceptionHandler({
            UserException.UserAlreadyExistsException.class,
            CategoryException.CategoryAlreadyExistsException.class,
            BookException.BookTitleAlreadyExistsException.class,
            AuthorException.AuthorAlreadyExistsException.class,
            PublisherException.PublisherAlreadyExistsException.class,
            LibraryException.LibraryAlreadyExistsException.class
    })
    public ResponseEntity<BaseResponse<Object>> handleConflict(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Not Found (HTTP 404)
    @ExceptionHandler({
            UserException.UserNotFoundException.class,
            CategoryException.CategoryNotFoundException.class,
            BookException.BookNotFoundException.class,
            BookException.BookNotAvailableException.class,
            BookException.BookAlreadyBorrowedException.class,
            PublisherException.PublisherNotFoundException.class,
            AuthorException.AuthorNotFoundException.class,
            LibraryException.LibraryNotFoundException.class,
            BookLoanException.BookLoanNotFoundException.class
    })
    public ResponseEntity<BaseResponse<Object>> handleNotFound(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Forbidden (HTTP 403)
    @ExceptionHandler({
            UserException.UnauthorizedAccessException.class,
            UserException.RoleUpdateNotAllowedException.class
    })
    public ResponseEntity<BaseResponse<Object>> handleForbidden(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    // Bad Request (HTTP 400)
    @ExceptionHandler({
            UserException.EmailUpdateNotAllowedException.class,
            BookLoanException.MaxBooksBorrowedException.class,
            BookLoanException.OverdueBookException.class,
            BookLoanException.NoFineException.class,
            BookLoanException.BookAlreadyReturnedException.class,
            BookLoanException.FineAlreadyPaidException.class,
            BookLoanException.FineNotPaidException.class,
            BookLoanException.InvalidDueDateException.class,
            AuthException.InvalidCredentialsException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<BaseResponse<Object>> handleBadRequest(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Unauthorized (HTTP 401)
    @ExceptionHandler({
            TokenException.InvalidTokenException.class,
            TokenException.ExpiredTokenException.class,
            TokenException.BlacklistedTokenException.class
    })
    public ResponseEntity<BaseResponse<Object>> handleUnauthorized(TokenException ex) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    // Generic RuntimeException (HTTP 500)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse<Object>> handleGenericRuntimeException(RuntimeException ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }

    // Generic Exception (HTTP 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleGenericException(Exception ex) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }
}