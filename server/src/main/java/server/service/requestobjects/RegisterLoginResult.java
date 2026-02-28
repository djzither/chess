package server.service.requestobjects;

public record RegisterLoginResult(boolean success, String username, String authToken) { }