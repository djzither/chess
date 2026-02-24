package server;
import dataaccess.CreateUpdateUserData;
import dataaccess.DataAccess;
import io.javalin.*;
import server.Handlers.Registration;
import server.Service.UserService;

public class Server {

    private final Javalin javalin;

    //still trying to wrap my head around this part...

    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        DataAccess dao = new CreateUpdateUserData();
        UserService userService = new UserService(dao);
        javalin.post("/user",  new Registration(userService));
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
