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
            drawRowOfSquares(out, first, boardRow, isBlackView);
        }
        drawColHeaders(out, isBlackView);
    }

    private static void drawColHeaders(PrintStream out, boolean isBlack){
        setBlack(out);

        out.print("   ");
        String[] topHeaders = {" A ", " B ", " C ", " D ", " E ", " F ", " G ", " H "};
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
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }


    private static void drawTopHeaders(PrintStream out, String topHeader){
        setBlack(out);
        out.print("" + topHeader + "");
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    public enum pieceFirst {

        BLACK_FIRST,
        WHITE_FIRST
    }

    private static void drawRowOfSquares(PrintStream out, pieceFirst which_color_first, int boardRow, boolean isBlack) {
        int displayRow = !isBlack ? BOARD_SIZE_IN_SQUARES - boardRow : boardRow + 1;

        setBlack(out);
        out.print(" " + displayRow + " ");

        for (int colBoard = 0; colBoard < BOARD_SIZE_IN_SQUARES; colBoard++) {
            int actualRow = !isBlack ? 8 - boardRow : boardRow + 1;
            int actualCol = !isBlack ? colBoard + 1 : 8 - colBoard;

            if ((actualRow + actualCol) % 2 == 0) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
            } else {
                out.print(SET_BG_COLOR_BLUE);
            }

            if (actualRow == 1 || actualRow == 2) {
                out.print(SET_TEXT_COLOR_WHITE);
            } else {
                out.print(SET_TEXT_COLOR_BLACK);
            }

            out.print(" " + getPiece(actualRow, actualCol) + " ");
        }

        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.println();
    }

    private static String getPiece(int row, int col){
        if (row == 8 || row == 1) {
            if (col == 1 || col == 8) {
                return "R";
            }
            if (col == 2 || col == 7) {
                return "N";
            }
            if (col == 3 || col == 6) {
                return "B";
            }
            if (col == 4) {
                return "Q";
            }
            if (col == 5) {
                return "K";
            }
        }
        if (row == 7 || row == 2){
            return "P";
        }
        return " ";

    }

    private static void setBlack(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }


}
