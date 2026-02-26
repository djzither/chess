package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.BadCreationRequest;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.exceptions.UserNameTakenException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.service.UserService;
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
            context.result(new Gson().toJson(result));
        } catch (BadCreationRequest e){
            context.status(400).result(new Gson().toJson(e.getMessage()));
        } catch(UnauthorizedException e){
            context.status(401).result(new Gson().toJson(e.getMessage()));
        } catch(DataAccessException e){
            context.status(500).result(new Gson().toJson(e.getMessage()));
        }
    }
}
