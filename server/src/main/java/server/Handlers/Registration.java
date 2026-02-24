package server.Handlers;
import dataaccess.exceptions.BadCreationRequest;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UserNameTakenException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.Service.RequestObjects.RegisterRequest;
import server.Service.RequestObjects.RegisterResult;
import server.Service.UserService;
import com.google.gson.Gson;

import java.util.Map;


public class Registration implements Handler{
    private final UserService userService;

    public Registration(UserService userService) {
        this.userService = userService;
    }

    public void handle(Context context){


        try {
            RegisterRequest registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);
            RegisterResult result = userService.register(registerRequest);

            context.status(200);
            context.result(new Gson().toJson(result));

        } catch (BadCreationRequest e){
            context.status(400);
            context.json(Map.of("success", false, "message", e.getMessage()));
        } catch (UserNameTakenException e) {
            context.status(403);
            context.json(Map.of("success", false, "message", e.getMessage()));
        } catch(DataAccessException e){
            context.status(500);
            context.json(Map.of("success", false, "message", e.getMessage()));
        }

    }

}
