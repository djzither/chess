package server.service.requestobjects;

public record CreateGameRequest(String gameName, String authToken) {
    public String getGameName(){
        return gameName;
    }
    public String getAuthToken(){
        return authToken;
    }
}
