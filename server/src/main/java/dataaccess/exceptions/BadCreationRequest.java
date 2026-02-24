package dataaccess.exceptions;

public class BadCreationRequest extends RuntimeException {
    public BadCreationRequest(String message) {
        super(message);
    }
}
