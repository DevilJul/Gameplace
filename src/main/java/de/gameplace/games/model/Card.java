package de.gameplace.games.model;

import lombok.Data;

@Data
public class Card {

    CardColor cardColor;
    int value; // 1 - 13: color card values, 0: narr, 14: wizard

    public enum CardColor {RED, BLUE, GREEN, YELLOW, WHITE};

}