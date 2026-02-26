package server.service;

import dataaccess.DataAccess;
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

        List<GameData> games = dao.listGames();

        return games;



    }
}
