package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.BadCreationRequest;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.exceptions.UserNameTakenException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.service.UserService;
import server.service.requestobjects.ErrorResponseResult;
import server.service.requestobjects.LoginRequest;
import server.service.requestobjects.RegisterLoginResult;
import server.service.requestobjects.RegisterRequest;

import javax.xml.crypto.Data;

public class Login implements Handler{
    private final UserService userService;

    public Login(UserService userService) {
        this.userService = userService;
    }

    public void handle(Context context){
        try {

            LoginRequest loginRequest = new Gson().fromJson(context.body(), LoginRequest.class);
            RegisterLoginResult result = userService.login(loginRequest);

            context.status(200);
            context.json(result);
        } catch (BadCreationRequest e){
            context.status(400);
            context.json(new ErrorResponseResult(e.getMessage()));
        } catch(UnauthorizedException e){
            context.status(401);
            context.json(new ErrorResponseResult(e.getMessage()));
        } catch(DataAccessException e){
            context.status(500);
            context.json(new ErrorResponseResult(e.getMessage()));
        }
    }
}
