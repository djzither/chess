package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.List;

public class KnightMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;


    public KnightMovesCalculator(ChessPiece.PieceType piecetype, ChessPosition position, ChessBoard board) {
        this.board = board;
        this.position = position;
    }

    public List<ChessMove> getPathValid() {
        int row = position.getRow();
        int col = position.getColumn();


        ChessPiece piece = board.getPiece(position);

        List<int[]> knightPossibleMoves = new ArrayList<>();
        knightPossibleMoves.add(new int[]{2, 1});
        knightPossibleMoves.add(new int[]{2, -1});
        knightPossibleMoves.add(new int[]{1, 2});
        knightPossibleMoves.add(new int[]{1, -2});

        knightPossibleMoves.add(new int[]{-2, 1});
        knightPossibleMoves.add(new int[]{-2, -1});
        knightPossibleMoves.add(new int[]{-1, 2});
        knightPossibleMoves.add(new int[]{-1, -2});

        OnBoardAndCapture instance = new OnBoardAndCapture(board, position, piece.getTeamColor());
        List<ChessPosition> validPositions = instance.outsideAndPosition(knightPossibleMoves);


        List<ChessMove> validMoves = new ArrayList<>();
        for (ChessPosition end : validPositions) {
            validMoves.add(new ChessMove(position, end, null)); // Knight never promotes
        }

        return validMoves;

    }
}
