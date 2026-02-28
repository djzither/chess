package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.GameData;
import server.service.GameService;
import server.service.requestobjects.AuthTokenRequest;
import server.service.requestobjects.ErrorResponseResult;
import server.service.requestobjects.ListGamesResult;

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
            ListGamesResult gamesObj = new ListGamesResult(games);
            context.status(200);
            context.json(gamesObj);

        }catch (UnauthorizedException e){
            context.status(401);
            context.json(new ErrorResponseResult(e.getMessage()));
        }catch(DataAccessException e){
            context.status(500);
            context.json(new ErrorResponseResult(e.getMessage()));
        }

    }

}
