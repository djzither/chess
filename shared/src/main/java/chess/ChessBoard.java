package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    //so this creates a chess board that holds chess pieces and then you
    // make it
    ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        
    }
    //making chess board as a 2d array


    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //we use the chessposition "position" object and chesspiece "piece" object
        // the chessposion position object has methods "getrow" and "get col"
        //because my tests are 1 based and my array is zero based, I subtract 1
        squares[position.getRow()-1][position.getColumn()-1] = piece;

    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];

    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    //all the pieces going in the starting position
    public void resetBoard() {
        // clear the board
        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                squares[row][col] = null;
            }
        }
        // put the pawns
        for (int col = 0; col < 8; col++){
            addPiece(new ChessPosition(2, col), new Pawn("White"));
        }
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    // overriding the "to" string is good for debugging and i should do it everywhere
    @Override
    public String toString() {
        return super.toString();
    }
}
