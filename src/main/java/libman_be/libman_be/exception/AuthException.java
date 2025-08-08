package libman_be.libman_be.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }

    public static class InvalidCredentialsException extends AuthException {
        public InvalidCredentialsException(String message) {
            super(message);
        }
    }
}