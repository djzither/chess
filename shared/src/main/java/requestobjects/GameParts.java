package requestobjects;

public record GameParts(int gameID,
                        String whiteUsername,
                        String blackUsername,
                        String gameName) { }
