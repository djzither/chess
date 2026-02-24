package server.service.requestobjects;

public record RegisterResult(boolean success, String userName, String authToken) {
    public boolean success(){
        return success;
    }
    public String username(){
        return userName;
    }
    public String authToken(){
        return authToken;
    }
}
