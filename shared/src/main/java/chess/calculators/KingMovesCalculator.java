package chess.calculators;


import chess.*;

import java.util.ArrayList;
import java.util.List;

public class KingMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece.PieceType pieceType;


    public KingMovesCalculator(ChessPiece.PieceType pieceType, ChessPosition position, ChessBoard board) {
        this.board = board;
        this.position = position;
        this.pieceType = pieceType;


    }
//    public List<ChessMove> king_check() {
//        ChessPiece piece = board.getPiece(position);
//        ChessGame.TeamColor color_king = piece.getTeamColor();
//        List<ChessMove> king_valid = getPathValid();
//
//        for (int row = 1; row <= 8; row++) {
//            for (int col = 1; col <= 8; col++) {
//                ChessPosition pos = new ChessPosition(row, col);
//                ChessPiece other_piece = board.getPiece(pos);
//                // remember that .'s are enum and you call them with whatever type you have
//                //with the method that calls it in you class if it's in a different class
//
//
//                if (other_piece == null) {
//                    continue;
//                }
//
//                else {
//                    ChessGame.TeamColor other_color = other_piece.getTeamColor();
//
//                    if (other_color != color_king) {
//                        ChessPiece.PieceType other_piece_type = other_piece.getPieceType();
//                        PieceMovesCalculator instance = new PieceMovesCalculator(board, pos, other_piece_type, other_color);
//                        List<ChessMove> valid_moves = instance.findRightMove();
//
//                        for (ChessMove move : valid_moves) {
//                            king_valid.remove(move);
//
//                        }
//                    }
//                }
//            }
//        }
//        return king_valid;
//    }

    public List<ChessMove> getPathValid() {
        int row = position.getRow();
        int col = position.getColumn();

        ChessPiece piece = board.getPiece(position);
        // list class and then the specific type of list
        List<int[]> king_possible_moves = new ArrayList<>();

        king_possible_moves.add(new int[]{0, 1});
        king_possible_moves.add(new int[]{1, 1});
        king_possible_moves.add(new int[]{-1, 1});
        king_possible_moves.add(new int[]{-1, 0});
        king_possible_moves.add(new int[]{1, 0});
        king_possible_moves.add(new int[]{-1, -1});
        king_possible_moves.add(new int[]{0, -1});
        king_possible_moves.add(new int[]{1, -1});


        OnBoardAndCapture instance = new OnBoardAndCapture(board, position, piece.getTeamColor());

        List<ChessPosition> valid = instance.outsideAndPosition(king_possible_moves);
        List<ChessMove> king_valid = new ArrayList<>();
        for (ChessPosition pos : valid) {
            king_valid.add(new ChessMove(position, pos, null));
        }

        return king_valid;
    } 
}
