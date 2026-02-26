package server.service.requestobjects;

public record LoginRequest(String username, String password) {
    public String getUserName() {
        return username;
    }
    public String getPassword(){
        return password;
    }
}
