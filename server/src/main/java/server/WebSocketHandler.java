package server;

import chess.*;
import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import io.javalin.websocket.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import requestobjects.GameParts;
import server.service.GameService;
import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;
import server.service.UserService;
import websocket.messages.ErrorMessages;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final GameService gameService;
    private final UserService userService;

    public WebSocketHandler(GameService gameService, UserService userService) {
        this.gameService = gameService;
        this.userService = userService;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {

        System.out.println("Websocket Connected (can delete in Websocket handler if annoying");

        ctx.enableAutomaticPings();

    }

    @Override
    public void handleMessage(WsMessageContext ctx) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> handleConnect(command, ctx.session);
                case LEAVE -> handleLeave(command, ctx.session);
                case RESIGN ->
                        handleResign(command, ctx.session);
                case MAKE_MOVE ->{
                    MoveCommand moveCmd = new Gson().fromJson(ctx.message(), MoveCommand.class);
                    handleMakeMove(moveCmd, ctx.session);
                }
            }

        } catch (IOException | DataAccessException | InvalidMoveException ex ) {
            ex.printStackTrace();

            var notif = new ErrorMessages(ex.getMessage());
            ctx.session.getRemote().sendString(new Gson().toJson(notif));
        }
    }
    @Override
    public void handleClose(WsCloseContext ctx){

        System.out.println("websocket closed");
        connections.remove(ctx.session);
    }
    private void handleConnect(UserGameCommand cmd, Session session) throws IOException, DataAccessException {
        if (!userService.isValidAuthToken(cmd.getAuthToken())){
            var err = new ErrorMessages("invalid or missing auth");
            session.getRemote().sendString(new Gson().toJson(err));
            return;
        }

        GameData gameData = gameService.getGame(cmd.getGameID());
        if (gameData == null){
            var err = new ErrorMessages("Error this game id doesn't exist");
            session.getRemote().sendString(new Gson().toJson(err));
            return;
        }

        connections.add(session, cmd.getUsername(), cmd.getGameID(), cmd.isPlayer(), cmd.getColor(), cmd.getAuthToken());

        ChessGame game = gameData.game();
        //certain types
        LoadGame loadGame = new LoadGame(game);
        //sends to client
        session.getRemote().sendString(new Gson().toJson(loadGame));

        var notif = new Notification(
                cmd.getUsername() + (cmd.isPlayer() ? " joined as " + cmd.getColor() :
                " is observering"));
        connections.broadcast(session, notif, cmd.getGameID());


    }
    private void handleLeave(UserGameCommand cmd, Session session) throws IOException, DataAccessException {
        connections.remove(session);
        GameData gameData = gameService.getGame(cmd.getGameID());

        String username = userService.getUsernameFromAuth(cmd.getAuthToken());
        if (Objects.equals(username, gameData.whiteUsername())) {
            gameData = new GameData(
                    gameData.gameId(),
                    null,
                    gameData.blackUsername(),
                    gameData.gameName(),
                    gameData.game()
            );
        }else if (username.equals(gameData.blackUsername())){
            gameData = new GameData(
                    gameData.gameId(),
                    gameData.whiteUsername(),
                    null,

                    gameData.gameName(),
                    gameData.game()
            );

        }
        gameService.updateGame(gameData);

        var notif = new Notification(
                username + " left the game");
        connections.broadcast(session, notif, cmd.getGameID());
    }

    private void handleResign(UserGameCommand cmd, Session session) throws IOException, DataAccessException {
        GameData gameData = gameService.getGame(cmd.getGameID());
        ChessGame game = gameData.game();

        String username = userService.getUsernameFromAuth(cmd.getAuthToken());

        ChessGame.TeamColor playerColor = getPlayerColor(username, gameData);

        if(playerColor== null){
            var error = new ErrorMessages("You are observer and cant resign...");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }

        if (game.isGameOver()){
            var error = new ErrorMessages("Game is already over");
            session.getRemote().sendString(new Gson().toJson(error));
            return;
        }


        game.resign(playerColor);

        GameData updatedGame = new GameData(
                gameData.gameId(),
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                game
        );
        gameService.updateGame(updatedGame);

        var notif = new Notification(
                username + " resigned");
        connections.broadcast(null, notif, cmd.getGameID());
    }

    private void handleMakeMove(MoveCommand cmd, Session session) throws IOException, DataAccessException, InvalidMoveException {
        GameData gameData = gameService.getGame(cmd.getGameID());
        ChessGame game = gameData.game();
        ChessMove move = cmd.getMove();
        String username = userService.getUsernameFromAuth(cmd.getAuthToken());


        ChessGame.TeamColor playerColor = getPlayerColor(username, gameData);
        if (playerColor == null || game.getTeamTurn() != playerColor) {
            var err = new ErrorMessages("unable to make move");
            session.getRemote().sendString(new Gson().toJson(err));
            return;
        }
        String lostName = null;
        String winnerName = null;
        game.makeMove(move);
        if (game.isInCheckmate(game.getTeamTurn())){
            ChessGame.TeamColor lost = game.getTeamTurn();

            if (lost == ChessGame.TeamColor.WHITE){
                lostName = gameData.whiteUsername();
            }
            else{
                lostName = gameData.blackUsername();
            }

            if (lost == ChessGame.TeamColor.WHITE){
                winnerName = gameData.blackUsername();
            }
            else{
                winnerName = gameData.whiteUsername();
            }

        }





        if (game.isInCheckmate(game.getTeamTurn())){
            var gameOver= new Notification("Checkmate: Game Over " + lostName + " was defeated by " + winnerName);
            connections.broadcast(null, gameOver, cmd.getGameID());


        }else if(game.isInStalemate(game.getTeamTurn())){
            var gameOver = new Notification("Stalemate: Game Over");
            connections.broadcast(null, gameOver, cmd.getGameID());
        }else if(game.isInCheck(game.getTeamTurn())){
            var check = new Notification("");
        }
        ChessGame.TeamColor inCheck = game.getTeamTurn();
        if (game.isInCheck(inCheck)) {
            String inCheckName = inCheck == ChessGame.TeamColor.WHITE
                    ? gameData.whiteUsername()
                    : gameData.blackUsername();
            var checkNotif = new Notification(inCheckName + " is in check!");
            connections.broadcast(null, checkNotif, cmd.getGameID());
        }

        GameData updatedGame = new GameData(
                gameData.gameId(),
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName(),
                game
        );
        gameService.updateGame(updatedGame);
        //get the game and send to players
        LoadGame loadGame = new LoadGame(updatedGame.game());
        connections.broadcast(null, loadGame, cmd.getGameID());
        String moveStr = positionToInput(move.getStartPosition()) + " -> " +
                positionToInput(move.getEndPosition());
        var notif = new Notification(
                username + " made move: " + moveStr );
        connections.broadcast(session, notif, cmd.getGameID());
    }
    private ChessGame.TeamColor getPlayerColor(String username, GameData gamedata){
        ChessGame.TeamColor playerColor = null;

        if (username != null) {
            if (username.equals(gamedata.whiteUsername())) {
                return ChessGame.TeamColor.WHITE;
            } else if (username.equals(gamedata.blackUsername())) {
                return ChessGame.TeamColor.BLACK;
            }

        }
        return null;

    }
    private String positionToInput(ChessPosition position) {
        char file = (char) ('a' + position.getColumn() - 1);
        int rank = position.getRow();
        return "" + file + rank;
    }

}
