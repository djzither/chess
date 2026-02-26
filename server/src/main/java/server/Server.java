package server;
import dataaccess.DataAccess;
import dataaccess.SysMemory;
import io.javalin.*;
import server.handlers.ClearApplication;
import server.handlers.Login;
import server.handlers.Registration;
import server.service.ClearService;
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


    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
