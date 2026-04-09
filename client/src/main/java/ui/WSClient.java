package ui;

import chess.*;
import com.google.gson.Gson;
import jakarta.websocket.*;
import jakarta.websocket.Session;


import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessages;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;
import exception.ResponseException;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;



public class WSClient extends Endpoint{
    private Session session;

    private ChessGame game;
    private final Integer gameId;
    private final String authToken;
    private ChessGame.TeamColor playerColor;
    private final Gson gson = new Gson();
    private final String username;



    public WSClient(String serverUrl, Integer gameId, ChessGame currentGame, String authToken, String username) throws ResponseException {


        try {

            this.username = username;
            this.gameId = gameId;
            this.game = currentGame;
            this.authToken = authToken;
            serverUrl = serverUrl.replace("http", "ws") + "/ws";
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, new URI(serverUrl));

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    handleServerMessage(serverMessage, message);

                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }


    }
    @Override
    public void onOpen(Session session, EndpointConfig config){

    }

    private void handleServerMessage(ServerMessage msg, String rawJson){
        switch (msg.getServerMessageType()){
            case LOAD_GAME -> {
                LoadGame loadMsg = gson.fromJson(rawJson, LoadGame.class);
                game = loadMsg.getBoardinJson();



                MakeBoard board = new MakeBoard(game);
                board.makeBoard(playerColor, null, null);
            }
            case NOTIFICATION -> {
                Notification notification = gson.fromJson(rawJson, Notification.class);
                System.out.println("Notification! " + notification.getMessage());
            }
            case ERROR -> {
                ErrorMessages errorMessages = gson.fromJson(rawJson, ErrorMessages.class);
                System.err.println(""+ errorMessages.getErrorText());

            }
        }
    }

    public void connect(ChessGame.TeamColor color) throws ResponseException{
        this.playerColor = color;
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId);
        command.setUsername(username);
        if (color != null) {
            command.setPlayer(true);
        } else {
            command.setPlayer(false);
        }


        if (color != null){
            command.setColor(color.name());
        }
        sendCommand(command);


    }

    public void sendMove(String from, String to, String promotion) throws ResponseException{
        try {
            ChessPosition startPos = ChessPosition.fromString(from);
            ChessPosition endPos = ChessPosition.fromString(to);
            ChessPiece.PieceType promotionPiece = null;

            if (promotion != null) {
                promotionPiece = ChessPiece.PieceType.valueOf(promotion);
            }

            ChessMove move = new ChessMove(startPos, endPos, promotionPiece);
            MoveCommand cmd = new MoveCommand(authToken, gameId, move);
            cmd.setUsername(username);
            sendCommand(cmd);
        } catch (IllegalArgumentException e){
            throw new ResponseException(ResponseException.Code.ClientError,
                    "move must be in valid squares and in lower case and only include promotion if pawn " +
                            "at end");

        }
    }

    public void resign() throws ResponseException{

        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameId);
        command.setUsername(username);
        command.setPlayer(playerColor != null);
        if (playerColor != null) {
            command.setColor(playerColor.name());
        }
        sendCommand(command);
    }

    public void leavegame() throws ResponseException{
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameId);
        command.setUsername(username);
        command.setPlayer(playerColor != null);
        if (playerColor != null) {
            command.setColor(playerColor.name());
        }
        sendCommand(command);

        try{
            session.close();
        } catch (IOException ex){
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private void sendCommand(Object cmd) throws ResponseException{
        try{
            String json = gson.toJson(cmd);
            session.getBasicRemote().sendText(json);
        } catch (IOException e){
            throw new ResponseException(ResponseException.Code.ServerError, e.getMessage());
        }
    }
    public ChessGame getGame(){
        return game;
    }
}

