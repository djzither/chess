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

        out.print(EMPTY);
        String[] topHeaders = {"A","B","C", "D","E", "F", "G", "H"};

        if (!isBlack) {
            for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
                drawTopHeaders(out, topHeaders[i] + " ");
            }
        }
        else{
            for (int i = BOARD_SIZE_IN_SQUARES - 1; i >= 0; i--){
                drawTopHeaders(out, topHeaders[i] + " ");
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


    private static void drawRowOfSquares(PrintStream out, pieceFirst which_color_first, int boardRow, boolean isBlack) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; squareRow++) {

            int displayRow;

            if (!isBlack) {
                displayRow = BOARD_SIZE_IN_SQUARES - boardRow;
            } else {
                displayRow = boardRow + 1;
            }


            if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                out.print(displayRow + " ");

            } else {
                out.print(EMPTY);
            }

            for (int colBoard = 0; colBoard < BOARD_SIZE_IN_SQUARES; colBoard++) {
                int actualRow;
                int actualCol;


                if (!isBlack) {
                    actualRow = 8 - boardRow;
                    actualCol = colBoard + 1;
                } else {
                    actualRow = boardRow + 1;
                    actualCol = 8 - colBoard;
                }

                if ((actualRow + actualCol) % 2 == 0) {
                    out.print(SET_BG_COLOR_WHITE);
                } else {
                    setBlack(out);
                }

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int beg = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int end = SQUARE_SIZE_IN_PADDED_CHARS - beg - 1;

                    out.print(" ".repeat(beg));
                    String piece = getPiece(actualRow, actualCol);
                    out.print(piece);
                    out.print(" ".repeat(end));
                } else {
                    out.print(EMPTY);
                }

            }


        }


    }

    private static String getPiece(int row, int col){
        if (row == 8) {
            if (col == 1 || col == 8) {
                return BLACK_ROOK;
            }
            if (col == 2 || col == 7) {
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
            if (col == 2 || col == 7) {
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
