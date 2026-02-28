package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import server.service.ClearService;

public class ClearApplication implements Handler {
    private final ClearService clearService;


    public ClearApplication(ClearService clearService) {
        this.clearService = clearService;
    }
    public void handle(Context context) throws DataAccessException {
        clearService.clear();
        context.status(200);


        context.result("{}");//


    }


}//
//
