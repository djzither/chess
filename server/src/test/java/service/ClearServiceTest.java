package service;

import dataaccess.DataAccess;
import dataaccess.SysMemory;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import model.GameData;
import org.junit.jupiter.api.Test;
import server.service.ClearService;
import server.service.GameService;
import server.service.UserService;
import server.service.requestobjects.LoginRequest;
import server.service.requestobjects.RegisterLoginResult;
import server.service.requestobjects.RegisterRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClearServiceTest {
    @Test
    void clearServicePos() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException {
        DataAccess dao = new SysMemory();
        UserService userService = new UserService(dao);
        GameService gameService = new GameService(dao);
        String userName = "Derek";
        String password = "12";
        String email = "djzither@byu.edu";

        RegisterRequest registerRequest = new RegisterRequest(userName, password, email);
        userService.register(registerRequest);
        //login
        LoginRequest loginRequest = new LoginRequest(userName, password);
        RegisterLoginResult loginResult = userService.login(loginRequest);

        String authToken = loginResult.authToken();

        //make game
        String gameName = "game";
        int gameID = gameService.createGame(authToken, gameName);

        // did we actually make it
        GameData createdGame = dao.getGame(gameID);
        ClearService clearService = new ClearService(dao);
        clearService.clear();

        assertThrows(UnauthorizedException.class, () -> gameService.listGames(authToken));

    }

}
