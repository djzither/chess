package ui;

import chess.ChessGame;
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

    private final ChessGame game;
    private final Integer gameId;
    private final String authToken;
    private ChessGame.TeamColor playerColor;
    private final Gson gson = new Gson();


    public WSClient(String serverUrl, Integer gameId, ChessGame currentGame, String authToken) throws ResponseException {
        try {


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
                    handleServerMessage(serverMessage);

                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }


    }
    @Override
    public void onOpen(Session session, EndpointConfig config){

    }

    private void handleServerMessage(ServerMessage msg){
        switch (msg.getServerMessageType()){
            case LOAD_GAME -> {
                //type casts are cool
                LoadGame loadMsg = (LoadGame) msg;
                MakeBoard board = new MakeBoard(game);
                board.makeBoard(playerColor, loadMsg.getBoardinJson());
            }
            case NOTIFICATION -> {
                Notification notification = (Notification) msg;
                System.out.println("Notification! " + notification.getMessage());
            }
            case ERROR -> {
                ErrorMessages errorMessages = (ErrorMessages) msg;
                System.err.println("Error! "+ errorMessages.getErrorText());

            }
        }
    }

    public void connect(ChessGame.TeamColor color) throws ResponseException{
        this.playerColor = color;
        sendCommand(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId));

    }

    public void sendMove(String from, String to) throws ResponseException{
        MoveCommand move = new MoveCommand(authToken, gameId, from, to);
        sendCommand(move);
    }

    public void resign() throws ResponseException{
        sendCommand(new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameId));

    }

    public void leavegame() throws ResponseException{
        sendCommand(new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameId));
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
}

