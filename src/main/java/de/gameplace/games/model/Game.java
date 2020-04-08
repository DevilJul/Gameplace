package de.gameplace.games.model;

import java.util.List;

import lombok.Data;

@Data
public class Game {

    List<Player> players;
    List<Card> cards;
    int numberOfRounds;
    int currentRound;
    GamestateEnum gamestate;
    int currentStartPlayerIndex;
    //int currentActionPlayerIndex;
    GameRound gameRound;

    public enum GamestateEnum {INIT, BET, PLAY, WAIT, END};

}