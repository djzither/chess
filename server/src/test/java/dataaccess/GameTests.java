package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTests {
    private DataAccess dao;
    private int gameId;
    @BeforeEach
    public void setupDb() throws DataAccessException {
        dao = new MySqlDataAccess();


        dao.clear();
        ChessGame chessGame = new ChessGame();
        GameData gameData = new GameData(
                0,
                null,
                null,
                "game",
                chessGame
        );
        gameId = dao.createGame(gameData);
    }

    @Test
    void createGamePos() throws DataAccessException {



        GameData game = dao.getGame(gameId);
        assertNull(game.whiteUsername());
        assertEquals("game", game.gameName());

    }
    @Test
    void createGameNeg() throws DataAccessException{
        GameData gameData = new GameData(0,
                null,
                null,
                null,
                null
        );
        assertThrows(DataAccessException.class, () ->{
            dao.createGame(gameData);
        });
    }

    @Test
    void getGame() throws DataAccessException {



        GameData gameData = dao.getGame(gameId);
        assertNotNull(gameData);



    }

    @Test
    void listGames() {
    }

    @Test
    void updateGame() {
    }
}