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
        int direction = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;

        ChessPosition oneStep = new ChessPosition(row + direction, col);

        if (board.getPiece(oneStep) == null) {
            possiblePawnMoves.add(new int[]{direction, 0});
            //double
            int startRow = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? 2 : 7;
            ChessPosition twoStep = new ChessPosition(row + 2 * direction, col);
            if (row == startRow && board.getPiece(twoStep) == null) {
                possiblePawnMoves.add(new int[]{2 * direction, 0});
            }
        }


            //diagonal
        for (int diag : new int[]{-1, 1}){
            ChessPosition diagonal = new ChessPosition(row + direction, col + diag);
            if (diagonal.getRow() >= 1 && diagonal.getRow() <= 8 && diagonal.getColumn() >=1 && diagonal.getColumn() <= 8){
                ChessPiece target = board.getPiece(diagonal);
                if (target != null && target.getTeamColor() != piece.getTeamColor()){
                    possiblePawnMoves.add(new int[]{direction, diag});
                }
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


