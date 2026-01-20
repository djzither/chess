package chess;

import chess.calculators.PieceMovesCalculator;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPosition position;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }
    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor(){
        //this. is implied here so dont need to repeat it again
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    //collection allows us to return different types of chess move objects
    //like possible destinatioins, nessary info to execute each move legally with rules for piece
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        //import list
        //we are importing variables from different classes above in func name
//
//        ChessPiece piece = board.getPiece(myPosition);
//        if (piece.getPieceType() == PieceType.BISHOP){
//            return List.of(new ChessMove(new ChessPosition(5,4), new ChessPosition(1,8), null));
//        }
//        this allows me to have access to the objects for this piece
        PieceType pieces = getPieceType();
        PieceType piece = PieceType.QUEEN;
        PieceMovesCalculator instance = new PieceMovesCalculator(board, myPosition, piece, getTeamColor());
        List<ChessPosition> valid_moves = instance.findRightMove();
        Collection<ChessMove> moves = new java.util.ArrayList<>();

        for (ChessPosition i : valid_moves)
            moves.add(new ChessMove(myPosition, i, null));
        return moves;
    } // ofc in the future we will impliment all of the pieces programatically instead of hard coding bc it would be horrible

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece piece = (ChessPiece) o;
        return pieceColor == piece.pieceColor && Objects.equals(position, piece.position) && type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, position, type);
    }
}
