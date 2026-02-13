package dataaccess;

import model.AuthData;
import model.UserData;
import model.GameData;


public interface DataAccess {
    static void createUser(UserData newUser) {
    }

    static void createAuth(AuthData authData) {
    }
    //this is so werid, i don't get

    void clear();
    void createUser(User user);
    User getUser(String username);
    void createGame(Game game);
    Game getGame(String gameID);
    List<Game> listGames();
    void updateGame(Game game);
    void createAuth(Auth auth);
    Auth getAuth(String authToken);
    void deleteAuth(String authToken);
}

