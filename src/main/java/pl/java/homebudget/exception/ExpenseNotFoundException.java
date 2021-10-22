package pl.java.homebudget.exception;

public class ExpenseNotFoundException extends RuntimeException {
    public ExpenseNotFoundException() {
    }

    public ExpenseNotFoundException(String message) {
        super(message);
    }
}
