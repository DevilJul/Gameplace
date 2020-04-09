package de.gameplace.games.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gameplace.games.exceptions.IllegalPlayActionException;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Player;

@Service
public class GameService {

    @Autowired
    Game game;

    public boolean allPlayersOkay() {
        return game.getPlayers().stream().allMatch(p -> p.isCurrentOkayState());
    }

    public boolean isCurrentActionPlayerLast() {
        return calcIncrementPlayerIndex(game.getGameRound().getCurrentActionPlayerIndex()) == game.getCurrentStartPlayerIndex();
    }

    public void incrementCurrentActionPlayer() {
        game.getGameRound().setCurrentActionPlayerIndex(calcIncrementPlayerIndex(game.getGameRound().getCurrentActionPlayerIndex()));
    }

    public void incrementCurrentStartPlayer() {
        game.setCurrentStartPlayerIndex(calcIncrementPlayerIndex(game.getCurrentStartPlayerIndex()));
    }

    private int calcIncrementPlayerIndex(int currentIndex) {
        currentIndex++;
        if (currentIndex == game.getPlayers().size()) {
            currentIndex = 0;
        }
        return currentIndex;
    }

    public void checkIsPlayersTurn(Player player) throws IllegalPlayActionException{
        if (!game.getPlayers().get(game.getGameRound().getCurrentActionPlayerIndex()).equals(player)) {
            throw new IllegalPlayActionException();
        }
    }
}