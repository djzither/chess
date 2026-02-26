package server;
import dataaccess.DataAccess;
import dataaccess.SysMemory;
import io.javalin.*;
import server.handlers.*;
import server.service.ClearService;
import server.service.GameService;
import server.service.UserService;
import io.javalin.json.JavalinGson;

public class Server {

    private final Javalin javalin;

    //still trying to wrap my head around this part...

    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        DataAccess dao = new SysMemory();
        UserService userService = new UserService(dao);
        Registration registration = new Registration(userService);
        javalin.post("/user", registration::handle);
        //clear


        ClearService clearService = new ClearService(dao);
        ClearApplication clear = new ClearApplication(clearService);
        javalin.delete("/db", clear::handle);

        //login
        Login login = new Login(userService);
        javalin.post("/session", login::handle);

        //
        Logout logout = new Logout(userService);
        javalin.delete("/session", logout::handle);



        //game service
        GameService gameService = new GameService(dao);
        ListGames listGames = new ListGames(gameService);
        javalin.get("/game", listGames::handle);


        //game service

        CreateGame createGame = new CreateGame(gameService);
        javalin.post("/game", createGame::handle);






    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
