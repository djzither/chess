package client;


import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import model.GameData;
import server.ServerFacade;

import server.service.requestobjects.*;
import ui.EscapeSequences;
import ui.MakeBoard;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class ChessClient {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String userName;
    private String authToken;
    private List<GameData> gamesListed;
    //I might want to use gamelist to join???



    public ChessClient(ServerFacade server, String userName) {
        this.server = server;
    }

    public void runfirstScreen() {
        if (state == State.SIGNEDOUT){
            System.out.println("WECOME TO MY CHESS SERVER! TYPE HELP TO GET STARTED!");

        }
        else{
            System.out.println("You are logged in as " + userName);
        }

        System.out.println(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")){
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);

            }catch(Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }


    private String help() {
        if (state == State.SIGNEDOUT){
            return """
                    - signIn <your usrname> <password>
                    - register <your usrname> <password> <email>
                    - quit
                    - help
                    """;
        } else{
            return """
                    - create <NAME> - a game
                    - list - games
                    - join <ID> [WHITE|BLACK] - a game
                    - observe <ID> - a game
                    - logout - when you are done
                    - quite - playing chess
                    - help
                    """;
        }

    }


    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_GREEN + ">>> " + EscapeSequences.RESET_TEXT_COLOR);
    }

    private enum State {
        SIGNEDOUT,
        SIGNEDIN
    }
    public String eval(String input){
        try{
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "signin" -> signIn(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);

                default -> help();

            };

        }catch (BadRequestException | UnauthorizedException | DataAccessException | AlreadyTakenException ex){

            return ex.getMessage();
        }

    }



    public String signIn(String...params) throws BadRequestException, UnauthorizedException, DataAccessException{
        if (params.length != 2) {
            return("Expected 2 strings, got different number");
        }
        //i'm not super sure but I need to handle bad input somewhere, maybe here???


        LoginRequest loginRequest = new LoginRequest(params[0], params[1]);


        RegisterLoginResult registerLoginResult = server.login(loginRequest);
        authToken = registerLoginResult.authToken();
        userName = registerLoginResult.username();
        state = State.SIGNEDIN;
        return String.format("you signed in as %s.", userName);

    }


    public String register(String...params) throws BadRequestException, DataAccessException{
        if (params.length != 3){
            throw new BadRequestException("Expected 3 strings, got different num");
        }
        RegisterRequest registerRequest = new RegisterRequest(params[0], params[1], params[2]);
        RegisterLoginResult registerLoginResult = server.register(registerRequest);
        authToken = registerLoginResult.authToken();
        userName = registerLoginResult.username();

        state = State.SIGNEDIN;
        return String.format("you registered as %s.", userName);
    }

    public String createGame(String...params) throws BadRequestException, UnauthorizedException, DataAccessException{
        assertSignedIn();
        if (params.length != 1) {
            throw new BadRequestException();
        }

        String gameName = params[0];
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName, authToken);

        CreateGameResult createGameResult = server.createGame(createGameRequest);
        //gonna need weird stuff with game id i think based on insturctions
        return String.format("created '%s' as a game", gameName);


    }

    private String listGames() throws UnauthorizedException, DataAccessException{
        assertSignedIn();
        String games = server.listGames();
        gamesListed = List.of(new Gson().fromJson(games, GameData[].class));

        if (gamesListed.isEmpty()){
            return "there are no games created";
        }

        var result = new StringBuilder();

        for (int i = 0; i < gamesListed.size(); i++){
            GameData game = gamesListed.get(i);
            result.append(i + 1).append(" ").append(game.gameName()).append("\n");
        }
        return result.toString();


    }
    private String joinGame(String...params) throws UnauthorizedException, DataAccessException, AlreadyTakenException, BadRequestException {
        assertSignedIn();
        if (params.length != 2) {
            throw new BadRequestException("needs to have <gameId> [WHITE|BLACK");
        }
        int idx;
        try {
            idx = Integer.parseInt(params[0])-1;
        } catch (NumberFormatException e) {
            throw new BadRequestException("game num must be number");
        }

        if (idx < 0 || idx >= gamesListed.size()){
            throw new BadRequestException("bad game num");
        }
        //i declare here bc thats how i use it later i guess? not inside try

        GameData game = gamesListed.get(idx);
        String color = params[1];



        JoinGameRequest joinGameRequest = new JoinGameRequest(color, game.gameId());
        server.joinGame(joinGameRequest);


        MakeBoard board;
        if (Objects.equals(color, "WHITE")) {
            board = new MakeBoard(game, ChessGame.TeamColor.WHITE);
        }
        else{
            board = new MakeBoard(game, ChessGame.TeamColor.BLACK);
        }

        return board.makeBoard(game.game(), "play");
    }

    private String observe(String...params) throws UnauthorizedException, DataAccessException, AlreadyTakenException, BadRequestException {
        assertSignedIn();
        if (params.length != 1){
            throw new BadRequestException("needs to have <gameID>");
        }
        int idx;
        try {
            idx = Integer.parseInt(params[0])-1;
        } catch (NumberFormatException e) {
            throw new BadRequestException("game num must be number");
        }

        if (idx < 0 || idx >= gamesListed.size()){
            throw new BadRequestException("bad game num");
        }
        //i declare here bc thats how i use it later i guess? not inside try

        GameData game = gamesListed.get(idx);

        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", game.gameId());
        server.joinGame(joinGameRequest);


        //also make board? i think this is the make board class

        MakeBoard board = new MakeBoard(game, ChessGame.TeamColor.WHITE);

        return board.makeBoard(game.game(), "observe");

    }
    private String logout(String...params) throws UnauthorizedException, DataAccessException{
        assertSignedIn();
        state = State.SIGNEDOUT;
        server.logout();
        return "You are logged out";
    }



    private void assertSignedIn() throws UnauthorizedException{
        if (state == State.SIGNEDOUT){
            throw new UnauthorizedException();
        }

    }


}