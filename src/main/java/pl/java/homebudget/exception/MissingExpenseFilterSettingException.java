package pl.java.homebudget.exception;

public class MissingExpenseFilterSettingException extends RuntimeException{
    public MissingExpenseFilterSettingException() {
    }

    public MissingExpenseFilterSettingException(String message) {
        super(message);
    }
}
