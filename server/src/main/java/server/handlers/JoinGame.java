package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.*;
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
            context.result(new Gson().toJson(""));

        }   catch (BadRequestException e) {
            context.status(400);
            context.result(new Gson().toJson(new ErrorResponseResult(e.getMessage())));

        }catch (UnauthorizedException e){
            context.status(401);
            context.result(new Gson().toJson(new ErrorResponseResult(e.getMessage())));



        } catch (AlreadyTakenException e) {
            context.status(403);
            context.result(new Gson().toJson(new ErrorResponseResult(e.getMessage())));
        } catch(DataAccessException e){
            context.status(500);
            context.result(new Gson().toJson(new ErrorResponseResult(e.getMessage())));
        }


    }


}
