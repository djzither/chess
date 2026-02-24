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

    public List<ChessMove> getPathValid() {
        int row = position.getRow();
        int col = position.getColumn();

        ChessPiece piece = board.getPiece(position);
        // list class and then the specific type of list
        List<int[]> kingPossibleMoves = new ArrayList<>();

        kingPossibleMoves.add(new int[]{0, 1});
        kingPossibleMoves.add(new int[]{1, 1});
        kingPossibleMoves.add(new int[]{-1, 1});
        kingPossibleMoves.add(new int[]{-1, 0});
        kingPossibleMoves.add(new int[]{1, 0});
        kingPossibleMoves.add(new int[]{-1, -1});
        kingPossibleMoves.add(new int[]{0, -1});
        kingPossibleMoves.add(new int[]{1, -1});


        OnBoardAndCapture instance = new OnBoardAndCapture(board, position, piece.getTeamColor());

        List<ChessPosition> valid = instance.outsideAndPosition(kingPossibleMoves);
        List<ChessMove> kingValid = new ArrayList<>();
        for (ChessPosition pos : valid) {
            kingValid.add(new ChessMove(position, pos, null));
        }

        return kingValid;
    } 
}
