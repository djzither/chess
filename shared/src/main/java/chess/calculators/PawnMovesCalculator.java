package chess.calculators;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

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

    public List<ChessPosition> getPathValid() {

        return Collections.emptyList();
    }
}
