package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.UserData;
import model.GameData;

import java.util.List;

//need this because we will be changing to database

public interface DataAccess {


    //testing
    void clear() throws DataAccessException;

    //user
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;

    //game
    void createGame(GameData game) throws DataAccessException;
    GameData getGame(String gameID) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;
    //auth
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    String authToUsername(String authToken) throws DataAccessException;
    void addGameInfo(GameData gameData) throws DataAccessException;
    int generateGameID() throws DataAccessException;






}

