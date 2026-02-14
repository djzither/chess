package dataaccess.exceptions;

import dataaccess.DataAccessException;

public class UserNameTakenException extends DataAccessException {
    public UserNameTakenException(String message) {
        super(message);
    }
}
