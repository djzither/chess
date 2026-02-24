package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class sysMemory implements DataAccess {
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<Integer, GameData> games = new HashMap<>();
    private final Map<String, AuthData> authTokens = new HashMap<>();
    @Override
    public void clear() {
        users.clear();
        games.clear();
        authTokens.clear();
    }

    @Override
    public void createUser(UserData user){
        users.put(user.username(), user);
    }
    @Override
    public UserData getUser(String userName){
        return users.get(userName);
    }

    @Override
    public void createGame(GameData game) {
        games.put(game.gameId(), game);

    }

    @Override
    public GameData getGame(String gameID) {
        games.get(gameID);
    }

    @Override
    public List<GameData> listGames() {
        return List.copyOf(games.values());
    }

    @Override
    public void updateGame(GameData game) {
        games.put(game.gameId(), game);

    }

    @Override
    public void createAuth(AuthData auth) {
        authTokens.put(auth.AuthToken(), auth);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authTokens.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authTokens.remove(authToken);
    }

    public void updateUser(UserData user) throws DataAccessException{
        if (!users.containsKey(user.username())){
            throw new DataAccessException("User doesn't exist");
        }
        users.put(user.username(), user);
    }
}
