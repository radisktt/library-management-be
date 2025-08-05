package libman_be.libman_be.exception;

public class PublisherException extends RuntimeException {
    public PublisherException(String message) {
        super(message);
    }
    public static class PublisherNotFoundException extends RuntimeException {
        public PublisherNotFoundException(String message) {
            super(message);
        }
    }
    public static class PublisherAlreadyExistsException extends UserException {
        public PublisherAlreadyExistsException(String message) {
            super(message);
        }
    }
}
