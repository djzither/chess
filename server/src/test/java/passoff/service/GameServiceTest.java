package passoff.service;

import dataaccess.DataAccess;
import dataaccess.SysMemory;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import model.GameData;
import org.junit.jupiter.api.Test;
import server.service.GameService;
import server.service.UserService;
import server.service.requestobjects.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    @Test
    void listGamesPos() throws UnauthorizedException, DataAccessException, BadRequestException, AlreadyTakenException {
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
        AuthTokenRequest authTokenRequest = new AuthTokenRequest(authToken);

        //game service stuff
        GameService gameService = new GameService(dao);
        List<GameData> gameData = gameService.listGames(authToken);

        assertTrue(gameData.isEmpty());


    }

    @Test
    void listGamesNeg() throws UnauthorizedException, DataAccessException, BadRequestException, AlreadyTakenException {
        DataAccess dao = new SysMemory();
        UserService userService = new UserService(dao);
        String userName = "Derek";
        String password = "12";
        String email = "djzither@byu.edu";

        RegisterRequest registerRequest = new RegisterRequest(userName, password, email);
        userService.register(registerRequest);


        //game service stuff
        GameService gameService = new GameService(dao);
        String invalidToken = "oo";

        assertThrows(UnauthorizedException.class, () -> {
            gameService.listGames(invalidToken);
        });

    }

    @Test
    void createGame() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException {
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

        assertEquals(gameName, createdGame.gameName());
        assertNull(createdGame.whiteUsername());
        assertNull(createdGame.blackUsername());

    }
    @Test
    void createGameNeg() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException {
        DataAccess dao = new SysMemory();
        UserService userService = new UserService(dao);
        GameService gameService = new GameService(dao);
        String userName = "Derek";
        String password = "12";
        String email = "djzither@byu.edu";

        RegisterRequest registerRequest = new RegisterRequest(userName, password, email);
        userService.register(registerRequest);


        LoginRequest loginRequest = new LoginRequest(userName, password);

        RegisterLoginResult loginResult = userService.login(loginRequest);

        String invalidAuth = "ii";

        assertThrows(UnauthorizedException.class, () -> {
            gameService.createGame(invalidAuth, "EE");
        });

    }

    @Test
    void joinGamePos() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException {
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

        gameService.joinGame(authToken, "BLACK", gameID);


        GameData updatedGame = dao.getGame(gameID);


        assertEquals(gameName, updatedGame.gameName());
        assertNull(updatedGame.whiteUsername());
        assertEquals(userName, updatedGame.blackUsername());



    }

    @Test
    void joinGameNeg() throws BadRequestException, AlreadyTakenException, DataAccessException, UnauthorizedException {
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

        assertThrows(BadRequestException.class, () -> {
            gameService.joinGame(authToken, "BLACK", 10000); // non-existent game
        });



    }

}