package chess.calculators;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.List;

public class BishopMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessPiece.PieceType pieceType;
    public BishopMovesCalculator(ChessPosition position, ChessBoard board, ChessPiece.PieceType pieceType) {
        this.board = board;
        this.position = position;
        this.pieceType = pieceType;
    }

    public List<ChessPosition> getPathValid() {
        int row = position.getRow();
        int col = position.getColumn();

        ChessPiece piece = board.getPiece(position);

        List<ChessPosition> bishop_valid = new ArrayList<>();

        List<int[]> bishop_possible_dir = new ArrayList<>(List.of(new int[]{1, 1}, new int[]{1, -1}, new int[]{-1, 1}, new int[]{-1, -1}));

        for (int[] move : bishop_possible_dir){
                int x = row;
                int y = col;
                for (int i = 1; i <= 8; i++){
                    x += move[0];
                    y += move[1];
                    if (x < 1 || x > 8 || y < 1 || y > 8) {
                        break;
                    }
                    ChessPosition new_pos = new ChessPosition(x, y);
                    ChessPiece new_piece = board.getPiece(new_pos);
                    if (new_piece == null || new_piece.getTeamColor() != piece.getTeamColor()){
                        bishop_valid.add(new_pos);
                        break;

                    }

                }
            }

        }
    }

