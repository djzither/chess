package chess.calculators;


import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

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
    public List<ChessPosition> king_check() {
        ChessPiece piece = board.getPiece(position);
        ChessGame.TeamColor color_king = piece.getTeamColor();
        List<ChessPosition> king_valid = getPathValid();

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece other_piece = board.getPiece(pos);
                // remember that .'s are enum and you call them with whatever type you have
                //with the method that calls it in you class if it's in a different class


                if (other_piece == null) {
                    continue;
                }

                else {
                    ChessGame.TeamColor other_color = other_piece.getTeamColor();

                    if (other_color != color_king) {
                        ChessPiece.PieceType other_piece_type = other_piece.getPieceType();
                        PieceMovesCalculator instance = new PieceMovesCalculator(board, pos, other_piece_type, other_color);
                        List<ChessPosition> valid_moves = instance.findRightMove();

                        for (ChessPosition move : valid_moves) {
                            king_valid.remove(move);

                        }
                    }
                }
            }
        }
        return king_valid;
    }

    public List<ChessPosition> getPathValid(){
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
        return instance.outsideAndPosition(king_possible_moves);
    }


}
