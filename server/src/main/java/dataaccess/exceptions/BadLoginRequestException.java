package dataaccess.exceptions;

public class BadLoginRequestException extends RuntimeException {
    public BadLoginRequestException(String message) {
        super(message);
    }
}
