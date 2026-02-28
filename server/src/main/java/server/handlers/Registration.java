package server.handlers;
import dataaccess.exceptions.BadCreationRequest;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UserNameTakenException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.service.requestobjects.ErrorResponseResult;
import server.service.requestobjects.RegisterRequest;
import server.service.requestobjects.RegisterLoginResult;
import server.service.UserService;
import com.google.gson.Gson;


public class Registration implements Handler{
    private final UserService userService;

    public Registration(UserService userService) {
        this.userService = userService;
    }

    public void handle(Context context){

        //did it
        try {
            RegisterRequest registerRequest = new Gson().fromJson(context.body(), RegisterRequest.class);
            RegisterLoginResult result = userService.register(registerRequest);

            context.status(200);
            context.json(result);

        } catch (BadCreationRequest e){
            context.status(400);
            context.json(new ErrorResponseResult(e.getMessage()));
        } catch (UserNameTakenException e) {
            context.status(403);
            context.json(new ErrorResponseResult(e.getMessage()));
            //this works

        } catch(DataAccessException e){
            context.status(500);
            context.json(new ErrorResponseResult(e.getMessage()));
        }

    }

}








