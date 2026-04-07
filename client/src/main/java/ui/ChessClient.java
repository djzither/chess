package ui;


import chess.ChessGame;


import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.server.Response;
import requestobjects.*;
import server.ServerFacade;


import java.util.*;


public class ChessClient {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String userName;
    private String authToken;
    private ChessGame currentGame;
    private List<GameData> gamesListed = new ArrayList<>();
    private WSClient wsClient;
    private final String serverUrl;
    //I might want to use gamelist to join???



    public ChessClient(ServerFacade server, String serverUrl) {
        this.server = server;
        this.serverUrl = serverUrl;
    }

    public void runfirstScreen() {
        if (state == State.SIGNEDOUT){
            System.out.println("WELCOME TO MY CHESS SERVER! TYPE HELP TO GET STARTED!");

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
//

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
                    - quit - playing chess
                    - help
                    """;
        }

    }


    public void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_GREEN + ">>> " + EscapeSequences.RESET_TEXT_COLOR);
    }

    private enum State {
        SIGNEDOUT,
        SIGNEDIN
    }
    public String eval(String input){
        try{
            String[] tokens = input.split(" ");
            String cmd = tokens[0].toLowerCase();
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

        }catch (ResponseException ex){

            return ex.getMessage();
        }

    }

    private void gameCommands(ChessGame.TeamColor playerColor){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Joined game "+ "\n Type 'help' for commands.");
        boolean running = true;

        while (running){
            printPrompt();
            String input = scanner.nextLine().trim();
            try {
                String[] tokens = input.split(" ");
                String cmd = tokens[0];
                String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

                switch (cmd) {
                    case "move":
                        wsClient.sendMove(params[0], params[1], params[2]);
                        break;
                    case "resign":

                        wsClient.resign();
                        running = false;
                        break;

                    case "leave":
                        wsClient.leavegame();
                        running = false;
                        break;
                    case "redraw":
                        MakeBoard board = new MakeBoard(currentGame);
                        board.makeBoard(playerColor, null);
                        break;
                    case "legalmoves":
                        break;
                    default:
                        gameHelp();


                }
            }catch(Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }



        }

    }

    public String gameHelp() throws ResponseException{
        return """
                    - help -> displays this menu
                    - redraw -> redraws the chessboard
                    - leave -> removes usr form game
                    - move <E5> <E4> -> make a move
                    - legalmoves -> gives legal moves
                    - resign -> you resign and loose
                    """;

    }



    public String signIn(String...params) throws ResponseException {
        if (params.length != 2) {
            return("Expected 2 strings, got different number");
        }
        //i'm not super sure but I need to handle bad input somewhere, maybe here???


        LoginRequest loginRequest = new LoginRequest(params[0], params[1]);


        RegisterLoginResult registerLoginResult = server.login(loginRequest);

        userName = registerLoginResult.username();
        state = State.SIGNEDIN;
        return String.format("you signed in as %s.", userName);

    }


    public String register(String...params) throws ResponseException{
        if (params.length != 3){
            throw new ResponseException(ResponseException.Code.ClientError, "Expected 3 strings, got different num");
        }
        RegisterRequest registerRequest = new RegisterRequest(params[0], params[1], params[2]);
        RegisterLoginResult registerLoginResult = server.register(registerRequest);

        userName = registerLoginResult.username();

        state = State.SIGNEDIN;
        return String.format("you registered as %s.", userName);
    }

    public String createGame(String...params) throws ResponseException {
        assertSignedIn();
        if (params.length != 1) {
            throw new ResponseException(ResponseException.Code.ClientError, "params len must be 1");
        }

        String gameName = params[0];
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName, null);

        server.createGame(createGameRequest);

        //gonna need weird stuff with game id i think based on insturctions
        return String.format("created '%s' as a game", gameName);
    }

    private String listGames() throws ResponseException{
        assertSignedIn();
        ListGamesResult games = server.listGames();

        //i think this should work to make a list
        gamesListed = new ArrayList<>();
        for (GameParts gameParts : games.games()){
            gamesListed.add(new GameData(
                    gameParts.gameID(),
                    gameParts.whiteUsername(),
                    gameParts.blackUsername(),
                    gameParts.gameName(),
                    gameParts.game()
            ));
        }
        if (gamesListed.isEmpty()){
            return "there are no games created";
        }

        var result = new StringBuilder();

        for (int i = 0; i < gamesListed.size(); i++){
            GameData game = gamesListed.get(i);
            result.append(i + 1).append(" ").append(game.gameName()).append("\n").append(" WHITE USERNAME ").
                    append(game.whiteUsername()).append(" BLACK USERNAME ").append(game.blackUsername()).append("\n");
        }
        return result.toString();


    }
    private String joinGame(String...params) throws ResponseException{
        assertSignedIn();

        if (params.length != 2) {
            throw new ResponseException(ResponseException.Code.ClientError,"needs to have <gameId> [WHITE|BLACK");
        }
        int idx;
        try {
            idx = Integer.parseInt(params[0])-1;
        } catch (NumberFormatException e) {
            throw new ResponseException(ResponseException.Code.ClientError,"game num must be number");
        }
        if (gamesListed.isEmpty()){
            throw new ResponseException(ResponseException.Code.ClientError,"must list games first");
        }
        if (idx < 0 || idx >= gamesListed.size()){
            throw new ResponseException(ResponseException.Code.ClientError,"bad game num");
        }
        //i declare here bc thats how i use it later i guess? not inside try

        GameData game = gamesListed.get(idx);
        String color = params[1];



        JoinGameRequest joinGameRequest = new JoinGameRequest(color, game.gameId());
        server.joinGame(joinGameRequest);


        ChessGame.TeamColor playersColor;
        if (color.equals("WHITE")) {
            playersColor = ChessGame.TeamColor.WHITE;
        }
        else{
            playersColor = ChessGame.TeamColor.BLACK;
        }

        //chess game

        currentGame = gamesListed.get(idx).game();
        if (game.game() != null) {
            currentGame = game.game();
        } else {
            currentGame = new ChessGame();
        }


        wsClient = new WSClient(serverUrl, game.gameId(), currentGame, authToken);
        wsClient.connect(playersColor);

        gameCommands(playersColor);
        return "joined game";
    }

    private String observe(String...params) throws ResponseException {
        assertSignedIn();
        if (params.length != 1){
            throw new ResponseException(ResponseException.Code.ClientError,"needs to have <gameID>");
        }
        int idx;
        try {
            idx = Integer.parseInt(params[0])-1;
        } catch (NumberFormatException e) {
            throw new ResponseException(ResponseException.Code.ClientError,"game num must be number");
        }

        if (idx < 0 || idx >= gamesListed.size()){
            throw new ResponseException(ResponseException.Code.ClientError,"bad game num");
        }
        //i declare here bc thats how i use it later i guess? not inside try
        GameData game = gamesListed.get(idx);


        if (game.game() != null) {
            currentGame = game.game();
        } else {
            currentGame = new ChessGame();
        }



        wsClient = new WSClient(serverUrl, game.gameId(),currentGame, authToken);
        wsClient.connect(null);

        gameCommands(null);

        return String.format("observing game '%s' between %s white and %s black.",
                game.gameName(),
                game.whiteUsername(),
                game.blackUsername());


    }
    private String logout(String...params) throws ResponseException {
        assertSignedIn();
        state = State.SIGNEDOUT;
        server.logout();
        return "You are logged out";
    }



    private void assertSignedIn() throws ResponseException{
        if (state == State.SIGNEDOUT){
            throw new ResponseException(ResponseException.Code.ClientError,"");
        }

    }


}