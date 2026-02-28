package server.service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.exceptions.*;
import model.AuthData;
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

    public void joinGame(String authToken, String playerColor, String gameID)
    throws BadJoinRequest, UnauthorizedException, GameTakenException, DataAccessException {
        if (authToken == null || playerColor == null || gameID == null){
            throw new BadJoinRequest("Error: Bad Request");
        }
        if (dao.getAuth(authToken) == null){
            throw new UnauthorizedException("Error: Unauthorized");
        }

        String userName = dao.authToUsername(authToken);
        if (playerColor.equals("WHITE")){
            if (dao.getGame(gameID).whiteUsername() != null){
                throw new GameTakenException("Error: already taken");
            }
        }
        if (playerColor.equals("BLACK")){
            if (dao.getGame(gameID).blackUsername() != null){
                throw new GameTakenException("Error: already taken");
            }
        }
        //need to finish method
        GameData game = dao.getGame(gameID);


        GameData updatedGame;
        if ("WHITE".equals(playerColor)){

            updatedGame = new GameData(game.gameId(), userName, game.blackUsername(),
                    game.gameName(), game.game());

        }else{
            updatedGame = new GameData(game.gameId(), game.whiteUsername(), userName,
                    game.gameName(), game.game());
        }
        dao.updateGame(updatedGame);

        //basically i need to get game by auth token
        //then I need to get the game name
        //then I need to get the game by game name

        //or I could get the game by auth token so then I can use it

    }
}
