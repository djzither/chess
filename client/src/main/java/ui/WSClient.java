package ui;

import chess.ChessGame;
import exception.ResponseException;

import java.util.Arrays;
import java.util.Scanner;

import static com.sun.org.apache.xpath.internal.XPathAPI.eval;


public class WSClient {
    private final Integer gameId;
    private final ChessGame game;
    private boolean running = false;
    private ChessGame.TeamColor playerColor;

    public WSClient(Integer gameId, ChessGame currentGame) {
        this.gameId = gameId;
        this.game = currentGame;

    }

    public void connect(ChessGame.TeamColor color) {
        this.playerColor = color;
        this.running = true;
        runGameLoop();
    }

    private void runGameLoop(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Joined game" + gameId + "\n Type 'help' for commands.");
        var result = "";
        while (running){
            printPrompt();
            String input = scanner.nextLine().trim();
            try {
                result = eval(input);
                System.out.print(result);
            }catch(Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }



        }
    }
    public String eval(String input){
        try{
            String[] tokens = input.split(" ");
            String cmd = tokens[0].toLowerCase();
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

            return switch (cmd) {
                case "redraw" -> redraw(params);
                case "leave" -> leave(params);
                case "move" -> move(params);
                case "resign" -> resign(params);
                case "legalMoves" -> legalMoves(params);
                default -> help();

            };

        }catch (ResponseException ex){

            return ex.getMessage();
        }

    }
    private String help() {

    }

    private String legalMoves(String[] params) {
    }

    private String resign(String[] params) {
    }

    private String move(String[] params) {
    }

    private String leave(String[] params) {
    }



    private String redraw(String[] params) {
    }

}
