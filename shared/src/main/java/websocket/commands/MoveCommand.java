package websocket.commands;

import chess.ChessMove;

import java.util.Objects;

public class MoveCommand extends UserGameCommand{
    private ChessMove move;

    public MoveCommand() {
        super(CommandType.MAKE_MOVE, null, null);

    }
    public MoveCommand(String authToken, Integer gameId, ChessMove move){
        super(CommandType.MAKE_MOVE, authToken, gameId);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }
}
