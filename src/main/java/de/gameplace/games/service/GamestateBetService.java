package de.gameplace.games.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.exceptions.IllegalPlayActionException;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Player;

@Service
public class GamestateBetService {

    @Autowired
    Game game;

    @Autowired
    GameService gameService;

    @Autowired
    GamePlayService gamePlayService;

    public void playBet(Player player, String betString) throws GameException {

        if (!game.getPlayers().get(game.getCurrentActionPlayerIndex()).equals(player)) {
            throw new IllegalPlayActionException();
        }

        placeBet(player, betString);
        
        if (gameService.isCurrentActionPlayerLast()) {
            gameService.incrementCurrentActionPlayer();
            gamePlayService.proceedToGamestatePlay();
        } else {
            gameService.incrementCurrentActionPlayer();
        }
    }

    private void placeBet(Player player, String betString) throws GameException {
        
        int bet = resolveBetString(betString);

        if (gameService.isCurrentActionPlayerLast() &&
            game.getCurrentRound() == getTotalBets() + bet) {   // number of bets is equal to possible tricks
                throw new IllegalPlayActionException();
        }
        
        player.getBets().set(player.getBets().size() - 1, bet);
    }

    private int resolveBetString(String betString) throws GameException {
        try {
            int bet = Integer.parseInt(betString);
            if (bet < 0) {
                throw new IllegalPlayActionException();
            }
            return bet;
        } catch (NumberFormatException e) {
            throw new IllegalPlayActionException();
        }
    }

    private int getTotalBets() {
        return game.getPlayers().stream().mapToInt(p -> p.getBets().get(game.getCurrentRound())).sum();
    }

}