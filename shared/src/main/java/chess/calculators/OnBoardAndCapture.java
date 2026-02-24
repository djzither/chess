package chess.calculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.List;

public class OnBoardAndCapture {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessGame.TeamColor color;

    public OnBoardAndCapture(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        this.board = board;
        this.position = position;
        this.color = color;
    }

    public List<ChessPosition> outsideAndPosition(List<int[]> possibleMoves) {
        List<ChessPosition> validMoves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece piece = board.getPiece(position);

        //check if on board, then get the piece and if returns none then good
        //color/capture, on board
        for (int[] i : possibleMoves) {
            int newRow = i[0] + row;
            int newCol = i[1] + col;
            //this should make sure we are inside
            if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                continue;

            }
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            ChessPiece newPiece = board.getPiece(newPos);
            if (newPiece == null || newPiece.getTeamColor() != piece.getTeamColor()) {
                validMoves.add(newPos);
            }

        }
        return validMoves;
    }
}
