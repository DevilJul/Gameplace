package de.gameplace.games.model;

import java.util.List;

import lombok.Data;

@Data
public class Player {
    
    String id;
    String name;
    List<Integer> bets;
    List<Integer> tricks;
    List<Integer> points;
    int pointsTotal;
    List<Card> currentCards;
    
}