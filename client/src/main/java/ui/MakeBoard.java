package ui;

import chess.ChessGame;
import chess.ChessPiece;
import model.GameData;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;


public class MakeBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;

    private static final String EMPTY = EscapeSequences.EMPTY;

    public MakeBoard(GameData game, ChessGame.TeamColor playerType) {
    }

    public void makeBoard(ChessGame game, ChessGame.TeamColor playerColor, String observe) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);


        boolean isBlackView = (playerColor == ChessGame.TeamColor.BLACK && observe == null);
        drawColHeaders(out, isBlackView);

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; boardRow++){
            pieceFirst first = (boardRow % 2 == 0) ? pieceFirst.BLACK_FIRST : pieceFirst.WHITE_FIRST;
            drawRowOfSquares(out, game, first, isBlackView);
        }
        drawColHeaders(out, isBlackView);
    }

    private static void drawColHeaders(PrintStream out, boolean isBlack){
        setBlack(out);

        out.print(EMPTY);
        String[] topHeaders = {"A","B","C", "D","E", "F", "G", "H"};

        if (!isBlack) {
            for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
                drawTopHeaders(out, topHeaders[i]);
            }
        }
        else{
            for (int i = BOARD_SIZE_IN_SQUARES - 1; i >= 0; i--){
                drawTopHeaders(out, topHeaders[i]);
            }
        }
        out.println();
    }


    private static void drawTopHeaders(PrintStream out, String topHeader){
        setBlack(out);
        out.print(" " + topHeader + " ");
    }

    public enum pieceFirst {
        /**
         *
         */
        BLACK_FIRST,
        WHITE_FIRST
    }


    private static void drawRowOfSquares(PrintStream out, ChessGame game, pieceFirst which_color_first, boolean boardRow) {
        for (int row = 0; row < SQUARE_SIZE_IN_PADDED_CHARS; row++){


            if (row == SQUARE_SIZE_IN_PADDED_CHARS / 2){
                out.print((BOARD_SIZE_IN_SQUARES - boardRow) + " ");

            }

            else{
                out.print("   ");
            }
            boolean currentSquareWhite = (which_color_first == pieceFirst.WHITE_FIRST);

            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++){
                if (currentSquareWhite){
                    out.print(SET_BG_COLOR_WHITE);
                }
                else {
                    out.print(SET_BG_COLOR_BLACK);
                }
                //we are getting the right base
                int actualRow = BOARD_SIZE_IN_SQUARES - boardRow;
                int actualCol = col + 1;
                if (row == SQUARE_SIZE_IN_PADDED_CHARS / 2){
                    ChessPiece piece = game.getBoard().getPiece(actualRow, actualCol);
                }



                out.print(EMPTY);
                out.print(RESET_BG_COLOR);

                if (currentSquareWhite){
                    currentSquareWhite = false;
                }
                else{
                    currentSquareWhite = true;
                }

            }
            if (row == SQUARE_SIZE_IN_PADDED_CHARS /2){
                out.print(" " + (BOARD_SIZE_IN_SQUARES - boardRow) + " ");
            }

        }
        out.println();
    }

    private static String getPiece(int row, int col){
        if (row == 8) {
            if (col == 1 || col == 8) {
                return BLACK_ROOK;
            }
            if (col == 2 || col == 8) {
                return BLACK_KNIGHT;
            }
            if (col == 3 || col == 6) {
                return BLACK_BISHOP;
            }
            if (col == 4) {
                return BLACK_QUEEN;
            }
            if (col == 5) {
                return BLACK_KING;
            }
        }
        if (row == 7){
            return BLACK_PAWN;
        }

        if (row == 1) {
            if (col == 1 || col == 8) {
                return WHITE_ROOK;
            }
            if (col == 2 || col == 8) {
                return WHITE_KNIGHT;
            }
            if (col == 3 || col == 6) {
                return WHITE_BISHOP;
            }
            if (col == 4) {
                return WHITE_QUEEN;
            }
            if (col == 5) {
                return WHITE_KING;
            }
        }
        if (row == 2){
            return WHITE_PAWN;
        }
        return EMPTY;
    }

    private static void setBlack(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }
}
