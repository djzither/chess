package server.service.requestobjects;

public record JoinGameRequest(String playerColor, int gameID) {
    public String getPlayerColor() {
        return playerColor;
    }
    public int getGameID(){
        return gameID;
    }
}
