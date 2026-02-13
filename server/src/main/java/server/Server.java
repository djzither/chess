package server;
import io.javalin.*;
import server.Handlers.Registration;

public class Server {

    private final Javalin javalin;

    //still trying to wrap my head around this part...

    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/user", ctx -> new Registration(userService).handle(ctx));
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
