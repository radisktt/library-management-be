package libman_be.libman_be.exception;

public class BookException extends RuntimeException {
    public BookException(String message) {
        super(message);
    }
    public static class BookNotFoundException extends RuntimeException {
        public BookNotFoundException(String message) {
            super(message);
        }
    }

    public static class BookTitleAlreadyExistsException extends RuntimeException {
        public BookTitleAlreadyExistsException(String message) {
            super(message);
        }
    }
}
