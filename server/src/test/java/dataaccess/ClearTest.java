package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public class ClearTest {
    @Test
    void clear() throws DataAccessException {
        DataAccess dao = new MySqlDataAccess();
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(
                0,
                null,
                null,
                "game",
                chessGame
        );
        int gameId = dao.createGame(gameData);
        dao.clear();
        GameData newGame = dao.getGame(gameId);
        assertNull(newGame);




    }
}
