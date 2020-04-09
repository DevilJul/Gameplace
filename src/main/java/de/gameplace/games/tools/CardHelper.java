package de.gameplace.games.tools;

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

    public static Card getWizardCard() {
        Card c = new Card();
        c.setCardColor(CardColor.WHITE);
        c.setValue(14);
        return c;
    }

    public static Card getFoolCard() {
        Card c = new Card();
        c.setCardColor(CardColor.WHITE);
        c.setValue(0);
        return c;
    }

}