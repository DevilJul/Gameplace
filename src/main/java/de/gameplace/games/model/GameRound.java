package de.gameplace.games.model;

import java.util.List;

import lombok.Data;

@Data
public class GameRound {

    int startPlayerIndex;
    int currentActionPlayerIndex;
    Card trumpCard;
    List<Card> playedCards;

}