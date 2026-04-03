package server;

import org.eclipse.jetty.websocket.api.Session;

import websocket.messages.Notification;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {


    private static class ClientInfo{
        Session session;
        String username;
        int gameId;
        boolean isPlayer;
        String color;

        public ClientInfo(Session session, String username, int gameId, boolean isPlayer, String color) {
            this.session = session;
            this.username = username;
            this.gameId = gameId;
            this.isPlayer = isPlayer;
            this.color = color;
        }
    }
    private final ConcurrentHashMap<Session, ClientInfo> connects = new ConcurrentHashMap<>();

    public void add(Session session, String username, int gameId, boolean isPlayer, String color){
        connects.put(session, new ClientInfo(session, username, gameId, isPlayer, color));
    }

    public void remove(Session session){
        connects.remove(session);
    }

    public void broadcast(Session excludeSession, Notification notification, int gameId) throws IOException{
        String msg = notification.toString();
        for (ClientInfo client : connects.values()){
            if(client.session.isOpen() && client.gameId == gameId){
                if(!client.session.equals(excludeSession)){
                    client.session.getRemote().sendString(msg);
                }
            }
        }
    }
}
