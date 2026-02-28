package server.service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.exceptions.*;
import model.GameData;

import java.util.List;

public class GameService {
    private final DataAccess dao;


    public GameService(DataAccess dao) {
        this.dao = dao;
    }

    public List<GameData> listGames(String authToken) throws UnauthorizedException, DataAccessException {
        if (authToken == "") {
            throw new UnauthorizedException();
        }
        if (dao.getAuth(authToken) == null){
            throw new UnauthorizedException();
        }

        List<GameData> games = dao.listGames();

        return games;



    }

    public int createGame(String authToken, String gameName) throws UnauthorizedException, BadRequestException, DataAccessException {
        if (dao.getAuth(authToken) == null){
            throw new UnauthorizedException();
        }
        if (authToken == null || gameName == null){
            throw new BadRequestException();
        }

        ChessGame newGame = new ChessGame();
        String userName = dao.authToUsername(authToken);

        int gameID = dao.generateGameID();
        GameData gameData = new GameData(gameID, null, null, gameName, newGame);



        dao.createGame(gameData);

        return gameID;

    }

    public void joinGame(String authToken, String playerColor, String gameID) throws BadRequestException,UnauthorizedException, AlreadyTakenException, DataAccessException {
        if (dao.getAuth(authToken) == null){
            throw new UnauthorizedException();
        }
        if (authToken == null || playerColor == null || gameID == null){
            throw new BadRequestException();
        }


        if (!playerColor.equals("BLACK") && !playerColor.equals("WHITE")){
            throw new BadRequestException();
        }
        GameData game = dao.getGame(gameID);

        if (game == null){
            throw new BadRequestException();
        }



        String userName = dao.authToUsername(authToken);
        if (playerColor.equals("WHITE")){
            if (dao.getGame(gameID).whiteUsername() != null){
                throw new AlreadyTakenException();
            }
        }
        if (playerColor.equals("BLACK")){
            if (dao.getGame(gameID).blackUsername() != null){
                throw new AlreadyTakenException();
            }
        }
        //need to finish method


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
