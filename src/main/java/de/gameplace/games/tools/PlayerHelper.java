package de.gameplace.games.tools;

import java.util.ArrayList;

import de.gameplace.games.model.Player;
import de.gameplace.games.model.PlayerOut;

public class PlayerHelper {

    public static Player getPlayer(String name) {
        Player player = new Player();
        player.setName(name);
        player.setBets(new ArrayList<>());
        player.setTricks(new ArrayList<>());
        player.setPoints(new ArrayList<>());
        player.setCurrentCards(new ArrayList<>());
        player.setPointsTotal(0);
        player.setCurrentOkayState(false);

        return player;
    }

    public static PlayerOut getPlayerOut(Player playerIntern) {
        PlayerOut playerOut = new PlayerOut();
        playerOut.setName(playerIntern.getName());
        playerOut.setCards(playerIntern.getCurrentCards());
        playerOut.setPoints(playerIntern.getPointsTotal());
        playerOut.setBets(playerIntern.getBets() == null || playerIntern.getBets().isEmpty() ? 0
                : playerIntern.getBets().get(playerIntern.getBets().size() - 1));

        return playerOut;
    }

}