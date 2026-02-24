package server.Service.RequestObjects;

public record RegisterRequest(String username, String password, String email) {
    public String getUserName(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getEmail(){
        return email;
    }
}
