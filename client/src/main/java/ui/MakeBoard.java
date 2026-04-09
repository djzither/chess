package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;


public class MakeBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;

    private final ChessGame game;

    public MakeBoard(ChessGame game) {
        this.game = game;
    }

    public void makeBoard(ChessGame.TeamColor playerColor, String observe, Collection<ChessPosition> highlights) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);


        boolean isBlackView = (playerColor == ChessGame.TeamColor.BLACK && observe == null);
        out.println();
        drawColHeaders(out, isBlackView);

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; boardRow++){
            drawRowOfSquares(out, boardRow, isBlackView, highlights);
        }
        drawColHeaders(out, isBlackView);
    }

    private static void drawColHeaders(PrintStream out, boolean isBlack){

        setBlack(out);


        out.print(" ");
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

    private void drawRowOfSquares(PrintStream out, int boardRow, boolean isBlack, Collection<ChessPosition> highlights) {
        int displayRow;
        if (!isBlack) {
            displayRow = BOARD_SIZE_IN_SQUARES - boardRow;
        } else {
            displayRow = boardRow + 1;
        }
        setBlack(out);
        out.print(" " + displayRow + " ");

        for (int colBoard = 0; colBoard < BOARD_SIZE_IN_SQUARES; colBoard++) {
            int actualRow;
            int actualCol;
            //handles my board inverse logic
            if (!isBlack) {
                actualRow = 8 - boardRow;
                actualCol = colBoard + 1;
            } else {
                actualRow = boardRow + 1;
                actualCol = 8 - colBoard;
            }
            ChessPosition pos = new ChessPosition(actualRow, actualCol);
            if (highlights != null && highlights.contains(pos)) {
                out.print(SET_BG_COLOR_YELLOW);
            }
            else if ((actualRow + actualCol) % 2 == 0) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
            } else {
                out.print(SET_BG_COLOR_BLUE);
            }

            ChessPiece piece = game.getBoard().getPiece(pos);
            if (piece != null) {
                if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(SET_TEXT_COLOR_WHITE);
                } else {
                    out.print(SET_TEXT_COLOR_BLACK);
                }
            }

            out.print(" " + getPiece(actualRow, actualCol) + " ");
        }
        setBlack(out);
        out.print(" " + displayRow + " ");

        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.println();
    }

    private String getPiece(int row, int col) {
        ChessPiece piece = game.getBoard().getPiece(new ChessPosition(row, col));
        if (piece == null) {
            return " ";
        }

        String pieceType = "";

        switch (piece.getPieceType()) {
            case KING:
                pieceType = "k";
                break;
            case QUEEN:
                pieceType = "q";
                break;
            case ROOK:
                pieceType = "r";
                break;
            case BISHOP:
                pieceType = "b";
                break;
            case KNIGHT:
                pieceType = "n";
                break;
            case PAWN:
                pieceType = "p";
                break;
        }

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            pieceType = pieceType.toUpperCase();
        }

        return pieceType;
    }

    private static void setBlack(PrintStream out){
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }


}
