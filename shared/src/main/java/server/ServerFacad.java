package server;

import com.google.gson.Gson;

import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import server.service.requestobjects.*;


import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacad {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacad(String url) {
        serverUrl = url;
    }


    public RegisterLoginResult register(RegisterRequest registerRequest)
            throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        var httpRequest = buildRequest("POST", "/user", registerRequest, null);
        var response = sendRequest(httpRequest);
        return handleResponse(response, RegisterLoginResult.class);
    }


    public CreateGameResult createGame(CreateGameRequest request)
            throws UnauthorizedException, DataAccessException {

        var httpRequest = buildRequest("POST", "/game", request, request.authToken());
        var response = sendRequest(httpRequest);
        return handleResponse(response, CreateGameResult.class);
    }


    public ListGamesResult listGames()
            throws UnauthorizedException, DataAccessException {

        var httpRequest = buildRequest("GET", "/game", null, authToken);
        var response = sendRequest(httpRequest);
        return handleResponse(response, ListGamesResult.class);


    }

    public void joinGame(JoinGameRequest request)
            throws UnauthorizedException, DataAccessException {

        var httpRequest = buildRequest("PUT", "/game", request, authToken);
        var response = sendRequest(httpRequest);
        handleResponse(response, null);


    }

    public void logout()
            throws UnauthorizedException, DataAccessException {

        var httpRequest = buildRequest("DELETE", "/session", null, authToken);
        var response = sendRequest(httpRequest);
        handleResponse(response, null);
    }


    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("auth", authToken);
        }
        return request.build();
    }


    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws DataAccessException {
        // idk if data access exception is correct
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws DataAccessException {


        var status = response.statusCode();
        if (!isSuccessful(status)) {

            throw new DataAccessException(response.body());

        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}







