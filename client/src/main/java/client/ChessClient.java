package client;


import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;

import java.util.Arrays;
import java.util.Scanner;


public class ChessClient {
    private final ServerFacad server;
    private State state = State.SIGNEDOUT;



    public ChessClient(ServerFacad server) {
        this.server = server;
    }

    public void runfirstScreen() {
        System.out.println("WECOME TO MY CHESS SERVER! TYPE HELP TO GET STARTED!");
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
                case "quit" -> quit(params);
                default -> help();

            };

        }catch (BadRequestException | UnauthorizedException | DataAccessException ex){

            return ex.getMessage();
        }
    }


}