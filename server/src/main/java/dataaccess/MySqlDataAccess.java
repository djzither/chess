package dataaccess;


import dataaccess.exceptions.DataAccessException;

public class MySqlDataAccess {
    public MySqlDataAccess() throws DataAccessException{
        configureDatabase();
    }
}
