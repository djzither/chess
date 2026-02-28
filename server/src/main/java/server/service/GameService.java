package server.service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.exceptions.BadCreationRequest;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import model.GameData;

import java.util.List;

public class GameService {
    private final DataAccess dao;


    public GameService(DataAccess dao) {
        this.dao = dao;
    }

    public List<GameData> listGames(String authToken) throws UnauthorizedException, DataAccessException {
        if (authToken == "") {
            throw new UnauthorizedException("Error: Unauthorized");
        }
        if (dao.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: Unauthorized");
        }

        List<GameData> games = dao.listGames();

        return games;



    }

    public int createGame(String authToken, String gameName) throws UnauthorizedException, BadCreationRequest, DataAccessException {
        if (dao.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: Unauthorized");
        }
        ChessGame newGame = new ChessGame();
        String userName = dao.authToUsername(authToken);

        int gameID = dao.generateGameID();
        GameData gameData = new GameData(gameID, userName, null, gameName, newGame);



        dao.createGame(gameData);

        return gameID;

    }
}
