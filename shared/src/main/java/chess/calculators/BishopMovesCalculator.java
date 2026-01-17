package chess.calculators;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collections;
import java.util.List;

public class BishopMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece.PieceType pieceType;
    public BishopMovesCalculator(ChessPiece.PieceType piecetype, ChessPosition position, ChessBoard board, ChessBoard board1, ChessPosition position1, ChessPiece.PieceType pieceType) {
        this.board = board1;
        this.position = position1;
        this.pieceType = pieceType;
    }

    public List<ChessPosition> getPathValid() {
        int row = position.getRow();
        int 

        return Collections.emptyList();
    }
}
