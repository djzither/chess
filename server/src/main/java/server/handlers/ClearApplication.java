package server.handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.service.ClearService;
import server.service.UserService;
import server.service.requestobjects.RegisterRequest;
import server.service.requestobjects.RegisterResult;

import java.util.Map;

public class ClearApplication implements Handler {
    private final ClearService clearService;


    public ClearApplication(ClearService clearService) {
        this.clearService = clearService;
    }
    public void handle(Context context){

    }


}//
