package de.gameplace.games.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.exceptions.IllegalPlayActionException;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Player;
import de.gameplace.games.tools.ListHelper;

@Service
public class GamestateBetService {

    @Autowired
    Game game;

    @Autowired
    GameService gameService;

    @Autowired
    GamePlayService gamePlayService;

    @Value("${wizard.rules.allowEqualBets}")
    boolean allowEqualBets;

    public void playBet(Player player, String betString) throws GameException {

        gameService.checkIsPlayersTurn(player);

        placeBet(player, betString);
        
        if (gameService.isCurrentActionPlayerLast()) {
            gamePlayService.proceedToGamestatePlay();
        } else {
            gameService.incrementCurrentActionPlayer();
        }
    }

    private void placeBet(Player player, String betString) throws GameException {
        
        int bet = resolveBetString(betString);

        checkLastPlayerAllowedToPlaceBet(bet);
        
        ListHelper.changeLastValue(player.getBets(), bet);
    }

    private void checkLastPlayerAllowedToPlaceBet(int bet) throws IllegalPlayActionException {
        if (!allowEqualBets &&
            gameService.isCurrentActionPlayerLast() &&
            game.getCurrentRound() == getTotalBets() + bet) {
            throw new IllegalPlayActionException();
    }
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
        return game.getPlayers().stream().mapToInt(p -> ListHelper.getLastValue(p.getBets())).sum();
    }

}