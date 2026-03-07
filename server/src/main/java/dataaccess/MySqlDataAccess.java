package dataaccess;


import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws DataAccessException{
        DataBaseInitialize.initialize();
    }

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, user.username(), user.password(), user.email());


    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT username, password, email FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return new UserData(
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email"));
                    }
                    else{
                        return null;
                    }
                }
            }
        }catch (SQLException e){
            throw new DataAccessException("Unable to query user", e);
        }
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        var statement = "INSERT INTO games (data) VALUES (?)";
        String json = new Gson().toJson(game);
        executeUpdate(statement, json);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = "SELECT data FROM games WHERE game_id=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)){
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        String json = rs.getString("data");
                        GameData game = new Gson().fromJson(json, GameData.class);
                        return game;
                    }
                    else{
                        return null;
                    }
                }
            }
        }catch (SQLException e){
            throw new DataAccessException("unable to query game", e);
        }

    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public String authToUsername(String authToken) throws DataAccessException {
        return "";
    }

    @Override
    public int generateGameID() throws DataAccessException {
        return 0;
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to update database");
        }

        //gonna want a query one as well, idk but I can't find it in the petshop example
    }


}
