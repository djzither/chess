package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import requestobjects.CreateGameRequest;
import requestobjects.JoinGameRequest;
import requestobjects.LoginRequest;
import requestobjects.RegisterRequest;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private String authToken;
    //so i can pus

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }
    @BeforeEach
    void clearDatabase() throws ResponseException{
        facade.clear();
        var result = facade.register(new RegisterRequest("derek", "derek", "derek"));
        authToken = result.authToken();
    }

    @Test
    public void registerPos() throws ResponseException{
        var result = facade.register(new RegisterRequest("D", "D", "D"));
        assertNotNull(result);
    }
    @Test
    public void registerNeg() throws ResponseException{

        assertThrows(ResponseException.class, () -> {
            facade.register((new RegisterRequest("DEREK", "DEREK", null)));

        });
    }

    @Test
    public void loginPos() throws ResponseException{
        var result = facade.login(new LoginRequest("derek", "derek"));
        assertNotNull(result);
    }
    @Test
    public void loginNeg() throws ResponseException{
        assertThrows(ResponseException.class, () -> {
            facade.login((new LoginRequest("DEREK", null)));
        });
    }
    @Test
    public void clearPos() throws ResponseException{
        facade.clear();
        assertThrows(ResponseException.class, () -> {
            facade.login((new LoginRequest("DEREK", "DEREK")));
        });
    }

    @Test
    public void createGamePos() throws ResponseException{

        var result = facade.createGame(new CreateGameRequest("test", authToken));
        assertNotNull(result);
    }
    @Test
    public void createGameNeg() throws ResponseException{
        facade.logout();
        assertThrows(ResponseException.class, () -> {
            facade.createGame((new CreateGameRequest("DEREK", "BAD")));
        });
    }

    @Test
    public void logoutPos() throws ResponseException{
        facade.logout();
        assertThrows(ResponseException.class, () -> {
            facade.joinGame(new JoinGameRequest("WHITE", 1));
        });
    }

    @Test
    public void logoutNeg() throws ResponseException{
        facade.logout();
        assertThrows(ResponseException.class, () -> {
            facade.logout();
        });

    }

    @Test
    public void listPos() throws ResponseException{
        facade.createGame(new CreateGameRequest("test", authToken));
        var result = facade.listGames();
        assertNotNull(result);
    }

    @Test
    public void listneg() throws ResponseException{
        facade.createGame(new CreateGameRequest("test", authToken));
        facade.clear();
        assertThrows(ResponseException.class, () -> {
            facade.listGames();
        });
    }

    @Test
    public void joinPos() throws ResponseException{
        facade.createGame(new CreateGameRequest("test", authToken));
        facade.joinGame(new JoinGameRequest("WHITE", 1));
    }

    @Test
    public void joinNeg() throws ResponseException{
        assertThrows(ResponseException.class, ()->{
            facade.joinGame(new JoinGameRequest("WHITE", 12));
        });
    }


    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
