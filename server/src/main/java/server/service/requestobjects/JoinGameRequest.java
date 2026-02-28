package server.service.requestobjects;

public record JoinGameRequest(String playerColor, String gameID) {
    public String getPlayerColor() {
        return playerColor;
    }
    public String getGameID(){
        return gameID;
    }
}
