package de.gameplace.games.configuration;

import java.util.ArrayList;

import org.springframework.context.annotation.Configuration;

import de.gameplace.games.model.Player;
import de.gameplace.games.model.PlayerOut;

@Configuration
public abstract class InitPlayer {

    //@Bean
    public static Player player(String name) {
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

    //@Bean
    public static PlayerOut playerOut(Player playerIntern) {
        PlayerOut playerOut = new PlayerOut();
        playerOut.setName(playerIntern.getName());
        playerOut.setCards(playerIntern.getCurrentCards());
        playerOut.setPoints(playerIntern.getPointsTotal());
        playerOut.setBets(playerIntern.getBets() == null || playerIntern.getBets().isEmpty() ? 0
                : playerIntern.getBets().get(playerIntern.getBets().size() - 1));

        return playerOut;
    }

}