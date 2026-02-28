package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.service.UserService;
import server.service.requestobjects.AuthTokenRequest;
import server.service.requestobjects.ErrorResponseResult;


public class Logout implements Handler {
    private final UserService userService;

    public Logout(UserService userService) {
        this.userService = userService;
    }

    public void handle(Context context){

        try{
            //does this work the AUTHORIZATION THING
            String authToken = context.header("authorization");
            userService.logout(new AuthTokenRequest(authToken));

            context.status(200);
            context.result("{}");


        }catch (UnauthorizedException e){
            context.status(401);
            context.json(new ErrorResponseResult(e.getMessage()));
        }catch(DataAccessException e){
            context.status(500);
            context.json(new ErrorResponseResult(e.getMessage()));
        }
    }

}




