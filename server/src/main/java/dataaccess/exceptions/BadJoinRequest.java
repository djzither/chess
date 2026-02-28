package dataaccess.exceptions;

public class BadJoinRequest extends RuntimeException {
    public BadJoinRequest(String message) {
        super(message);
    }
}
