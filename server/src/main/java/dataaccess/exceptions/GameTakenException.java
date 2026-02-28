package dataaccess.exceptions;

public class GameTakenException extends RuntimeException {
    public GameTakenException(String message) {
        super(message);
    }
}
