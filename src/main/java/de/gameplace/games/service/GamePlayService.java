package de.gameplace.games.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.exceptions.IllegalGamestateException;
import de.gameplace.games.model.Card;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.GameRound;
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

    @Autowired
    GamestatePlayService gamestatePlayService;

    @Autowired
    GameService gameService;

    public void playCard(String playerName, String cardString) throws GameException {
        Player player = resolvePlayerAndCheckGameState(GamestateEnum.PLAY, playerName);
        gamestatePlayService.playCard(player, cardString);
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

    public void startGame() {
        game.setCurrentStartPlayerIndex(0);
        game.setCurrentRound(1);

        startRound();
    }

    public void startRound() {
        GameRound gameRound = new GameRound();
        gameRound.setStartPlayerIndex(game.getCurrentStartPlayerIndex());
        gameRound.setCurrentActionPlayerIndex(game.getCurrentStartPlayerIndex());
        game.setGameRound(gameRound);

        proceedToGamestateBet();
    }

    public void endRound() {
        game.setCurrentRound(game.getCurrentRound() + 1);
        gameService.incrementCurrentStartPlayer();
        // TODO more...
    }

    public void proceedToGamestatePlay() {
        game.setGamestate(GamestateEnum.PLAY);
        game.getGameRound().setCurrentActionPlayerIndex(game.getCurrentStartPlayerIndex());
    }

    public void proceedToGamestateBet() {
        game.setGamestate(GamestateEnum.BET);
        game.getPlayers().stream().forEach(p -> {
            p.setCurrentOkayState(false);
            p.getBets().add(0);
        });
        dealCards();
    }

    public void proceedToGamestateEnd() {
        
    }

    private void dealCards() {
        int numberOfCards = game.getCurrentRound() * game.getPlayers().size();
        List<Integer> allCardIndices = IntStream.range(0, game.getCards().size()).boxed().collect(Collectors.toList());
        List<Integer> dealedCardsIndices = new ArrayList<Integer>(numberOfCards);

        Random rand = new Random();
        for (int i = 0; i < numberOfCards; i++) {
            dealedCardsIndices.add(allCardIndices.remove(rand.nextInt(allCardIndices.size())));
        }

        game.getPlayers().forEach(p -> p.setCurrentCards(new ArrayList<Card>(game.getCurrentRound())));

        for (int iterCards = 0; iterCards < dealedCardsIndices.size(); iterCards++) {
            game.getPlayers().get(iterCards % game.getPlayers().size()).getCurrentCards().add(game.getCards().get(dealedCardsIndices.get(iterCards)));
        }

        game.getGameRound().setTrumpCard(game.getCards().get(allCardIndices.remove(rand.nextInt(allCardIndices.size()))));
    }

}