package pl.java.homebudget.exception;

public class AssetNotFoundException extends RuntimeException {
    public AssetNotFoundException() {
    }

    public AssetNotFoundException(String message) {
        super(message);
    }
}
