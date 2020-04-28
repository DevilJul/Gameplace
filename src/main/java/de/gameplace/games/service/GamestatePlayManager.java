package de.gameplace.games.service;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.model.Card;
import de.gameplace.games.model.Card.CardColor;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.GameRound;
import de.gameplace.games.model.Player;
import de.gameplace.games.model.Wizard.ActionPlayCard;
import de.gameplace.games.tools.CardHelper;
import de.gameplace.games.tools.PlayCardImpl;

public class GamestatePlayManager {

    Game game;
    GameRound gameRound;
    Player player;
    Card card;

    public static GamestatePlayManager build() {
        return new GamestatePlayManager();
    }

    public GamestatePlayManager game(Game game) {
        this.game = game;
        this.gameRound = game.getGameRound();
        return this;
    }

    public GamestatePlayManager player(Player player) {
        this.player = player;
        return this;
    }

    public void playCard(ActionPlayCard action) throws GameException {
        
        startTrickRoundIfFirstCard();

        PlayCardImpl playCard = new PlayCardImpl(player);
        playCard.setGameRound(gameRound);
        playCard.playCard(action.getCard());

        endTrickRoundIfLastCard();
    }

    private void startTrickRoundIfFirstCard() {
        if (gameRound.getPlayedCards() == null) {
            gameRound.setPlayedCards(new ArrayList<Card>());
        }
    }

    private void endTrickRoundIfLastCard() {
        if (gameRound.getPlayedCards().size() == game.getPlayers().size()) {
            resolveTrick();
            gameRound.setPlayedCards(new ArrayList<>());
        }
    }

    private void resolveTrick() {
        int trickCardIndex = resolveHighestCard();
        int trickPlayerIndex = getPlayerIndexByPlayedCardIndex(trickCardIndex);

        incrementTricks(game.getPlayers().get(trickPlayerIndex));
        gameRound.setCurrentActionPlayerIndex(trickPlayerIndex);
    }

    private void incrementTricks(Player player) {
        player.getTricks().set(player.getBets().size() - 1, player.getTricks().get(player.getTricks().size() - 1) + 1);
    }

    private int getPlayerIndexByPlayedCardIndex(int trickCardIndex) {
        return (trickCardIndex + gameRound.getCurrentActionPlayerIndex()) % game.getPlayers().size();
    }

    private int resolveHighestCard() {
        List<Card> playedCards = gameRound.getPlayedCards();

        int wizardIndex = playedCards.indexOf(CardHelper.getWizardCard());

        if (wizardIndex >= 0) {
            return wizardIndex;
        }

        if (isAllCardsFools()) {
            return 0;
        }

        if (!isNoTrump()) {
            OptionalInt highestTrumpColorIndex = getHighestCardByColorIndex(gameRound.getTrumpCard().getCardColor());
            if (highestTrumpColorIndex.isPresent()) {
                return highestTrumpColorIndex.getAsInt();
            }
        }

        return getHighestCardByColorIndex(gameRound.getPlayedCards().get(0).getCardColor()).getAsInt();
    }

    private boolean isNoTrump() {
        return gameRound.getTrumpCard().equals(CardHelper.getFoolCard()) ||
               game.getCurrentRound() == game.getNumberOfRounds();
        
    }

    private boolean isAllCardsFools() {
        return gameRound.getPlayedCards().stream().allMatch(c -> c.getValue() == 0);
    }

    private OptionalInt getHighestCardByColorIndex(CardColor color) {
       OptionalInt maxColorValue = gameRound.getPlayedCards().stream()
                                              .filter(c -> c.getCardColor() == color)
                                              .mapToInt(c -> c.getValue())
                                              .max();
        if (!maxColorValue.isPresent()) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(gameRound.getPlayedCards().indexOf(generateCard(color, maxColorValue.getAsInt())));
    }

    private Card generateCard(CardColor color, int value) {
        Card c = new Card();
        c.setCardColor(color);
        c.setValue(value);
        return c;
    }

}