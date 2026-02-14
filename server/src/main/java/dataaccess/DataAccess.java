package dataaccess;

import model.AuthData;
import model.UserData;
import model.GameData;

import java.util.List;

//need this because we will be changing to database

public interface DataAccess {


    //testing
    void clear();

    //user
    void createUser(UserData user);
    UserData getUser(String username);

    //game
    void createGame(GameData game);
    GameData getGame(String gameID);
    List<GameData> listGames();
    void updateGame(GameData game);
    //auth
    void createAuth(AuthData auth);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
}

