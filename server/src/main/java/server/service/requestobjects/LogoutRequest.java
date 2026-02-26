package server.service.requestobjects;

public record LogoutRequest(String authToken) {

    public String getAuthToken() {
        return authToken;
    }
}
