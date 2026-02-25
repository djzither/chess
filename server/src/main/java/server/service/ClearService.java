package server.service;

import dataaccess.DataAccess;

public class ClearService {
    private DataAccess daoClear;

    public ClearService(DataAccess daoClear) {
        this.daoClear = daoClear;
    }

    public void clear() {
        daoClear.clear();
    }
}
