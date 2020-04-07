package de.gameplace.games.model;

import lombok.Data;

@Data
public class Card {

    Color color;
    int value; // 1 - 13: color card values, 0: narr, 14: wizard

    public enum Color {Red, Blue, Green, Yello, White};

}