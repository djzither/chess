package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;

    private TeamColor currentTurn;

    public ChessGame(ChessBoard board, TeamColor currentTurn) {
        this.board = board;
        this.currentTurn = currentTurn;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTurn = team;
    }


    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece piece = board.getPiece(startPosition);

        Collection<ChessMove> pieceMoves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();


        for (ChessMove move : pieceMoves) {
            ChessBoard testBoard = board.copy();
            testBoard.addPiece(move.getEndPosition(), testBoard.getPiece(move.getStartPosition()));
            testBoard.addPiece(move.getStartPosition(), null);
            if(!isInCheckCloned(currentTurn, testBoard)){
                validMoves.add(move);





            }
        }
        return validMoves;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = findKing(teamColor);
        ChessPiece instance = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        Collection<ChessMove> possibleKingMoves = instance.pieceMoves(board, kingPos);
        for (int row = 1; row <= 8; row++)
            for (int col = 1; col <= 8; col++) {
                ChessPosition otherPos = new ChessPosition(row, col);
                ChessPiece otherPiece = board.getPiece(otherPos);
                if (otherPiece == null) {
                    continue;
                }

                if (otherPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> possibleOtherMoves = otherPiece.pieceMoves(board, otherPos);
                    for (ChessMove possibleOtherMove : possibleOtherMoves) {
                        if (possibleOtherMove.getEndPosition().equals(kingPos)) {
                            return (true);
                        }
                    }
                }
            }
        return false;
    }

    public boolean isInCheckCloned(TeamColor teamColor, ChessBoard testBoard) {
        ChessPosition kingPos = findKingCloned(teamColor, testBoard);
        ChessPiece instance = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        Collection<ChessMove> possibleKingMoves = instance.pieceMoves(testBoard, kingPos);
        for (int row = 1; row <= 8; row++)
            for (int col = 1; col <= 8; col++) {
                ChessPosition otherPos = new ChessPosition(row, col);
                ChessPiece otherPiece = testBoard.getPiece(otherPos);
                if (otherPiece == null) {
                    continue;
                }

                if (otherPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> possibleOtherMoves = otherPiece.pieceMoves(testBoard, otherPos);
                    for (ChessMove possibleOtherMove : possibleOtherMoves) {
                        if (possibleOtherMove.getEndPosition().equals(kingPos)) {
                            return (true);
                        }
                    }
                }
            }
        return false;
    }


    public ChessPosition findKing(TeamColor teamColor){
        for (int row = 1; row <= 8; row++){
            for (int col = 1; col <= 8; col++){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING){
                    return position;
                }
            }
        }
        return null;

    }

    private ChessPosition findKingCloned(TeamColor teamColor, ChessBoard board) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor
                        && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    return position;
                }
            }
        }
        return null;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && currentTurn == chessGame.currentTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", currentTurn=" + currentTurn +
                '}';
    }
}
