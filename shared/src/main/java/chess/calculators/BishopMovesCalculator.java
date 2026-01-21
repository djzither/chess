package chess.calculators;

import chess.ChessBoard;
import chess.ChessMove;
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

    public List<ChessMove> getPathValid() {
        int row = position.getRow();
        int col = position.getColumn();

        ChessPiece piece = board.getPiece(position);

        List<ChessMove> bishop_valid = new ArrayList<>();

        List<int[]> bishop_possible_dir = List.of(new int[]{1, 1}, new int[]{1, -1}, new int[]{-1, 1}, new int[]{-1, -1});

        for (int[] move : bishop_possible_dir){
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
                        bishop_valid.add(new ChessMove(position, new_pos, null));;
                    }
                    if (new_piece != null && new_piece.getTeamColor() != piece.getTeamColor()) {
                        bishop_valid.add(new ChessMove(position, new_pos, null));;
                        break;
                    }
                    if (new_piece != null && new_piece.getTeamColor() == piece.getTeamColor()) {
                        break;
                    }

                }
            }
        return bishop_valid;
        }

    }

