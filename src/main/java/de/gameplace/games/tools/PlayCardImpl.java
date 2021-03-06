package de.gameplace.games.tools;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.exceptions.IllegalCardValueException;
import de.gameplace.games.model.Card;
import de.gameplace.games.model.Card.CardColor;
import lombok.extern.slf4j.Slf4j;
import de.gameplace.games.model.GameRound;
import de.gameplace.games.model.Player;

@Slf4j
public class PlayCardImpl {

    private Player player;
    private Card card;
    private GameRound gameRound;

    public PlayCardImpl(Player player) {
        this.player = player;
    }

    public void setGameRound(GameRound gamerRound) {
        this.gameRound = gamerRound;
    }

    public void playCard(String cardColorString, String cardValueString) throws GameException {
        playCard(CardHelper.generateCard(cardColorString, cardValueString));
    }

    public void playCard(Card card) throws GameException {
        this.card =card;
        
        checkCardIsOfPlayer();
        checkPlayerCanPlayCard();

        removeCard();
    }

    private void removeCard() {
        gameRound.getPlayedCards().add(card);
        player.getCurrentCards().remove(card);
    }

    private void checkPlayerCanPlayCard() throws IllegalCardValueException {

        if (!isWhiteCard() &&
            !isFirstCard() &&
            !isFirstColorWhite() &&
            !isSameColorAsFirst() &&
            hasPlayerCardInFirstColor()) {
                throw new IllegalCardValueException();
        }

    }

    private boolean isWhiteCard() {
        boolean isWhiteCard = card.getCardColor() == CardColor.WHITE;
        log.info("isWhiteCard={}", isWhiteCard);
        return isWhiteCard;
    }

    private boolean isFirstCard() {
        boolean isFirstCard = gameRound.getPlayedCards().size() == 0;
        log.info("isFirstCard={}", isFirstCard);
        return isFirstCard;
    }

    private boolean isSameColorAsFirst() {
        boolean isSameColorAsFirst = gameRound.getPlayedCards().get(0).getCardColor() == card.getCardColor();
        log.info("isSameColorAsFirst={}", isSameColorAsFirst);
        return isSameColorAsFirst;
    }

    private boolean hasPlayerCardInFirstColor() {
        boolean hasPlayerCardInFirstColor = player.getCurrentCards().stream().anyMatch(c -> gameRound.getPlayedCards().get(0).getCardColor() == c.getCardColor());
        log.info("hasPlayerCardInFirstColor={}", hasPlayerCardInFirstColor);
        return hasPlayerCardInFirstColor;
    }

    private boolean isFirstColorWhite() {
        boolean isFirstColorWhite = gameRound.getPlayedCards().get(0).getCardColor() == CardColor.WHITE;
        log.info("isFirstColorWhite={}", isFirstColorWhite);
        return isFirstColorWhite;
    }

    private void checkCardIsOfPlayer() throws IllegalCardValueException {
        if (!player.getCurrentCards().stream().anyMatch(c -> c.equals(card))) {
            throw new IllegalCardValueException();
        }
    }

}