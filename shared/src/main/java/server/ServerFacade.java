package server;

import com.google.gson.Gson;


import exception.ResponseException;
import requestobjects.*;


import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
//
public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url, String authToken) {
        serverUrl = url;

        this.authToken = authToken;
    }


    public RegisterLoginResult register(RegisterRequest registerRequest)
            throws ResponseException {
        var httpRequest = buildRequest("POST", "/user", registerRequest, null);
        var response = sendRequest(httpRequest);
        var result = handleResponse(response, RegisterLoginResult.class);
        this.authToken = result.authToken();
        return result;
    }


    public CreateGameResult createGame(CreateGameRequest request)
            throws ResponseException {

        var httpRequest = buildRequest("POST", "/game", request, authToken);
        var response = sendRequest(httpRequest);
        return handleResponse(response, CreateGameResult.class);
    }


    public ListGamesResult listGames()
            throws ResponseException {

        var httpRequest = buildRequest("GET", "/game", null, authToken);
        var response = sendRequest(httpRequest);
        return handleResponse(response, ListGamesResult.class);


    }

    public void joinGame(JoinGameRequest request)
            throws ResponseException {

        var httpRequest = buildRequest("PUT", "/game", request, authToken);
        var response = sendRequest(httpRequest);
        handleResponse(response, null);


    }

    public RegisterLoginResult login(LoginRequest loginRequest) throws ResponseException{
        var httpRequest = buildRequest("POST", "/session", loginRequest, null);

        var response = sendRequest(httpRequest);
        var result = handleResponse(response, RegisterLoginResult.class);
        this.authToken = result.authToken();
        return result;
    }

    public void logout()
            throws ResponseException{
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
            request.setHeader("authorization", authToken);
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

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException{
        // idk if data access exception is correct
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {


        var status = response.statusCode();
        //i am going to parse first
        if (!isSuccessful(status)) {
            // Convert server error to ResponseException
            ErrorResponseResult errorResponseResult = new Gson().fromJson(response.body(), ErrorResponseResult.class);
            ResponseException.Code code = ResponseException.fromHttpStatusCode(status);
            throw new ResponseException(code, errorResponseResult.getMessage());
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







