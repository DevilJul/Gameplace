package de.gameplace.games.tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.gameplace.games.exceptions.IllegalCardValueException;
import de.gameplace.games.model.Card;
import de.gameplace.games.model.Card.CardColor;

public class CardHelper {

    Card card;

    public CardHelper(Card card) {
        this.card = card;
    }

    public static CardHelper cardHelper(Card card) {
        CardHelper h = new CardHelper(card);
        return h;
    }

    public static boolean isWizard(Card c) {
        return c.getCardColor() == CardColor.WHITE && c.getValue() == 14;   // TODO auslagern!!
    }

    public static boolean isFool(Card c) {
        return c.getCardColor() == CardColor.WHITE && c.getValue() == 0;
    }

    public static Card generateCard(String cardValueString, String cardColorString) throws IllegalCardValueException {
        Card card = new Card();
        
        if (cardColorString.equalsIgnoreCase(CardColor.BLUE.toString())) {
            card.setCardColor(CardColor.BLUE);
        } else if (cardColorString.equalsIgnoreCase(CardColor.RED.toString())) {
            card.setCardColor(CardColor.RED);
        } else if (cardColorString.equalsIgnoreCase(CardColor.GREEN.toString())) {
            card.setCardColor(CardColor.GREEN);
        } else if (cardColorString.equalsIgnoreCase(CardColor.YELLOW.toString())) {
            card.setCardColor(CardColor.YELLOW);
        } else if (cardColorString.equalsIgnoreCase(CardColor.WHITE.toString())) {
            card.setCardColor(CardColor.WHITE);
        } else  {
            throw new IllegalCardValueException();
        }

        try {
            int cardValue = Integer.parseInt(cardValueString);
            if (cardValue < 0 ||cardValue > 14) {
                throw new IllegalCardValueException();
            }
            card.setValue(cardValue);
        } catch (NumberFormatException e) {
            throw new IllegalCardValueException();
        }

        return card;
    }

    public static Card generateCard(int value, CardColor color) {
        Card c = new Card();
        c.setValue(value);
        c.setCardColor(color);
        return c;
    }

    public static Card getWizardCard() {
        return generateCard(14, CardColor.WHITE);   // TODO
    }

    public static Card getFoolCard() {
        return generateCard(0, CardColor.WHITE);   // TODO
    }

    public static CardColor selectRandomCardColor(CardColor...excludedCardColors) {
        Random rand = new Random();
        List<CardColor> excluded = Collections.emptyList();
        if (excludedCardColors != null && excludedCardColors.length > 0) {
            excluded = Arrays.asList(excludedCardColors);
        }
        CardColor randomColor = CardColor.values()[rand.nextInt(CardColor.values().length - 1)];
        while (excluded.contains(randomColor)) {
            randomColor = CardColor.values()[rand.nextInt(CardColor.values().length - 1)];
        }
        return randomColor;
    }

}