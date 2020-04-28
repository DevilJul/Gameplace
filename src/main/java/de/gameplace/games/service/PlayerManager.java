package de.gameplace.games.service;

import java.util.List;
import java.util.stream.Collectors;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.exceptions.NoNewPlayerAllowedException;
import de.gameplace.games.exceptions.PlayerNameException;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Player;
import de.gameplace.games.model.PlayerOut;
import de.gameplace.games.tools.PlayerHelper;

public class PlayerManager {

    Game game;

    public PlayerManager(Game game) {
        this.game = game;
    }

    public Player addPlayer(String playerId, String playerName) throws GameException {
        if (game.getPlayers().size() >= game.getMaxPlayers()) {
            throw new NoNewPlayerAllowedException();
        }

        checkIfPlayerExists(playerId);
        game.getPlayers().add(PlayerHelper.getPlayer(playerId, playerName));

        return getPlayerById(playerId);
    }

    public void checkIfPlayerIsManager(String id) throws GameException {
        if (!id.equalsIgnoreCase(game.getManager().getId())) {
            throw new PlayerNameException("Player is not manager");
        }
    }

    public void checkCorrectNumberOfPlayersToStart() throws GameException {
        if (game.getPlayers().size() < game.getMinPlayers() ||game.getPlayers().size() > game.getMaxPlayers()) {
            throw new GameException("Not correct number of players to start game");
        }
    }

    public void checkIfPlayerExists(final String id) throws PlayerNameException {
        if (game.getPlayers().stream().anyMatch(p -> p.getId().equals(id))) {
            throw new PlayerNameException();
        }
    }

    public Player getPlayerById(final String id) throws PlayerNameException {
        return game.getPlayers().stream().filter(p -> p.getId().equals(id)).findAny().orElseThrow(() -> new PlayerNameException());
    }

    public Player getPlayerByName(final String name) throws PlayerNameException {
        return game.getPlayers().stream().filter(p -> p.getName().equals(name)).findAny().orElseThrow(() -> new PlayerNameException());
    }

    public PlayerOut getPlayerOut(final String name) throws GameException {
        return PlayerHelper.getPlayerOut(getPlayerByName(name));
    }

    public List<PlayerOut> getPlayersOut() throws GameException {
        return game.getPlayers().stream().map(p -> PlayerHelper.getPlayerOut(p)).collect(Collectors.toList());
    }

    public PlayerOut addPlayer(final PlayerOut playerFromUser) throws GameException {
        // TODO create player if allowed
        return getPlayerOut(playerFromUser.getName());
    }

}