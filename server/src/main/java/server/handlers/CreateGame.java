package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import io.javalin.http.Context;
import server.service.GameService;
import server.service.requestobjects.CreateGameRequest;
import server.service.requestobjects.CreateGameResult;
import server.service.requestobjects.ErrorResponseResult;

public class CreateGame {
    private final GameService gameService;

    public CreateGame(GameService gameService) {
        this.gameService = gameService;
    }
    public void handle(Context context){
        try {
            String authToken = context.header("authorization");
            CreateGameRequest request = new Gson().fromJson(context.body(), CreateGameRequest.class);
            String gameName = request.getGameName();
//

            int gamesID = gameService.createGame(authToken, gameName);
            CreateGameResult gamesObj = new CreateGameResult(gamesID);

            context.status(200);
            context.result(new Gson().toJson(gamesObj));
        }catch (BadRequestException e){
            context.status(400);
            context.result(new Gson().toJson(new ErrorResponseResult(e.getMessage())));
        }catch(UnauthorizedException e) {
            context.status(401);
            context.result(new Gson().toJson(new ErrorResponseResult(e.getMessage())));
        }
        catch(DataAccessException e) {
            context.status(500);
            context.result(new Gson().toJson(new ErrorResponseResult(e.getMessage())));
        }
    }

}
