package de.gameplace.games.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.model.GameAction;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Player;
import de.gameplace.games.tools.PlayerHelper;

@Service
public class GameManagerService {

    Map<String, GameManager> activeGames = new HashMap<String, GameManager>();

    // TODO remove!
    public GameManager getGameManager(String gameId) throws GameException {
        return getGameManagerForId(gameId);
    }

    public Collection<GameManager> getGameManagers() throws GameException {
        return activeGames.values();
    }

    public Game initNewGame(String gameId, String gameName, String playerId) throws GameException {
        return initNewGame(gameId, gameName, playerId, playerId);
    }

    public Game initNewGame(String gameId, String gameName, String playerId, String playerName) throws GameException {
        if (gameId == null || gameId.isEmpty()) {
            gameId = String.valueOf(System.currentTimeMillis());
        }

        if (activeGames.containsKey(gameId)) {
            throw new GameException("GameId already exists. Can't create new one.");
        }

        Player manager = PlayerHelper.getPlayer(playerId, playerName);

        if (gameName.equalsIgnoreCase("wizard")) {  // TODO
            GameManager gameManagar = new GameManagerWizard(gameId, manager);
            activeGames.put(gameId, gameManagar);
            return gameManagar.getGame();
        } else {
            throw new GameException("Unknown game name.");
        }

    }

    public void joinGame(String gameId, String playerId) throws GameException {
        getGameManagerForId(gameId).addPlayer(playerId);
    }

    public void joinGame(String gameId, String playerId, String playerName) throws GameException {
        getGameManagerForId(gameId).addPlayer(playerId, playerName);
    }

    public void startGame(String gameId, String playerId) throws GameException {
        getGameManagerForId(gameId).startGame(playerId);
    }

    public void playAction(String gameId, String playerId, GameAction playAction) throws GameException {
        GameManager currentManager;
        if (gameId != null && !gameId.isEmpty()) {
            currentManager = getGameManagerForId(gameId);
        } else if (playerId != null && !playerId.isEmpty()) {
            currentManager = getGameManagerForPlayerId(playerId);
        } else {
            throw new GameException("Cannot identify game");
        }

        currentManager.playAction(playerId, playAction);
    }

    private GameManager getGameManagerForPlayerId(final String playerId) throws GameException {
        return activeGames.values().stream()
                .filter(manager -> manager.getGame().getPlayers().stream()
                    .map(player -> player.getId())
                    .anyMatch(pId -> pId.equals(playerId)))
                .findFirst()
                .orElseThrow(() -> new GameException("No game with this player exists"));
    }

    private GameManager getGameManagerForId(String gameId) throws GameException {
        if (gameId == null || gameId.isEmpty() || !activeGames.containsKey(gameId)) {
            throw new GameException("correct gameId must be provided");
        }
        return activeGames.get(gameId);
    }

}