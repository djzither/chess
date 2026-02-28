package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.BadCreationRequest;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import io.javalin.http.Context;
import server.service.GameService;
import server.service.requestobjects.ErrorResponseResult;
import server.service.requestobjects.JoinGameRequest;

public class JoinGame {
    private final GameService gameService;
    public JoinGame(GameService gameService) {
        this.gameService = gameService;
    }
    public void handle(Context context){
        try {
            String authToken = context.header("authorization");
            JoinGameRequest request = new Gson().fromJson(context.body(), JoinGameRequest.class);
            String playerColor = request.getPlayerColor();
            String gameID = request.gameID();
            gameService.joinGame(authToken, playerColor, gameID);

            context.status(200);
            context.result("{}");

        }catch (BadCreationRequest e) {
            context.status(400);
            context.json(new ErrorResponseResult(e.getMessage()));
        }catch (UnauthorizedException e){
            context.status(401);
            context.json(new ErrorResponseResult(e.getMessage()));
        }catch(DataAccessException e){
            context.status(500);
            context.json(new ErrorResponseResult(e.getMessage()));
        }


        }


}
