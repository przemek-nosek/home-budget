package pl.java.homebudget.exception;


public class AppUserInvalidUsernameOrPasswordException extends RuntimeException {
    public AppUserInvalidUsernameOrPasswordException() {
    }

    public AppUserInvalidUsernameOrPasswordException(String message) {
        super(message);
    }
}
