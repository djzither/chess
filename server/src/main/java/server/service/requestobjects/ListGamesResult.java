package server.service.requestobjects;

import model.GameData;

import java.util.List;

public record ListGamesResult(List<GameParts> games) {

}
