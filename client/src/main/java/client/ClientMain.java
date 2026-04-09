package client;

import chess.*;
import server.ServerFacade;
import ui.ChessClient;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        int port = 8080;
        ServerFacade server = new ServerFacade(port);
        ChessClient client = new ChessClient(server, "http://localhost:" + port);

        client.runfirstScreen();

        //going well

    }
}
