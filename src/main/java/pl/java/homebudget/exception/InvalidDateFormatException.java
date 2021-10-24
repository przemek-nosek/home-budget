package pl.java.homebudget.exception;

public class InvalidDateFormatException extends RuntimeException {
    public InvalidDateFormatException() {
    }

    public InvalidDateFormatException(String message) {
        super(message);
    }
}
