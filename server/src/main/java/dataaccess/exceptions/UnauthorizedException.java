package dataaccess.exceptions;

public class UnauthorizedException extends ServiceException {
    public UnauthorizedException() {
        super("unauthorized");
    }
}
