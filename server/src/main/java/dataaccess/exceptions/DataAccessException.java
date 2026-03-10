package dataaccess.exceptions;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends ServiceException{
    public DataAccessException(String message) {
        super(message);
    }
    public DataAccessException(String message, Throwable ex) {
        super(message, ex);
    }
}
