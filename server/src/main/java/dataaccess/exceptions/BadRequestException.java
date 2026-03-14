package dataaccess.exceptions;

public class BadRequestException extends ServiceException {
    public BadRequestException() {
        super("bad request");
    }
    public BadRequestException(String message){
        super(message);
    }
}
