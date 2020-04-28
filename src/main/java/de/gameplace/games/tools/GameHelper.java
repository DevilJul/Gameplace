package de.gameplace.games.tools;

import de.gameplace.games.model.Game;

public class GameHelper {

    public static boolean isCurrentActionPlayerLast(Game game) {
        return calcIncrementPlayerIndex(game, game.getGameRound().getCurrentActionPlayerIndex()) == game.getCurrentStartPlayerIndex();
    }

    public static int calcIncrementPlayerIndex(Game game, int currentIndex) {
        currentIndex++;
        if (currentIndex == game.getPlayers().size()) {
            currentIndex = 0;
        }
        return currentIndex;
    }

}