package de.gameplace.games.model;

import java.util.List;

import lombok.Data;

@Data
public class Game {

    List<Player> players;
    List<Card> cards;
    int numberOfRounds;
    int currentRound;
    Gamestate gamestate;

}