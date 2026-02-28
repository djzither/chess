package server.service;

import dataaccess.DataAccess;
import dataaccess.exceptions.DataAccessException;

public class ClearService {
    private DataAccess daoClear;

    public ClearService(DataAccess daoClear) {
        this.daoClear = daoClear;
    }

    public void clear() throws DataAccessException {
        daoClear.clear();
    }
}
