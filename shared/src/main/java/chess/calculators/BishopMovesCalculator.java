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

        List<ChessMove> bishopValid = new ArrayList<>();

        List<int[]> bishopPossibleDir = List.of(new int[]{1, 1}, new int[]{1, -1}, new int[]{-1, 1}, new int[]{-1, -1});

        for (int[] move : bishopPossibleDir){
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
                    ChessPosition newPos = new ChessPosition(x, y);
                    ChessPiece newPiece = board.getPiece(newPos);
                    if (newPiece == null) {
                        bishopValid.add(new ChessMove(position, newPos, null));;
                    }
                    if (newPiece != null && newPiece.getTeamColor() != piece.getTeamColor()) {
                        bishopValid.add(new ChessMove(position, newPos, null));;
                        break;
                    }
                    if (newPiece != null && newPiece.getTeamColor() == piece.getTeamColor()) {
                        break;
                    }

                }
            }
        return bishopValid;
        }

    }

