package chess;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;


    public KingMovesCalculator(ChessPiece.PieceType pieceType, ChessPosition position, ChessBoard board) {
        this.board = board;
        this.position = position;

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
        king_possible_moves.add(new int[]{-1, -1});
        king_possible_moves.add(new int[]{0, -1});
        king_possible_moves.add(new int[]{1, -1});
        king_possible_moves.add(new int[]{-1, -1});

        List<ChessPosition> king_valid = new ArrayList<>();

        //check if on board, then get the piece and if returns none then good
        //color/capture, on board
        for (int[] i : king_possible_moves){
            int new_row = i[0] + row;
            int new_col = i[1] + col;
            //this should make sure we are inside
            if (new_row < 1 || new_row > 8){
                if (new_col < 1 || new_col > 8){
                    continue;
                }
            }
            ChessPosition new_pos = new ChessPosition(new_row, new_col);
            ChessPiece new_piece = board.getPiece(new_pos);
            if (new_piece == null || new_piece.getTeamColor() != piece.getTeamColor()){
                king_valid.add(new_pos);
            }

        }
        return king_valid;



    }
}
