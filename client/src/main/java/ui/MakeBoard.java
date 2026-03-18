package ui;

import chess.ChessGame;
import model.GameData;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;


public class MakeBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;

    private static final String EMPTY = "   ";

    public MakeBoard(GameData game, ChessGame.TeamColor playerType) {
    }

    public void makeBoard(ChessGame game, String observe) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);

        drawColHeaders(out);

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; boardRow++){
            pieceFirst first = (boardRow % 2 == 0) ? pieceFirst.BLACK_FIRST : pieceFirst.WHITE_FIRST;
            drawRowOfSquares(out, first, boardRow);
        }
        drawColHeaders(out);



    }

    private static void drawColHeaders(PrintStream out){
        setBlack(out);

        out.print(SQUARE_SIZE_IN_PADDED_CHARS);
        String[] topHeaders = {"A","B","C", "D","E", "F", "G", "H"};
         for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol){

            drawTopHeaders(out, topHeaders[boardCol]);


        }
        out.println();
    }


    private static void drawTopHeaders(PrintStream out, String topHeader){
        setBlack(out);
        out.print(" " + topHeader + " ");
    }

    private static void PrintHeadertText(PrintStream out, String player){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(player);
        setBlack(out);


    }
    public enum pieceFirst {
        /**
         *
         */
        BLACK_FIRST,
        WHITE_FIRST
    }


    private static void drawRowOfSquares(PrintStream out, pieceFirst which_color_first, int boardRow) {
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

    private static void setBlack(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }
}
