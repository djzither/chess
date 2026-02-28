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

    public List<GameData> listGames(String authToken) throws
            UnauthorizedException, DataAccessException {
        if (authToken.isEmpty()) {
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
        GameData gameData =
                new GameData(gameID, null, null, gameName, newGame);



        dao.createGame(gameData);

        return gameID;

    }

    public void joinGame(String authToken, String playerColorString, int gameID)
            throws BadRequestException,UnauthorizedException, AlreadyTakenException, DataAccessException {
        //GAME ID NULL ISN'T CHECKED!!!

        if (authToken == null || playerColorString == null){
            throw new BadRequestException();
        }

        if (!playerColorString.equals("WHITE") &&
                !playerColorString.equals("BLACK")) {
            throw new BadRequestException();
        }

        ChessGame.TeamColor playerColor = ChessGame.TeamColor.valueOf(playerColorString);

        if (playerColor != ChessGame.TeamColor.BLACK && playerColor != ChessGame.TeamColor.WHITE){
            throw new BadRequestException();
        }

        if (dao.getAuth(authToken) == null){
            throw new UnauthorizedException();
        }



        GameData game = dao.getGame(gameID);

        if (game == null){
            throw new BadRequestException();
        }



        String userName = dao.authToUsername(authToken);

        if (playerColor == ChessGame.TeamColor.WHITE){
            if (game.whiteUsername() != null){
                throw new AlreadyTakenException();
            }
        }
        if (playerColor == ChessGame.TeamColor.BLACK){
            if (game.blackUsername() != null){
                throw new AlreadyTakenException();
            }
        }
        //need to finish method


        GameData updatedGame;
        if (playerColor == ChessGame.TeamColor.WHITE){

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
