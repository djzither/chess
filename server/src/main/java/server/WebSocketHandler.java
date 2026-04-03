package server;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MoveCommand;
import websocket.commands.UserGameCommand;

import websocket.messages.Notification;
import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();


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

        } catch (IOException ex) {
            ex.printStackTrace();
            //might need to be better with notification

        }
    }
    @Override
    public void handleClose(WsCloseContext ctx){
        System.out.println("websocket closed");

    }
    private void handleConnect(UserGameCommand cmd, Session session) throws IOException{
        connections.add(session, cmd.getUsername(), cmd.getGameID(), cmd.isPlayer(), cmd.getColor());
        var notif = new Notification(Notification.Type.NOTIFICATION,
                cmd.getUsername() + (cmd.isPlayer() ? " joined as " + cmd.getColor():
                "is observering"));
        connections.broadcast(session, notif, cmd.getGameID());


    }
    private void handleLeave(UserGameCommand cmd, Session session) throws IOException{
        connections.remove(session);
        var notif = new Notification(Notification.Type.NOTIFICATION,
                cmd.getUsername() + " left the game");
        connections.broadcast(session, notif, cmd.getGameID());
    }

    private void handleResign(UserGameCommand cmd, Session session) throws IOException{

        var notif = new Notification(Notification.Type.NOTIFICATION,
                cmd.getUsername() + " resigned");
        connections.broadcast(null, notif, cmd.getGameID());
    }
    private void handleMakeMove(MoveCommand cmd, Session session) throws IOException{
        var notif = new Notification(Notification.Type.NOTIFICATION,
                cmd.getUsername() + " made move");
        connections.broadcast(null, notif, cmd.getGameID());
    }
}
