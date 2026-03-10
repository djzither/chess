package dataaccess;

import dataaccess.exceptions.DataAccessException;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class DataBaseInitialize {

    public static void initialize() throws DataAccessException {
        try {
            DatabaseManager.createDatabase();

            try (Connection conn = DatabaseManager.getConnection();
                 Statement chess = conn.createStatement()) {

                String users = """
                        CREATE TABLE IF NOT EXISTS users (
                        id INT NOT NULL AUTO_INCREMENT,
                        username varchar(256) NOT NULL,
                        password varchar(256) NOT NULL,
                        email varchar(256) NOT NULL,
                        PRIMARY KEY (`id`)
                        )
                        """;
                chess.executeUpdate(users);

                String games = """
                        CREATE TABLE IF NOT EXISTS games (
                        gameId INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        whiteUsername VARCHAR(256) NOT NULL,
                        blackUsername VARCHAR(256) NOT NULL,
                        gameName VARCHAR(256) NOT NULL,
                        game TEXT NOT NULL
                        )
                        """;

                chess.executeUpdate(games);


                String auth = """
                        CREATE TABLE IF NOT EXISTS auth (
                        auth_token VARCHAR(256) PRIMARY KEY,
                        username VARCHAR(256) NOT NULL
                        )""";
                chess.executeUpdate(auth);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Didn't initialize database" + e.getMessage(), e);
        }


    }
}