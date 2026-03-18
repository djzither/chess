package client;

import chess.*;
import server.ServerFacad;
import ui.ChessClient;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        String serverUrl = "http://localhost:8080";
        ServerFacad server = new ServerFacad(serverUrl, null);
        ChessClient client = new ChessClient(server, null);

        client.runfirstScreen();


    }
}
