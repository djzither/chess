package server;
import dataaccess.DataAccess;
import dataaccess.SysMemory;
import io.javalin.*;
import server.handlers.Registration;
import server.service.UserService;

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
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
