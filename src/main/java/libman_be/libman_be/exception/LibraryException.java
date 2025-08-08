package libman_be.libman_be.exception;

public class LibraryException extends RuntimeException {
    public LibraryException(String message) {
        super(message);
    }

    public static class LibraryNotFoundException extends LibraryException {
        public LibraryNotFoundException(String message) {
            super(message);
        }
    }

    public static class LibraryAlreadyExistsException extends LibraryException {
        public LibraryAlreadyExistsException(String message) {
            super(message);
        }
    }
}