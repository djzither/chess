package server;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.eclipse.jetty.server.session.Session;
import websocket.commands.UserGameCommand;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private Object Notificaitn;

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket Connected (can delete in Websocket handler if annoying");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> handleConnect(command, ctx.session);
                case LEAVE -> handleLeave(command, ctx.session);
                case RESIGN -> handleResign(command, ctx.session);
                case MAKE_MOVE -> handleMakeMove(cmd, ctx.session);

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
    private handleConnectCommand(UserGameCommand cmd, Session session) throws IOException{
        connections.add(session, cmd.username, cmd.getGameID(), cmd.isPlayer, cmd.color);
        var Notification = new Notification((Notification.Type.NOTIFICATION, cmd.username + (cmd.isPlayer ? " joined as " + cmd.color:
                "is observering")));
        connections.bradcast(session, notificiation, cmd.gameID);


    }
    private void handleLeave(UserGameCommand cmd, Session session) throws IOException{
        connections.remove(session);
        var Notification = new Notification(Notificaitn.Type.NOTIFICIATION,
                cmd.username + " left the game");
        connections.bradcast(session, Notification, cmd.getGameID());
    }

    private void handleResign(UserGameCommand cmd, Session session) throws IOException{

        var Notification = new Notification(Notificaitn.Type.NOTIFICIATION,
                cmd.username + " resigned");
        connections.bradcast(null, Notification, cmd.getGameID());
    }
    private void handleMakeMove(UserGameCommand cmd, Session session) throws IOException{
        var Notification = new Notification(Notificaitn.Type.NOTIFICIATION,
                cmd.username + " made move");
        connections.bradcast(null, Notification, cmd.getGameID());
    }
}
