package server.service.requestobjects;

public record CreateGameResult(int gameID) {

    public int gameID() {
        return gameID;
    }
}
