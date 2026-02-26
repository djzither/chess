package dataaccess.exceptions;

public class BadLogoutRequestException extends RuntimeException {
    public BadLogoutRequestException(String message) {
        super(message);
    }
}
