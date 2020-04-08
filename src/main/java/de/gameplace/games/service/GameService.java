package de.gameplace.games.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gameplace.games.model.Game;

@Service
public class GameService {

    @Autowired
    Game game;

    public boolean allPlayersOkay() {
        return game.getPlayers().stream().allMatch(p -> p.isCurrentOkayState());
    }

    public boolean isCurrentActionPlayerLast() {
        return calcIncrementPlayerIndex(game.getCurrentActionPlayerIndex()) == game.getCurrentStartPlayerIndex();
    }

    public void incrementCurrentActionPlayer() {
        game.setCurrentActionPlayerIndex(calcIncrementPlayerIndex(game.getCurrentActionPlayerIndex()));
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
}