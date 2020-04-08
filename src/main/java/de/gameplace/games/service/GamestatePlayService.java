package de.gameplace.games.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Player;

@Service
public class GamestatePlayService {

    @Autowired
    Game game;

    @Autowired
    GameService gameService;

    @Autowired
    GamePlayService gamePlayService;

    public void playCard(Player player, String cardString) throws GameException {
        gameService.checkPlayerIsAllowedToPlay(player);
        
        
    }


}