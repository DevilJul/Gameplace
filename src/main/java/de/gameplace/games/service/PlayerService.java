package de.gameplace.games.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.exceptions.PlayerNameException;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Player;
import de.gameplace.games.model.PlayerOut;
import de.gameplace.games.tools.PlayerHelper;

@Service
public class PlayerService {

    @Autowired
    Game game;


    public void checkIfPlayerExists(final String name) throws PlayerNameException {
        if (game.getPlayers().stream().anyMatch(p -> p.getName().equals(name))) {
            throw new PlayerNameException();
        }
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