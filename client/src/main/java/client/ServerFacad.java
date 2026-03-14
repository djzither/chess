package client;

import model.GameData;
import server.service.requestobjects.*;

public class ServerFacad {


    public static RegisterLoginResult register(RegisterRequest request) {
    }

    public static CreateGameResult createGame(CreateGameRequest gameName) {
        return null;
    }

    public static String listGames() {
        return null;
    }



    public static GameData getGame(int gameId) {
        return null;
    }

    public static void logout() {
    }


    public static RegisterLoginResult login(LoginRequest loginRequest) {
        return null;
    }

    public static void joinGame(JoinGameRequest joinGameRequest) {
    }
}
