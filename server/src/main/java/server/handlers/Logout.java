package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.service.UserService;
import server.service.requestobjects.LogoutRequest;

import javax.xml.crypto.Data;


public class Logout implements Handler {
    private final UserService userService;

    public Logout(UserService userService) {
        this.userService = userService;
    }

    public void handle(Context context){

        try{
            //does this work the AUTHORIZATION THING
            String authToken = context.header("authorization");
            userService.logout(new LogoutRequest(authToken));
            context.status(200);
            context.result(new Gson().toJson(""));


        }catch (UnauthorizedException e){
            context.status(401).result(new Gson().toJson(e.getMessage()));
        }catch(DataAccessException e){
            context.status(500).result(new Gson().toJson(e.getMessage()));
        }
    }

}
