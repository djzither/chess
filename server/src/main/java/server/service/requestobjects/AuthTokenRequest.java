package server.service.requestobjects;

public record AuthTokenRequest(String authToken) {

    public String getAuthToken() {
        return authToken;
    }
}
