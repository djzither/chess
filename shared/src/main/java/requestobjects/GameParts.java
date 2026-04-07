package requestobjects;

import chess.ChessGame;

public record GameParts(int gameID,
                        String whiteUsername,
                        String blackUsername,
                        String gameName,
                        ChessGame game) { }
