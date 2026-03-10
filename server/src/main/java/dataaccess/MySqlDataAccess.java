package dataaccess;


import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws DataAccessException{
        DataBaseInitialize.initialize();
    }

    @Override
    public void clear() throws DataAccessException {
        executeUpdate("TRUNCATE TABLE users");
        executeUpdate("TRUNCATE TABLE games");
        executeUpdate("TRUNCATE TABLE authToken");

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {// i need to do special password stuff
        var statement = """
                            INSERT INTO users (username, password, email) 
                            VALUES (?, ?, ?)
                            """;
        executeUpdate(statement, user.username(), user.password(), user.email());


    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            //more special password stuff
            var statement = """
                            SELECT username, password, email 
                            FROM users 
                            WHERE username=?
                            """;

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
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        String json = new Gson().toJson(game.game());
        executeUpdate(statement, game.whiteUsername(), game.blackUsername(),
                        game.gameName(), json);
        //i think that my generate id is in here but shoudl be later
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()){
            var statement = """
                SELECT gameId, whiteUsername, blackUsername, gameName, game
                FROM games
                WHERE gameId=?
                """;
                    try (PreparedStatement ps = conn.prepareStatement(statement)){
                        ps.setInt(1, gameID);
                        try (ResultSet rs = ps.executeQuery()){
                            if (rs.next()){


                                ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                                return new GameData(
                                        rs.getInt("gameId"),
                                        rs.getString("whiteUsername"),
                                        rs.getString("blackUsername"),
                                        rs.getString("gameName"),
                                        game
                                );
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
                List<GameData> result = new ArrayList<>();

                try (Connection conn = DatabaseManager.getConnection()){
                    var statement = "SELECT  gameId, whiteUsername, blackUsername, gameName, game FROM games";
                    try (PreparedStatement preparedStatement = conn.prepareStatement(statement)){
                        try (ResultSet rs = preparedStatement.executeQuery()){
                            while (rs.next()){
                                ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                                result.add(new GameData(
                                        rs.getInt("gameId"),
                                        rs.getString("whiteUsername"),
                                        rs.getString("blackUsername"),
                                        rs.getString("gameName"),
                                        game
                                ));
                            }
                        }
                    }
                }catch (SQLException e){
                    throw new DataAccessException("unable to query games", e);

            }
            return result;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        //this is select
        var statement = """
                UPDATE games
                set whiteUsername = ?, blackUsername = ?, gameName = ?, game = ?
                WHERE gameId=?
                """;
        String json = new Gson().toJson(game.game());
        executeUpdate(statement, game.whiteUsername(), game.blackUsername(),
                        game.gameName(), json, game.gameId());


    }


    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        var statement = "INSERT INTO authToken (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, auth.authToken(), auth.userName());
    }



    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()){
            var statement = """
                    SELECT authToken, username FROM authToken WHERE authToken = (?)""";
            try (PreparedStatement ps = connection.prepareStatement(statement)){
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return new AuthData(rs.getString("authToken"),
                                            rs.getString("username"));
                    }
                    else{
                        return null;
                    }
                }
            }
        }catch (SQLException e){
            throw new DataAccessException("couldn't query username");
        }
    }




    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = """
                DELETE FROM authToken WHERE authToken = (?)""";
        executeUpdate(statement, authToken);
    }

    @Override
    public String authToUsername(String authToken) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()){
            var statement = """
                    SELECT username FROM authToken WHERE authToken = (?)""";
            try (PreparedStatement ps = connection.prepareStatement(statement)){
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()){
                    if (rs.next()){
                        return rs.getString("username");
                    }
                    else{
                        return null;
                    }

                }
            }
        }catch (SQLException e){
            throw new DataAccessException("couldn't get username with the given auth");
        }
    }



    @Override
    public int generateGameID() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            var statement = """
                    SELECT MAX(gameId) FROM games""";
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) + 1;
                } else {
                    return 1;
                }

            }
        }catch (SQLException e) {
            throw new DataAccessException("Unable to get next game id");
        }
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
