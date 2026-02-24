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