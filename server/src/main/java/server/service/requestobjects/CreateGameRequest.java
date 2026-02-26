package server.service.requestobjects;

public record CreateGameRequest(String gameID) {
    public String getGameId(){
        return gameID;
    }
}
