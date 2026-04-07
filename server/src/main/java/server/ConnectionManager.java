package server;

import com.google.gson.Gson;

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
        String authToken;

        public ClientInfo(Session session, String username, int gameId, boolean isPlayer, String color, String authToken) {
            this.session = session;
            this.username = username;
            this.gameId = gameId;
            this.isPlayer = isPlayer;
            this.color = color;
            this.authToken = authToken;
        }
    }
    private final ConcurrentHashMap<Session, ClientInfo> connects = new ConcurrentHashMap<>();

    public void add(Session session, String username, int gameId, boolean isPlayer, String color, String authToken){
        connects.put(session, new ClientInfo(session, username, gameId, isPlayer, color, authToken));
    }

    public void remove(Session session){
        connects.remove(session);
    }

    public void broadcast(Session excludeSession, Object messageObject, int gameId) throws IOException{
        String msg = new Gson().toJson(messageObject);
        for (ClientInfo client : connects.values()){
            if(client.session.isOpen() && client.gameId == gameId){
                if(!client.session.equals(excludeSession)){
                    client.session.getRemote().sendString(msg);
                }
            }
        }
    }


}
