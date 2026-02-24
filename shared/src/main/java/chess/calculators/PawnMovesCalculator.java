package chess.calculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PawnMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece.PieceType pieceType;

    public PawnMovesCalculator(ChessPosition position, ChessBoard board, ChessPiece.PieceType pieceType) {
        this.board = board;
        this.position = position;
        this.pieceType = pieceType;
    }

    public List<ChessMove> getPathValid() {
        int row = position.getRow();
        int col = position.getColumn();

        ChessPiece piece = board.getPiece(position);

        List<int[]> possiblePawnMoves = new ArrayList<>();

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            //white
            List<int[]> possiblePawnDiagonals = new ArrayList<>(List.of((new int[]{1, -1}), new int[]{1, 1}));
            List<int[]> possiblePawnForwards = new ArrayList<>(List.of((new int[]{1, 0})));

            //diag in bound
            for (int[] diag : possiblePawnDiagonals) {
                int newRow = row + diag[0];
                int newCol = col + diag[1];

                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                    continue;
                }
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece newPiece = board.getPiece(newPos);
                if (newPiece != null && newPiece.getTeamColor() != piece.getTeamColor()) {
                    possiblePawnMoves.add(diag);
                }
            }

            //forwards
            for (int[] forwards : possiblePawnForwards) {
                int newRow = row + forwards[0];
                int newCol = col + forwards[1];

                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                    continue;
                }
                ChessPosition newPos = new ChessPosition(newRow, newCol);
                ChessPiece newPiece = board.getPiece(newPos);
                if (newPiece == null) {
                    possiblePawnMoves.add(forwards);
                }
                //would do an if statment for promotion rn but idk if we need to
            }
        }
        else{
        //black
        List<int[]> possiblePawnDiagonalsBlack = new ArrayList<>(List.of((new int[]{-1, -1}), new int[]{-1, 1}));
        List<int[]> possiblePawnForwardsBlack = new ArrayList<>(List.of((new int[]{-1, 0})));

        //diag in bound
        for (int[] diag : possiblePawnDiagonalsBlack) {
            int newRow = row + diag[0];
            int newCol = col + diag[1];

            if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                continue;
            }
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            ChessPiece newPiece = board.getPiece(newPos);
            if (newPiece != null && newPiece.getTeamColor() != piece.getTeamColor()) {
                possiblePawnMoves.add(diag);
            }
        }

        //forwards
        for (int[] forwards : possiblePawnForwardsBlack) {
            int newRow = row + forwards[0];
            int newCol = col + forwards[1];

            if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                continue;
            }
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            ChessPiece newPiece = board.getPiece(newPos);
            if (newPiece == null) {
                possiblePawnMoves.add(forwards);
            }
            //would do an if statment for promotion rn but idk if we need to

        }}

        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK && row == 7) {
            ChessPosition one = new ChessPosition(row -1, col);
            ChessPosition two = new ChessPosition(row -2, col);
            if (board.getPiece(one) == null && board.getPiece(two) == null){
                possiblePawnMoves.add(new int[]{-2,0});
            }
        }

        //double
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && row == 2) {
            ChessPosition one = new ChessPosition(row+1, col);
            ChessPosition two = new ChessPosition(row+2, col);
            if (board.getPiece(one) == null && board.getPiece(two) == null){
                possiblePawnMoves.add(new int[]{2,0});
            }
        }

        List<ChessMove> validMoves = new ArrayList<>();
        for (int[] move : possiblePawnMoves){
            int newRow = row + move[0];
            int newCol = col + move[1];
            //rest of promotion
            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition end = new ChessPosition(newRow, newCol);


                if ((piece.getTeamColor() == ChessGame.TeamColor.WHITE && newRow == 8) ||
                        (piece.getTeamColor() == ChessGame.TeamColor.BLACK && newRow == 1)) {

                    validMoves.add(new ChessMove(position, end, ChessPiece.PieceType.QUEEN));
                    validMoves.add(new ChessMove(position, end, ChessPiece.PieceType.ROOK));
                    validMoves.add(new ChessMove(position, end, ChessPiece.PieceType.BISHOP));
                    validMoves.add(new ChessMove(position, end, ChessPiece.PieceType.KNIGHT));
                } else {
                    validMoves.add(new ChessMove(position, end, null));
                }

            }

        }
        return validMoves;
    }
}


