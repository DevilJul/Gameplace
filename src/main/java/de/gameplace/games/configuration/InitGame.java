package de.gameplace.games.configuration;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.gameplace.games.model.Game;

@Configuration
public class InitGame {

    @Bean
    Game game() {
        Game game = new Game();
        game.setPlayers(new ArrayList<>());
        game.setCards(new ArrayList<>());
        game.setNumberOfRounds(10);
        game.setCurrentRound(0);
        return game;
    }

}