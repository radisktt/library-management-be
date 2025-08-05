package libman_be.libman_be.exception;

public class AuthorException extends RuntimeException {
    public AuthorException(String message) {
        super(message);
    }

  public static class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {
      super(message);
    }
  }

  public static class AuthorAlreadyExistsException extends RuntimeException {
    public AuthorAlreadyExistsException(String message) {
      super(message);
    }
  }
}
