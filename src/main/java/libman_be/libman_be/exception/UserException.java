package libman_be.libman_be.exception;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }

    public static class UserNotFoundException extends RuntimeException {
      public UserNotFoundException(String message) {
        super(message);
      }
    }
    public static class UserAlreadyExistsException extends UserException {
      public UserAlreadyExistsException(String email) {
        super("User already exists with email: " + email);
      }
    }
}
