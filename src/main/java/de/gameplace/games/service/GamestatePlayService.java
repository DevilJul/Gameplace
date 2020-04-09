package de.gameplace.games.service;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.model.Card;
import de.gameplace.games.model.Card.CardColor;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Player;
import de.gameplace.games.tools.CardHelper;
import de.gameplace.games.tools.PlayCardImpl;

@Service
public class GamestatePlayService {

    @Autowired
    Game game;

    @Autowired
    GameService gameService;

    @Autowired
    GamePlayService gamePlayService;

    public void playCard(Player player, String cardColorString, String cardValueString) throws GameException {
        gameService.checkIsPlayersTurn(player);
        
        startTrickRound();
        PlayCardImpl playCard = new PlayCardImpl(player);
        playCard.setGameRound(game.getGameRound());
        playCard.playCard(cardColorString, cardValueString);
        gameService.incrementCurrentActionPlayer();
        endTrickRound();
    }

    private void startTrickRound() {
        if (game.getGameRound().getPlayedCards() == null) {
            game.getGameRound().setPlayedCards(new ArrayList<Card>());
        }
    }

    private void endTrickRound() {
        if (game.getGameRound().getPlayedCards().size() == game.getPlayers().size()) {
            resolveTrick();
            game.getGameRound().setPlayedCards(new ArrayList<>());
        }
    }

    private void resolveTrick() {
        int trickCardIndex = resolveHighestCard();
        int trickPlayerIndex = getPlayerIndexByPlayedCardIndex(trickCardIndex);

        incrementTricks(game.getPlayers().get(trickPlayerIndex));
        game.getGameRound().setCurrentActionPlayerIndex(trickPlayerIndex);
    }

    private void incrementTricks(Player player) {
        player.getTricks().set(player.getBets().size() - 1, player.getTricks().get(player.getTricks().size() - 1) + 1);
    }

    private int getPlayerIndexByPlayedCardIndex(int trickCardIndex) {
        return (trickCardIndex + game.getGameRound().getCurrentActionPlayerIndex()) % game.getPlayers().size();
    }

    private int resolveHighestCard() {
        List<Card> playedCards = game.getGameRound().getPlayedCards();

        int wizardIndex = playedCards.indexOf(CardHelper.getWizardCard());

        if (wizardIndex >= 0) {
            return wizardIndex;
        }

        if (isAllCardsFools()) {
            return 0;
        }

        if (!isNoTrump()) {
            OptionalInt highestTrumpColorIndex = getHighestCardByColorIndex(game.getGameRound().getTrumpCard().getCardColor());
            if (highestTrumpColorIndex.isPresent()) {
                return highestTrumpColorIndex.getAsInt();
            }
        }

        return getHighestCardByColorIndex(game.getGameRound().getPlayedCards().get(0).getCardColor()).getAsInt();
    }

    private boolean isNoTrump() {
        return game.getGameRound().getTrumpCard().equals(CardHelper.getFoolCard()) ||
               game.getCurrentRound() == game.getNumberOfRounds();
        
    }

    private boolean isAllCardsFools() {
        return game.getGameRound().getPlayedCards().stream().allMatch(c -> c.getValue() == 0);
    }

    private OptionalInt getHighestCardByColorIndex(CardColor color) {
       OptionalInt maxColorValue = game.getGameRound().getPlayedCards().stream()
                                              .filter(c -> c.getCardColor() == color)
                                              .mapToInt(c -> c.getValue())
                                              .max();
        if (!maxColorValue.isPresent()) {
            return OptionalInt.empty();
        }

        return OptionalInt.of(game.getGameRound().getPlayedCards().indexOf(generateCard(color, maxColorValue.getAsInt())));
    }

    private Card generateCard(CardColor color, int value) {
        Card c = new Card();
        c.setCardColor(color);
        c.setValue(value);
        return c;
    }

    private void addTrickToPlayer(Player player) {

    }

    private void setNextPlayer(Player player) {

    }

    




}