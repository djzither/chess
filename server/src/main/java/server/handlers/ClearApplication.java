package server.handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.service.ClearService;

public class ClearApplication implements Handler {
    private final ClearService clearService;


    public ClearApplication(ClearService clearService) {
        this.clearService = clearService;
    }
    public void handle(Context context){
        clearService.clear();
        context.status(200);

        context.status(200);
        String json = new Gson().toJson("");
        context.result(new Gson().toJson(json));//


    }


}//
//
