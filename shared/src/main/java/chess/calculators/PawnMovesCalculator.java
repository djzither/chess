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

        List<int[]> possible_pawn_moves = new ArrayList<>();

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            //white
            List<int[]> possible_pawn_diagonals = new ArrayList<>(List.of((new int[]{1, -1}), new int[]{1, 1}));
            List<int[]> possible_pawn_forwards = new ArrayList<>(List.of((new int[]{1, 0})));

            //diag in bound
            for (int[] diag : possible_pawn_diagonals) {
                int new_row = row + diag[0];
                int new_col = col + diag[1];

                if (new_row < 1 || new_row > 8 || new_col < 1 || new_col > 8) {
                    continue;
                }
                ChessPosition new_pos = new ChessPosition(new_row, new_col);
                ChessPiece new_piece = board.getPiece(new_pos);
                if (new_piece != null && new_piece.getTeamColor() != piece.getTeamColor()) {
                    possible_pawn_moves.add(diag);
                }
            }

            //forwards
            for (int[] forwards : possible_pawn_forwards) {
                int new_row = row + forwards[0];
                int new_col = col + forwards[1];

                if (new_row < 1 || new_row > 8 || new_col < 1 || new_col > 8) {
                    continue;
                }
                ChessPosition new_pos = new ChessPosition(new_row, new_col);
                ChessPiece new_piece = board.getPiece(new_pos);
                if (new_piece == null) {
                    possible_pawn_moves.add(forwards);
                }
                //would do an if statment for promotion rn but idk if we need to
            }
        }
        else{
        //black
        List<int[]> possible_pawn_diagonals_black = new ArrayList<>(List.of((new int[]{-1, -1}), new int[]{-1, 1}));
        List<int[]> possible_pawn_forwards_black = new ArrayList<>(List.of((new int[]{-1, 0})));

        //diag in bound
        for (int[] diag : possible_pawn_diagonals_black) {
            int new_row = row + diag[0];
            int new_col = col + diag[1];

            if (new_row < 1 || new_row > 8 || new_col < 1 || new_col > 8) {
                continue;
            }
            ChessPosition new_pos = new ChessPosition(new_row, new_col);
            ChessPiece new_piece = board.getPiece(new_pos);
            if (new_piece != null && new_piece.getTeamColor() != piece.getTeamColor()) {
                possible_pawn_moves.add(diag);
            }
        }

        //forwards
        for (int[] forwards : possible_pawn_forwards_black) {
            int new_row = row + forwards[0];
            int new_col = col + forwards[1];

            if (new_row < 1 || new_row > 8 || new_col < 1 || new_col > 8) {
                continue;
            }
            ChessPosition new_pos = new ChessPosition(new_row, new_col);
            ChessPiece new_piece = board.getPiece(new_pos);
            if (new_piece == null) {
                possible_pawn_moves.add(forwards);
            }
            //would do an if statment for promotion rn but idk if we need to

        }}

        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK && row == 7) {
            ChessPosition one = new ChessPosition(row -1, col);
            ChessPosition two = new ChessPosition(row -2, col);
            if (board.getPiece(one) == null && board.getPiece(two) == null){
                possible_pawn_moves.add(new int[]{-2,0});
            }
        }

        //double
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE && row == 2) {
            ChessPosition one = new ChessPosition(row+1, col);
            ChessPosition two = new ChessPosition(row+2, col);
            if (board.getPiece(one) == null && board.getPiece(two) == null){
                possible_pawn_moves.add(new int[]{2,0});
            }
        }

        List<ChessMove> valid_moves = new ArrayList<>();
        for (int[] move : possible_pawn_moves){
            int new_row = row + move[0];
            int new_col = col + move[1];
            //rest of promotion
            if (new_row >= 1 && new_row <= 8 && new_col >= 1 && new_col <= 8) {
                ChessPosition end = new ChessPosition(new_row, new_col);

// promotion
                if ((piece.getTeamColor() == ChessGame.TeamColor.WHITE && new_row == 8) ||
                        (piece.getTeamColor() == ChessGame.TeamColor.BLACK && new_row == 1)) {

                    valid_moves.add(new ChessMove(position, end, ChessPiece.PieceType.QUEEN));
                    valid_moves.add(new ChessMove(position, end, ChessPiece.PieceType.ROOK));
                    valid_moves.add(new ChessMove(position, end, ChessPiece.PieceType.BISHOP));
                    valid_moves.add(new ChessMove(position, end, ChessPiece.PieceType.KNIGHT));
                } else {
                    valid_moves.add(new ChessMove(position, end, null));
                }

            }

        }
        return valid_moves;
    }
}


