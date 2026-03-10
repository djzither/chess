package dataaccess.exceptions;

public abstract class ServiceException extends Exception {
    public ServiceException(String message) {
        super("Error: " + message);
    }
    public ServiceException(String message, Throwable ex){
        super("Error " + message, ex);
    }
}
