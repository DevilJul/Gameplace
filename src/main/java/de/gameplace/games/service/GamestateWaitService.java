package de.gameplace.games.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gameplace.games.model.Game;
import de.gameplace.games.model.Player;

@Service
public class GamestateWaitService {

    @Autowired
    Game game;

    @Autowired
    GameService gameService;

    @Autowired
    GamePlayService gamePlayService;

    public void setPlayerOkay(Player player) {
        player.setCurrentOkayState(true);

        if (gameService.allPlayersOkay()) {

            if (isGameFinished()) {
                gamePlayService.proceedToGamestateEnd();
            } else {
                gamePlayService.proceedToGamestateBet();
            }
        }
    }

    private boolean isGameFinished() {
        return game.getCurrentRound() == game.getNumberOfRounds();
    }

}