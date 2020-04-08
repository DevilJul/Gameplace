package de.gameplace.games.model;

import lombok.Data;

@Data
public class GameRound {

    int startPlayerIndex;
    int currentActionPlayerIndex;
    Card trumpCard;
   // int roundNumber;

}