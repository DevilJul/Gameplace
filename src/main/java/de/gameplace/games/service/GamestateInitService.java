package de.gameplace.games.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gameplace.games.configuration.InitPlayer;
import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.exceptions.IllegalGamestateException;
import de.gameplace.games.exceptions.NoNewPlayerAllowedException;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Game.GamestateEnum;
import de.gameplace.games.model.Player;
import de.gameplace.games.model.PlayerOut;

@Service
public class GamestateInitService {

    @Autowired
    Game game;

    @Autowired
    PlayerService playerService;

    @Autowired
    GameService gameService;

    @Autowired
    GamePlayService gamePlayService;

    //@Value("$(wizard.maxPlayers)") TODO
    int maxPlayers = 6;

    public PlayerOut addPlayer(final String name) throws GameException {

        if (game.getGamestate() != GamestateEnum.INIT) {
            throw new IllegalGamestateException();
        }

        if (game.getPlayers().size() >= maxPlayers) {
            throw new NoNewPlayerAllowedException();
        }

        playerService.checkIfPlayerExists(name);

        Player newPlayer = InitPlayer.player(name);
        game.getPlayers().add(newPlayer);

        return InitPlayer.playerOut(newPlayer);
    }

    public void setPlayerOkay(Player player) {
        player.setCurrentOkayState(true);

        if (canStartGame()) {
            gamePlayService.startGame();
        }
    }

    private boolean canStartGame() {
        return game.getPlayers().size() >= 3 && gameService.allPlayersOkay();
    }
}