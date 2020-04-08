package de.gameplace.games.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.gameplace.games.configuration.InitPlayer;
import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.exceptions.NoNewPlayerAllowedException;
import de.gameplace.games.exceptions.PlayerNameException;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Player;
import de.gameplace.games.model.PlayerOut;

@Service
public class GameService {

    @Autowired
    Game game;

    //@Value("$(wizard.maxPlayers)") TODO
    int maxPlayers = 6;

    public PlayerOut getPlayerOut(final String name) throws GameException {
        return convertPlayerToOut(getPlayerByName(name));
    }

    public List<PlayerOut> getPlayersOut() throws GameException {
        return game.getPlayers().stream().map(p -> InitPlayer.playerOut(p)).collect(Collectors.toList());
    }

    public PlayerOut addPlayer(final String name) throws GameException {

        // TODO: If game is running, new players can't be added

        if (game.getPlayers().size() >= maxPlayers) {
            throw new NoNewPlayerAllowedException();
        }

        if (getPlayerByName(name).isPresent()) {
            throw new PlayerNameException();
        }

        Player newPlayer = InitPlayer.player(name);
        game.getPlayers().add(newPlayer);

        return InitPlayer.playerOut(newPlayer);
    }

    public PlayerOut addPlayer(final PlayerOut playerFromUser) throws GameException {
        // TODO create player if allowed
        return getPlayerOut(playerFromUser.getName());
    }

    private PlayerOut convertPlayerToOut(final Optional<Player> playerIntern) {
        return playerIntern.map(p -> InitPlayer.playerOut(p)).orElse(null);
    }

    private Optional<Player> getPlayerByName(final String name) {
        return game.getPlayers().stream().filter(p -> p.getName().equals(name)).findAny();
    }

}