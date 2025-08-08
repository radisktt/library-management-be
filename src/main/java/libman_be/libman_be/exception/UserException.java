package libman_be.libman_be.exception;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }

    public static class UserNotFoundException extends UserException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }

    public static class UserAlreadyExistsException extends UserException {
        public UserAlreadyExistsException(String email) {
            super("User already exists with email: " + email);
        }
    }

    public static class UnauthorizedAccessException extends UserException {
        public UnauthorizedAccessException(String message) {
            super(message);
        }
    }

    public static class EmailUpdateNotAllowedException extends UserException {
        public EmailUpdateNotAllowedException(String message) {
            super(message);
        }
    }

    public static class RoleUpdateNotAllowedException extends UserException {
        public RoleUpdateNotAllowedException(String message) {
            super(message);
        }
    }
}