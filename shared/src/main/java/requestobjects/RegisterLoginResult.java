package requestobjects;

public record RegisterLoginResult(boolean success, String username, String authToken) { }