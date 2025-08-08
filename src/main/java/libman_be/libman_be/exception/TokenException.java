package libman_be.libman_be.exception;

public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }

    public static class InvalidTokenException extends TokenException {
        public InvalidTokenException(String message) {
            super(message);
        }
    }

    public static class ExpiredTokenException extends TokenException {
        public ExpiredTokenException(String message) {
            super(message);
        }
    }

    public static class BlacklistedTokenException extends TokenException {
        public BlacklistedTokenException(String message) {
            super(message);
        }
    }
}