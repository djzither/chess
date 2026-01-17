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

    public List<ChessPosition> outsideAndPosition(List<int[]> possible_moves) {
        List<ChessPosition> valid_moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece piece = board.getPiece(position);

        //check if on board, then get the piece and if returns none then good
        //color/capture, on board
        for (int[] i : possible_moves) {
            int new_row = i[0] + row;
            int new_col = i[1] + col;
            //this should make sure we are inside
            if (new_row < 1 || new_row > 8 || new_col < 1 || new_col > 8) {
                continue;

            }
            ChessPosition new_pos = new ChessPosition(new_row, new_col);
            ChessPiece new_piece = board.getPiece(new_pos);
            if (new_piece == null || new_piece.getTeamColor() != piece.getTeamColor()) {
                valid_moves.add(new_pos);
            }

        }
        return valid_moves;
    }
}
