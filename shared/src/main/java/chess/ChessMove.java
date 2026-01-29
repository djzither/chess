package chess;
import java.util.Objects;

public record ChessMove(ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece){
    public ChessPosition getStartPosition(){
        return startPosition;
    }
    public ChessPosition getEndPosition(){
        return endPosition;
    }
    public ChessPiece.PieceType getPromotionPiece(){
        return promotionPiece;
    }
}
//package chess;
//
//import java.util.Objects;
//
///**
// * Represents moving a chess piece on a chessboard
// * <p>
// * Note: You can add to this class, but you may not alter
// * signature of the existing methods.
// */
//public class ChessMove {
//// these are the thing you want all of your piece calculataors to do
////good to make a record, also python initconsturctors make variables on fly but in java, you need preallocated
//    private final ChessPosition startPosition;
//    private final ChessPosition endPosition;
//    private final ChessPiece.PieceType promotionPiece;
//
//    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
//                     ChessPiece.PieceType promotionPiece) {
//        // right click the name of the function and bind parameters to fields (under //
//        // context actions to have it do "this" and "private" for me
//        this.startPosition = startPosition;
//        this.endPosition = endPosition;
//        this.promotionPiece = promotionPiece;
//    }
//
//    /**
//     * @return ChessPosition of starting location
//     */
//    public ChessPosition getStartPosition() {
//        return startPosition;
//    }
//
//    /**
//     * @return ChessPosition of ending location
//     */
//    public ChessPosition getEndPosition() {
//        return endPosition;
//    }
//
//    /**
//     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
//     * chess move
//     *
//     * @return Type of piece to promote a pawn to, or null if no promotion
//     */
//    public ChessPiece.PieceType getPromotionPiece() {
//        // so literally on all of these we were just returning a certain piece
//        //this case what they want to promote to
//
//        return promotionPiece;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) {
//            return false;
//        }
//        ChessMove chessMove = (ChessMove) o;
//        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && promotionPiece == chessMove.promotionPiece;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(startPosition, endPosition, promotionPiece);
//    }
//
//    @Override
//    public String toString() {
//        // String.format is like fstring in python
//        // in this case we are concationating start and end position together
//        // add to string when we have pawn that has promotion
//        return String.format("%s%s", startPosition, endPosition);
//    }
//}
