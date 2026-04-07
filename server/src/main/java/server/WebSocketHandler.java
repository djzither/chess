package server;

import chess.*;
import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import requestobjects.GameParts;
import server.service.GameService;
import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;

import websocket.messages.LoadGame;
import websocket.messages.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final GameService gameService;

    public WebSocketHandler(GameService gameService) {
        this.gameService = gameService;
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
                case RESIGN -> handleResign(command, ctx.session);
                case MAKE_MOVE ->{
                    MoveCommand moveCmd = new Gson().fromJson(ctx.message(), MoveCommand.class);
                    handleMakeMove(moveCmd, ctx.session);
                }
            }

        } catch (IOException | DataAccessException ex) {
            ex.printStackTrace();

            //might need to be better with notification

        }
    }
    @Override
    public void handleClose(WsCloseContext ctx){

        System.out.println("websocket closed");
        connections.remove(ctx.session);
    }
    private void handleConnect(UserGameCommand cmd, Session session) throws IOException, DataAccessException {
        connections.add(session, cmd.getUsername(), cmd.getGameID(), cmd.isPlayer(), cmd.getColor(), cmd.getAuthToken());


        GameData gameData = gameService.getGame(cmd.getGameID());

        ChessGame game = gameData.game();
        //certain types
        LoadGame loadGame = new LoadGame(new Gson().toJson(game));
        //sends to client
        session.getRemote().sendString(new Gson().toJson(loadGame));

        var notif = new Notification(
                cmd.getUsername() + (cmd.isPlayer() ? " joined as " + cmd.getColor():
                "is observering"));
        connections.broadcast(session, notif, cmd.getGameID());


    }
    private void handleLeave(UserGameCommand cmd, Session session) throws IOException, DataAccessException {
        connections.remove(session);
        GameData gameData = gameService.getGame(cmd.getGameID());
        if (cmd.isPlayer()) {
            gameData = new GameData(
                    gameData.gameId(),
                    null,
                    gameData.blackUsername(),
                    gameData.gameName(),
                    gameData.game()
            );
        }else{
            new GameData(
                    gameData.gameId(),
                    gameData.whiteUsername(),
                    null,

                    gameData.gameName(),
                    gameData.game()
            );

        }
        var notif = new Notification(
                cmd.getUsername() + " left the game");
        connections.broadcast(session, notif, cmd.getGameID());
    }

    private void handleResign(UserGameCommand cmd, Session session) throws IOException{

        var notif = new Notification(
                cmd.getUsername() + " resigned");
        connections.broadcast(null, notif, cmd.getGameID());
    }
    private void handleMakeMove(MoveCommand cmd, Session session) throws IOException, DataAccessException, InvalidMoveException {
        GameData gameData = gameService.getGame(cmd.getGameID());
        ChessGame game = gameData.game();
        boolean promotionRow;
        ChessMove move;
        ChessPosition startPos = ChessPosition.fromString(cmd.getFrom());
        ChessPosition endPos = ChessPosition.fromString(cmd.getTo());
        ChessPiece piece = game.getBoard().getPiece(startPos);
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
            promotionRow = (piece.getTeamColor() == ChessGame.TeamColor.WHITE && endPos.getRow() == 1)
                    || (piece.getTeamColor() == ChessGame.TeamColor.BLACK && endPos.getRow() == 8);
            if (promotionRow) {

                ChessPiece.PieceType promoteTo = ChessPiece.PieceType.valueOf(cmd.getPromotion());
                move = new ChessMove(startPos, endPos, promoteTo);
                game.makeMove(move);
            }
        }
        else {

            move = new ChessMove(startPos, endPos, null);
            game.makeMove(move);
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
        LoadGame loadGame = new LoadGame(new Gson().toJson(game));
        connections.broadcast(null, loadGame, cmd.getGameID());

        var notif = new Notification(
                cmd.getUsername() + " made move");
        connections.broadcast(null, notif, cmd.getGameID());
    }
}
