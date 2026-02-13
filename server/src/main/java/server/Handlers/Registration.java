package server.Handlers;
import com.google.gson.JsonObject;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.Service.RegisterRequest;
import server.Service.RegisterResult;
import server.Service.UserService;
import com.google.gson.Gson;
//will have to make this a try except


public class Registration implements Handler{
    private final UserService userService;
    private final Gson gson;
    public Registration(UserService userService) {
        this.userService = userService;
        this.gson = new Gson();
    }

    //@Override
    public void handle(Context ctx){
        try {
            RegisterRequest registerRequest = gson.fromJson(ctx.body(), RegisterRequest.class);


            RegisterResult result = userService.register(registerRequest);

        } catch ()

    }

}
