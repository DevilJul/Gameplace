package de.gameplace.games.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.gameplace.games.model.Card;
import de.gameplace.games.model.Card.CardColor;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Game.GamestateEnum;

@Configuration
public class InitGame {

    @Bean
    Game game() {
        Game game = new Game();
        game.setPlayers(new ArrayList<>());
        game.setCards(generateCards());
        game.setNumberOfRounds(10);
        game.setCurrentRound(0);
        game.setGamestate(GamestateEnum.INIT);
        return game;
    }

    private List<Card> generateCards() {
        List<Card> cards = new ArrayList<Card>(60);

        for (int i = 1; i <= 13; i++) {
            cards.add(generateCard(CardColor.RED, i));
            cards.add(generateCard(CardColor.BLUE, i));
            cards.add(generateCard(CardColor.YELLOW, i));
            cards.add(generateCard(CardColor.GREEN, i));
        }

        for (int i = 0; i < 4; i++) {
            cards.add(generateCard(CardColor.WHITE, 0));
            cards.add(generateCard(CardColor.WHITE, 14));
        }

        return cards;
    }

    private Card generateCard(CardColor color, int value) {
        Card c = new Card();
        c.setCardColor(color);
        c.setValue(value);
        return c;
    }

}