package chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KnightMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;


    public KnightMovesCalculator(ChessPiece.PieceType piecetype, ChessPosition position, ChessBoard board) {
        this.board = board;
        this.position = position;
    }

    public List<ChessPosition> getPathValid() {
        int row = position.getRow();
        int col = position.getColumn();


        ChessPiece piece = board.getPiece(position);

        List<int[]> knight_possible_moves = new ArrayList<>();
        knight_possible_moves.add(new int[]{2, 1});
        knight_possible_moves.add(new int[]{2, -1});
        knight_possible_moves.add(new int[]{1, 2});
        knight_possible_moves.add(new int[]{1, -2});

        knight_possible_moves.add(new int[]{-2, 1});
        knight_possible_moves.add(new int[]{-2, -1});
        knight_possible_moves.add(new int[]{-1, 2});
        knight_possible_moves.add(new int[]{-1, -2});

        List<ChessPosition> knight_valid = new ArrayList<>();

        for (int[] i : knight_possible_moves){
            int new_row = i[0] + row;
            int new_col = i[1] + col;
            //this should make sure we are inside
            if (new_row < 1 || new_row > 8 || new_col < 1 || new_col > 8){
                continue;

            }
            ChessPosition new_pos = new ChessPosition(new_row, new_col);
            ChessPiece new_piece = board.getPiece(new_pos);
            if (new_piece == null || new_piece.getTeamColor() != piece.getTeamColor()){
                knight_valid.add(new_pos);
            }

        }
        return knight_valid;
    }
}
