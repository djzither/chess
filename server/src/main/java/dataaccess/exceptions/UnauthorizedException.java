package dataaccess.exceptions;

public class UnauthorizedException extends ServiceException {
    public UnauthorizedException() {
        super("unauthorized");
    }
    public UnauthorizedException(String message) {
        super(message);
    }
}
