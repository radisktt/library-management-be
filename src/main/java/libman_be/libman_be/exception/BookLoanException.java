package libman_be.libman_be.exception;

public class BookLoanException extends RuntimeException {
  public BookLoanException(String message) {
    super(message);
  }

  public static class BookLoanNotFoundException extends BookLoanException {
    public BookLoanNotFoundException(String message) {
      super(message);
    }
  }

  public static class MaxBooksBorrowedException extends BookLoanException {
    public MaxBooksBorrowedException(String message) {
      super(message);
    }
  }

  public static class OverdueBookException extends BookLoanException {
    public OverdueBookException(String message) {
      super(message);
    }
  }

  public static class NoFineException extends BookLoanException {
    public NoFineException(String message) {
      super(message);
    }
  }

  public static class BookAlreadyReturnedException extends BookLoanException {
    public BookAlreadyReturnedException(String message) {
      super(message);
    }
  }

  public static class FineAlreadyPaidException extends BookLoanException {
    public FineAlreadyPaidException(String message) {
      super(message);
    }
  }

  public static class FineNotPaidException extends BookLoanException {
    public FineNotPaidException(String message) {
      super(message);
    }
  }

  public static class InvalidDueDateException extends BookLoanException {
    public InvalidDueDateException(String message) {
      super(message);
    }
  }
}