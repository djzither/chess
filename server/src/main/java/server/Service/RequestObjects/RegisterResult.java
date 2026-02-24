package server.Service.RequestObjects;

public record RegisterResult(boolean success, String userName, String authToken) {
    public boolean success(){
        return success;
    }
    public String userName(){
        return userName;
    }
    public String authToken(){
        return authToken;
    }
}
