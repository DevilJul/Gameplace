package de.gameplace.games.service;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.exceptions.IllegalPlayActionException;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Player;
import de.gameplace.games.model.Wizard.ActionPlaceBet;
import de.gameplace.games.tools.GameHelper;
import de.gameplace.games.tools.ListHelper;

public class GamestateBetManager {

    // @Value("${wizard.rules.allowEqualBets}") TODO
    private static boolean ALLOW_EQUAL_BETS = false;

    Game game;
    Player player;
    int bet;

    public static GamestateBetManager build() {
        return new GamestateBetManager();
    }

    public GamestateBetManager game(Game game) {
        this.game = game;
        return this;
    }

    public GamestateBetManager player(Player player) {
        this.player = player;
        return this;
    }

    
    public void placeBet(ActionPlaceBet action) throws GameException {
        
        int bet = action.getTricks();

        checkValidBet();
        
        ListHelper.changeLastValue(player.getBets(), bet);
    }

    private void checkValidBet() throws IllegalPlayActionException {

        if (bet < 0) {
            throw new IllegalPlayActionException();
        }

        checkLastPlayerAllowedToPlaceBet();
    }

    private void checkLastPlayerAllowedToPlaceBet() throws IllegalPlayActionException {
        if (!ALLOW_EQUAL_BETS &&
            GameHelper.isCurrentActionPlayerLast(game) &&
            game.getCurrentRound() == getTotalBets() + bet) {
            
                throw new IllegalPlayActionException();
        }
    }

    private int getTotalBets() {
        return game.getPlayers().stream().mapToInt(p -> ListHelper.getLastValue(p.getBets())).sum();
    }

}