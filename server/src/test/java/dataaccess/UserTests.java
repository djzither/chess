package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTests {
    private DataAccess dao;
    @BeforeEach
    public void setupDb() throws DataAccessException{
        dao = new MySqlDataAccess();

        dao.clear();
    }

    @Test
    public void createUserPos() throws DataAccessException {
        UserData userData = new UserData("derek", "none", "djzith");
        dao.createUser(userData);
        UserData result = dao.getUser("derek");
        assertEquals("derek", result.username());
        assertEquals("djzith", result.email());


    }
    @Test
    public void createUserNeg() throws DataAccessException{
        //can't havenull thing in database?
        UserData userData = new UserData(null, null, "djzith");

        assertThrows(DataAccessException.class, () ->
        {
            dao.createUser(userData);

        });

    }


    @Test
    void getUserPos() throws DataAccessException {
        UserData userData = new UserData("derek", "none", "djzith");
        dao.createUser(userData);
        UserData result = dao.getUser("derek");
        assertEquals(userData.username(), result.username());
    }

    @Test
    void getUserNeg() throws DataAccessException{
            UserData result = dao.getUser("google");
            assertEquals(null, result);

            }
}