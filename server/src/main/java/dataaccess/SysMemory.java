package dataaccess;

import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedException;
import dataaccess.exceptions.UserNameTakenException;
import model.AuthData;
import model.GameData;
import model.UserData;

// gotta throw some errors here I think
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SysMemory implements DataAccess {
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
    public void createUser(UserData user) throws UserNameTakenException {
        if (users.containsKey(user.username())){
            throw new UserNameTakenException("Username Taken");
        }
        users.put(user.username(), user);
    }
    @Override
    public UserData getUser(String userName) throws UnauthorizedException {
        if (!users.containsKey(userName)) {
            throw new UnauthorizedException("Error: Unauthorized");
        }
        return users.get(userName);
    }
    //game stuff
    @Override
    public void createGame(GameData game) {
        games.put(game.gameId(), game);

    }

    @Override
    public GameData getGame(String gameID) {
        return(games.get(gameID));
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
        authTokens.put(auth.authToken(), auth);
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
