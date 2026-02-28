package passoff.service;

import dataaccess.DataAccess;
import dataaccess.SysMemory;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import org.junit.jupiter.api.Test;
import server.service.UserService;
import server.service.requestobjects.AuthTokenRequest;
import server.service.requestobjects.LoginRequest;
import server.service.requestobjects.RegisterLoginResult;
import server.service.requestobjects.RegisterRequest;

import javax.sql.rowset.serial.SerialException;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTests {

    @Test
    void registerPositive() throws BadRequestException, AlreadyTakenException, DataAccessException {
        DataAccess dao = new SysMemory();
        UserService userService = new UserService(dao);
        String userName = "Derek";
        String password = "12";
        String email = "djzither@byu.edu";

        RegisterRequest registerRequst = new RegisterRequest(userName, password, email);

        RegisterLoginResult registerLoginResult = userService.register(registerRequst);

        assertTrue(registerLoginResult.success());
        assertEquals(userName, registerLoginResult.username());
        assertNotNull(registerLoginResult.authToken());
    }

    @Test
    void registerNegative() throws BadRequestException, AlreadyTakenException, DataAccessException{
        DataAccess dao = new SysMemory();
        UserService userService = new UserService(dao);
        String userName = "Derek";
        String password = "Robinson";
        String email = "dj";
        RegisterRequest registerRequst = new RegisterRequest(userName, password, email);

        userService.register(registerRequst);



        assertThrows(AlreadyTakenException.class, () -> {
            userService.register(registerRequst);
        });


    }
    @Test
    void loginPos() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException {

        DataAccess dao = new SysMemory();
        UserService userService = new UserService(dao);
        String userName = "Derek";
        String password = "12";
        String email = "djzither@byu.edu";

        RegisterRequest registerRequest = new RegisterRequest(userName, password, email);

        userService.register(registerRequest);


        LoginRequest loginRequest = new LoginRequest(userName, password);

        RegisterLoginResult loginResult = userService.login(loginRequest);

        assertTrue(loginResult.success());
        assertEquals(userName, loginResult.username());
        assertNotNull(loginResult.authToken());
    }

    @Test
    void loginNeg() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException {
        DataAccess dao = new SysMemory();
        UserService userService = new UserService(dao);
        String userName = "Derek";
        String password = "12";
        String email = "djzither@byu.edu";

        RegisterRequest registerRequest = new RegisterRequest(userName, password, email);
        userService.register(registerRequest);

        //login wrong
        LoginRequest badloginRequest = new LoginRequest(userName, "1");


        assertThrows(UnauthorizedException.class, () -> {
            userService.login(badloginRequest);
        });



    }



        @Test
    void logoutPos() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException {
            DataAccess dao = new SysMemory();
            UserService userService = new UserService(dao);
            String userName = "Derek";
            String password = "12";
            String email = "djzither@byu.edu";

            RegisterRequest registerRequest = new RegisterRequest(userName, password, email);
            userService.register(registerRequest);
            LoginRequest loginRequest = new LoginRequest(userName, password);
            RegisterLoginResult loginResult = userService.login(loginRequest);
            //logout
            String authToken = loginResult.authToken();
            AuthTokenRequest authTokenRequest = new AuthTokenRequest(authToken);
            userService.logout(authTokenRequest);

            assertNull(dao.getAuth(authToken));
        }

    @Test
    void logoutNeg() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException {
        DataAccess dao = new SysMemory();
        UserService userService = new UserService(dao);
        String userName = "Derek";
        String password = "12";
        String email = "djzither@byu.edu";

        RegisterRequest registerRequest = new RegisterRequest(userName, password, email);
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest(userName, password);
        RegisterLoginResult loginResult = userService.login(loginRequest);

        String authToken = loginResult.authToken();
        //logout

        AuthTokenRequest authTokenRequest = new AuthTokenRequest("iii");

        assertThrows(UnauthorizedException.class, () -> {
            userService.logout(authTokenRequest);
        });
    }

}