package client;


import dataaccess.exceptions.AlreadyTakenException;
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

    public void runSecondScreen() {
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
                case "quit" -> "quit";
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
        String userName;
        String password;
        userName = params[0];
        password = params[1];

        ServerFacad.login(userName, password);
        state = State.SIGNEDIN;
        return String.format("you signed in as %s.", userName);

    }


    public String register(String...params) throws BadRequestException, DataAccessException{
        if (params.length != 3){
            return("Expected 3 strings, got different num");
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
}