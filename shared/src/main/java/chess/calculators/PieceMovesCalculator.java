package chess.calculators;


import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.List;

public class PieceMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece.PieceType piecetype;
    // don't need piece color because it is enum

    // apparenly I have to specify in the constuctor of the class I call where I get these as well
    public PieceMovesCalculator(ChessBoard board, ChessPosition position, ChessPiece.PieceType piecetype, ChessGame.TeamColor color) {
        this.board = board;
        this.position = position;
        this.piecetype = piecetype;
    }

    public List<ChessPosition> findRightMove(){
        switch (piecetype){
            case PAWN -> {
                PawnMovesCalculator instance = new PawnMovesCalculator(piecetype, position, board);
                return instance.getPathValid();
            }
            case ROOK -> {
                RookMovesCalculator instance = new RookMovesCalculator(position, board, piecetype);
                return instance.getPathValid();
            }
            case KNIGHT -> {
                KnightMovesCalculator instance = new KnightMovesCalculator(piecetype, position, board);
                return instance.getPathValid();
            }
            case BISHOP -> {
                BishopMovesCalculator instance = new BishopMovesCalculator(position, board, piecetype);
                return instance.getPathValid();
            }
            case QUEEN -> {
                QueenMovesCalculator instance = new QueenMovesCalculator(position, board, piecetype);
                return instance.getPathValid();
            }
            case KING -> {
                KingMovesCalculator instance = new KingMovesCalculator(piecetype, position, board);
                return instance.getPathValid();
            }
            default -> throw new IllegalArgumentException("Invalid piece");
        }
    }
}
