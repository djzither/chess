package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthPasswordTests {
    private DataAccess dao;
    private String authToken;
    private String usernamegood;
    private AuthData authData;

    @BeforeEach
    void createAuthInsert() throws DataAccessException {
        dao = new MySqlDataAccess();
        dao.clear();
        authToken = "111";
        usernamegood = "derek";
        authData = new AuthData(
                authToken,
                usernamegood
        );



    }

    @Test
    void createAuth() throws DataAccessException {
        dao.createAuth(authData);
        assertEquals(authData, dao.getAuth(authToken));
    }


    @Test
    void createAuthNeg() throws DataAccessException {
        dao.createAuth(authData);
        assertNull(dao.getAuth("1111"));
    }

    @Test
    void getAuth() throws DataAccessException{
        dao.createAuth(authData);
        assertEquals(authData, dao.getAuth("111"));
    }
    @Test
    void getAuthNeg() throws DataAccessException{
        dao.createAuth(authData);
        assertNull(dao.getAuth("1111"));
    }

    @Test
    void deleteAuthPos() throws DataAccessException{
        dao.createAuth(authData);
        dao.deleteAuth(authToken);
        assertNull(dao.getAuth(authToken));
    }
    @Test
    void deleteAuthNeg() throws DataAccessException{
        dao.createAuth(authData);
        dao.deleteAuth("bad");
        assertEquals(authData, dao.getAuth(authToken));

    }

    @Test
    void authToUsername() throws DataAccessException {
        dao.createAuth(authData);
        String username = dao.authToUsername(authToken);
        assertEquals(usernamegood, username);

    }
    @Test
    void authToUsernameNeg() throws DataAccessException{
        dao.createAuth(authData);
        String username = dao.authToUsername("bad");
        assertNull(username);
    }


}