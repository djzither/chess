package websocket.messages;

import chess.ChessGame;

public class LoadGame extends ServerMessage{
    private final String boardinJson;

    public LoadGame(String boardinJson){
        super(ServerMessageType.LOAD_GAME);
        this.boardinJson = boardinJson;
    }

    public String getBoardinJson(){
        return boardinJson;
    }

    public ChessGame getGame() {
    }
}
