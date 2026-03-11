package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTests {
    private DataAccess dao;
    private int gameId;
    @BeforeEach
    public void setupSQLDatabase() throws DataAccessException {
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
    void createGameNeg(){
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
    void getGameNeg() throws DataAccessException{
        GameData gameData = dao.getGame(111);
        assertNull(gameData);


    }

    @Test
    void listGamesPos() throws DataAccessException {
        ChessGame chessGame2 = new ChessGame();
        GameData gameData = new GameData(
                0,
                null,
                null,
                "game2",
                chessGame2
        );
        dao.createGame(gameData);

        List<GameData> games = dao.listGames();



        assertEquals(2, games.size());

    }
    @Test
    void listGamesNeg() throws DataAccessException{
        dao.clear();
        List<GameData> listGames = dao.listGames();
        assertEquals(0, listGames.size());


    }

    @Test
    void updateGamePos() throws DataAccessException {
        GameData gameData = new GameData(
                1,
                "test",
                "test",
                "game2",
                new ChessGame()
        );
        dao.updateGame(gameData);
        assertEquals(gameData ,dao.getGame(1));
    }

    @Test
    void updateGameNeg() throws DataAccessException{

        GameData gameData = new GameData(
                999,
                null,
                null,
                null,
                new ChessGame()
        );
        dao.updateGame(gameData);
        GameData game = dao.getGame(999);
        assertNull(game);

    }
}