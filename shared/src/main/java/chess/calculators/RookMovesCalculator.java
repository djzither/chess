package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.List;

public class RookMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece.PieceType pieceType;
    public RookMovesCalculator(ChessPosition position, ChessBoard board, ChessPiece.PieceType pieceType) {

        this.board = board;
        this.position = position;
        this.pieceType = pieceType;

    }

    public List<ChessMove> getPathValid() {
        int row = position.getRow();
        int col = position.getColumn();

        ChessPiece piece = board.getPiece(position);

        List<ChessMove> rook_valid = new ArrayList<>();

        List<int[]> rook_possible_dir = new ArrayList<>(List.of(new int[]{0, 1}, new int[]{0, -1}, new int[]{-1, 0}, new int[]{1, 0}));

        for (int[] move : rook_possible_dir){
            int x = row;
            int y = col;
            for (int i = 1; i <= 8; i++){
                x += move[0];
                y += move[1];
                //check if on board
                if (x < 1 || x > 8 || y < 1 || y > 8) {
                    break;
                }
                //create new position and do checks on it
                ChessPosition new_pos = new ChessPosition(x, y);
                ChessPiece new_piece = board.getPiece(new_pos);
                if (new_piece == null) {
                    rook_valid.add(new ChessMove(position, new_pos, null)); // null because Rooks don’t promote

                }
                if (new_piece != null && new_piece.getTeamColor() != piece.getTeamColor()) {
                    rook_valid.add(new ChessMove(position, new_pos, null)); // null because Rooks don’t promote

                    break;
                }
                if (new_piece != null && new_piece.getTeamColor() == piece.getTeamColor()) {
                    break;
                }

            }
        }
        return rook_valid;
    }

}

