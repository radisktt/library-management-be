package libman_be.libman_be.exception;

public class LibraryException extends RuntimeException {
    public LibraryException(String message) {
        super(message);
    }
    public static class LibraryNotFoundException extends RuntimeException {
        public LibraryNotFoundException(String message) {
            super(message);
        }
    }
    public static class LibraryAlreadyExistsException extends UserException {
        public LibraryAlreadyExistsException(String message) {
            super(message);
        }
    }
}
