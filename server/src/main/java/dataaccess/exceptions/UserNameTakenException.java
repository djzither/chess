package dataaccess.exceptions;

public class UserNameTakenException extends DataAccessException {
    public UserNameTakenException(String message) {
        super(message);
    }

}
