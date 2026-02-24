package server.Handlers;
import dataaccess.exceptions.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.Service.RequestObjects.RegisterRequest;
import server.Service.RequestObjects.RegisterResult;
import server.Service.UserService;
import com.google.gson.Gson;



public class Registration implements Handler{
    private final UserService userService;

    public Registration(UserService userService) {
        this.userService = userService;
    }

    public void handle(Context context){


        try {
            RegisterRequest registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);
            RegisterResult result = userService.register(registerRequest);

            context.status(201);
            context.result(new Gson().toJson(result));

        } catch (DataAccessException){
            context.status(403).json("Error: " + e.getMessage());
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

}
