package dataaccess.exceptions;

public class AlreadyTakenException extends ServiceException{
    public AlreadyTakenException() {
        super("already taken");
    }
}
