package client;


import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import model.GameData;
import ui.MakeBoard;


import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class ChessClient {
    private final ServerFacad server;
    private State state = State.SIGNEDOUT;
    private String userName;
    //I might want to use gamelist to join???



    public ChessClient(ServerFacad server, String userName) {
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
    private void printPrompt(){
        System.out.print("\n" + RESET + ">>>>" + GREEN)
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
                case "list" -> listGames(params);
                case "join" -> joinGame(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);

                default -> help();

            };

        }catch (BadRequestException | UnauthorizedException | DataAccessException ex){

            return ex.getMessage();
        }

    }



    public String signIn(String...params) throws BadRequestException, UnauthorizedException, DataAccessException{
        if (params.length != 2) {
            return("Expected 2 strings, got different number");
        }
        //i'm not super sure but I need to handle bad input somewhere, maybe here???

        String password;
        userName = params[0];
        password = params[1];

        ServerFacad.login(userName, password);
        state = State.SIGNEDIN;
        return String.format("you signed in as %s.", userName);

    }


    public String register(String...params) throws BadRequestException, DataAccessException{
        if (params.length != 3){
            throw new BadRequestException("Expected 3 strings, got different num");
        }
        String userName;
        String password;
        String email;
        userName = params[0];
        password = params[1];
        email = params[2];

        ServerFacad.register(userName, password, email);
        state = State.SIGNEDIN;
        return String.format("you registered as %s.", userName);
    }

    public String createGame(String...params) throws BadRequestException, UnauthorizedException, DataAccessException{
        assertSignedIn();
        if (params.length != 1) {
            throw new BadRequestException();
        }
        String gameName = params[0];
        ServerFacad.createGame(gameName);

        return String.format("created '%s' as a game", gameName);


    }

    private String listGames(String...params) throws UnauthorizedException, DataAccessException{
        assertSignedIn();
        List<GameData> games = ServerFacad.listGames();
        if (games.isEmpty()){
            return "there are no games created";
        }
        var result = new StringBuilder();
        var gson = new Gson();
        for (GameData game : games){
            result.append(gson.toJson(game)).append("\n");
        }
        return result.toString();


    }
    private String joinGame(String...params) throws UnauthorizedException, DataAccessException, AlreadyTakenException, BadRequestException {
        assertSignedIn();
        if (params.length != 2) {
            throw new BadRequestException("needs to have <gameId> [WHITE|BLACK");
        }
        int gameId;
        try {
            gameId = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            throw new BadRequestException("id must be number");
        }
        //i declare here bc thats how i use it later i guess? not inside try
        ChessGame.TeamColor color;
        try {
            color = ChessGame.TeamColor.valueOf(params[1]);

        } catch (IllegalArgumentException e) {
            throw new BadRequestException("game color must be WHITE or BLACK");
        }
        ServerFacad.joinGame(params);
        return String.format("joined '%s' as team color %s", gameId, color);
    }

    private String observe(String...params) throws UnauthorizedException, DataAccessException, AlreadyTakenException, BadRequestException {
        assertSignedIn();
        if (params.length != 1){
            throw new BadRequestException("needs to have <gameID>")
        }
        int gameId;
        try {
            gameId = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            throw new BadRequestException("id must be number");
        }

        GameData game = ServerFacad.getGame(gameId);
        if (game == null){
            throw new BadRequestException("there isn't game associated with said id, sorry");
        }

        //also make board? i think this is the make board class

        MakeBoard board = new MakeBoard(game, "observer");

        return board.makeBoard(game.game(), "observe");

    }
    private String logout(String...params) throws UnauthorizedException, DataAccessException{
        assertSignedIn();
        state = State.SIGNEDOUT;
        ServerFacad.logout();
        return "You are logged out";
    }



    private void assertSignedIn() throws UnauthorizedException{
        if (state == State.SIGNEDOUT){
            throw new UnauthorizedException();
        }

    }


}