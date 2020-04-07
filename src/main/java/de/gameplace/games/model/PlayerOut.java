package de.gameplace.games.model;

import java.util.List;

import lombok.Data;

@Data
public class PlayerOut {

    String name;
    List<Card> cards;
    int points;
    int bets;

}