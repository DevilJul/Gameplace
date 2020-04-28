package de.gameplace.games.tools;

import java.util.ArrayList;
import java.util.List;

import de.gameplace.games.model.Card;
import de.gameplace.games.model.Card.CardColor;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Game.GamestateEnum;
import de.gameplace.games.model.Player;

public class WizardGameHelper {

    // TODO
    //@Value("${wizard.cards.value.fool})")
    private static int VALUE_FOOL = 0;
    //@Value("${wizard.cards.value.wizard})")
    private static int VALUE_WIZARD = 14;

    // @Value("${wizard.minPlayers}") TODO
    private static int MIN_PLAYERS = 3;
    // @Value("${wizard.maxPlayers}") TODO
    private static int MAX_PLAYERS = 6;

    public static Game generateWizardGame(String gameId, Player manager) {
        final Game game = new Game();
        game.setGameId(gameId);
        game.setManager(manager);
        game.setPlayers(new ArrayList<>());
        game.getPlayers().add(manager);
        game.setCards(generateCards());
        game.setNumberOfRounds(0);
        game.setCurrentRound(0);
        game.setGamestate(GamestateEnum.INIT);
        game.setMaxPlayers(MAX_PLAYERS);
        game.setMinPlayers(MIN_PLAYERS);
        return game;
    }

    private static List<Card> generateCards() {
        final List<Card> cards = new ArrayList<Card>(60);

        for (int i = 1; i <= 13; i++) {
            cards.add(generateCard(CardColor.RED, i));
            cards.add(generateCard(CardColor.BLUE, i));
            cards.add(generateCard(CardColor.YELLOW, i));
            cards.add(generateCard(CardColor.GREEN, i));
        }

        
        for (int i = 0; i < 4; i++) {
            cards.add(generateCard(CardColor.WHITE, VALUE_FOOL));
            cards.add(generateCard(CardColor.WHITE, VALUE_WIZARD));
        }

        return cards;
    }

    private static Card generateCard(final CardColor color, final int value) {
        final Card c = new Card();
        c.setCardColor(color);
        c.setValue(value);
        return c;
    }

}