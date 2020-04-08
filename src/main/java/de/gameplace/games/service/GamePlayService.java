package de.gameplace.games.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.exceptions.IllegalGamestateException;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Game.GamestateEnum;
import de.gameplace.games.model.Player;

@Service
public class GamePlayService {

    @Autowired
    Game game;

    @Autowired
    PlayerService playerService;

    @Autowired
    GamestateInitService gamestateInitService;

    @Autowired
    GamestateBetService gamestateBetService;

    public void playCard(String playerName, String cardString) throws GameException {
        Player player = resolvePlayerAndCheckGameState(GamestateEnum.PLAY, playerName);
    }

    public void playBet(String playerName, String betString) throws GameException {
        Player player = resolvePlayerAndCheckGameState(GamestateEnum.BET, playerName);
        gamestateBetService.playBet(player, betString);
    }

    public void playOkay(String playerName) throws GameException {
        Player player = playerService.getPlayerByName(playerName);
        switch (game.getGamestate()) {
            case INIT:
            gamestateInitService.setPlayerOkay(player);
                break;
            case BET:
            default:
                break;
        }
    }

    private Player resolvePlayerAndCheckGameState(GamestateEnum expectedGamestate, String playerName) throws GameException {
        if (game.getGamestate() != expectedGamestate) {
            throw new IllegalGamestateException();
        }

        return playerService.getPlayerByName(playerName);
    }

    public void proceedToGamestatePlay() {
        game.setGamestate(GamestateEnum.PLAY);
        game.setCurrentActionPlayerIndex(game.getCurrentStartPlayerIndex());

    }

    public void initGamestateBet() {
        game.setCurrentStartPlayerIndex(0);
        game.setCurrentActionPlayerIndex(0);

        proceedToGamestateBet();
    }

    public void proceedToGamestateBet() {
        game.setGamestate(GamestateEnum.BET);
        game.getPlayers().stream().forEach(p -> {
            p.setCurrentOkayState(false);
            p.getBets().add(0);
        });
    }

    public void proceedToGamestateEnd() {
        
    }

}