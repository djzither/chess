package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import jakarta.websocket.*;
import org.eclipse.jetty.websocket.api.WebSocketContainer;
import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import exception.ResponseException;


import javax.management.Notification;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Scanner;


public class WSClient {
    private Session session;
    private final ChessGame game;
    private final Integer gameId;
    private final String authToken;
    private ChessGame.TeamColor playerColor;
    private final Gson gson = new Gson();


    public WSClient(String serverUrl, Integer gameId, ChessGame currentGame, String authToken) {
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
                MakeBoard board = new MakeBoard(game);
                board.makeBoard(playerColor, null);
            }
            case NOTIFICATION -> {
                System.out.println("Notification! " + msg);
            }
            case ERROR -> {
                System.err.println("Error! "+ msg);

            }
        }
    }

    public void connect(ChessGame.TeamColor color) throws ResponseException{
        this.playerColor = color;
        sendCommand(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId))

    }

    public void connectToGame(ChessGame.TeamColor color) throws ResponseException{
        this.playerColor = color;
        sendCommand(new UserGameCommand((UserGameCommand.CommandType.CONNECT, authToken, gameId)))
    }

    public void sendMove(String from, String to) throws ResponseException{
        MoveCommand move = new MoveCommand(authToken, gameId, from, to);
        sendCommand(move);
    }

    public void resign() throws ResponseException{
        sendCommand(new UserGameCommand((UserGameCommand.CommandType.RESIGN, authToken, gameId)));

    }

    public void leavegame() throws ResponseException{
        sendCommand(new UserGameCommand((UserGameCommand.CommandType.LEAVE, authToken, gameId));
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

