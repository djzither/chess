package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.GameData;
import server.service.GameService;
import server.service.requestobjects.AuthTokenRequest;

import java.util.List;

public class ListGames implements Handler{
    private final GameService gameService;

    public ListGames(GameService gameService) {
        this.gameService = gameService;
    }


    public void handle(Context context){
        try {
            String authToken = context.header("authorization");
            List<GameData> games = gameService.listGames(authToken);

            context.status(200);
            context.result(new Gson().toJson(games));

        }catch (UnauthorizedException e){
            context.status(401).result(new Gson().toJson(e.getMessage()));

        }catch(DataAccessException e){
            context.status(500).result(new Gson().toJson(e.getMessage()));
        }

    }

}
