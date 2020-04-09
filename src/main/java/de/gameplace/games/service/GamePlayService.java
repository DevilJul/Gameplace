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
import de.gameplace.games.tools.CardHelper;
import de.gameplace.games.tools.ListHelper;
import de.gameplace.games.model.Player;
import de.gameplace.games.model.Card.CardColor;

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
    GamestateWaitService gamestateWaitService;

    @Autowired
    GamestatePlayService gamestatePlayService;

    @Autowired
    GameService gameService;

    public Player playCard(String playerName, String cardColorString, String cardValueString) throws GameException {
        Player player = resolvePlayerAndCheckGameState(GamestateEnum.PLAY, playerName);
        gamestatePlayService.playCard(player, cardColorString, cardValueString);

        if (isLastCard()) {
            endRound();
        }
        return player;
    }

    private boolean isLastCard() {
        return game.getPlayers().stream().map(p -> p.getCurrentCards()).allMatch(l -> l.isEmpty());
    }

    public Player playBet(String playerName, String betString) throws GameException {
        Player player = resolvePlayerAndCheckGameState(GamestateEnum.BET, playerName);
        gamestateBetService.playBet(player, betString);
        return player;
    }

    public Player playOkay(String playerName) throws GameException {
        Player player = playerService.getPlayerByName(playerName);
        switch (game.getGamestate()) {
            case INIT:
            gamestateInitService.setPlayerOkay(player);
                break;
            case WAIT:
            gamestateWaitService.setPlayerOkay(player);
            default:
                break;
        }
        return player;
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
        setNumberOfRounds();

        startRound();
    }

    private void setNumberOfRounds() {
        game.setNumberOfRounds(60 / game.getPlayers().size());
    }

    public void startRound() {
        GameRound gameRound = new GameRound();
        gameRound.setStartPlayerIndex(game.getCurrentStartPlayerIndex());
        gameRound.setCurrentActionPlayerIndex(game.getCurrentStartPlayerIndex());
        game.setGameRound(gameRound);

        proceedToGamestateBet();
    }

    public void endRound() {
        calculatePoints();
        game.setCurrentRound(game.getCurrentRound() + 1);
        gameService.incrementCurrentStartPlayer();
        proceedToGameStateWait();
    }

    private void calculatePoints() {

        for (Player player : game.getPlayers()) {
            int bet = ListHelper.getLastValue(player.getBets());
            int tricks = ListHelper.getLastValue(player.getTricks());
            int points = 0;
            if (bet == tricks) {
                points = 20 + tricks * 10;
            } else {
                points = 0 - (Math.abs(bet - tricks) * 10);
            }
            player.getPoints().add(points);
            player.setPointsTotal(player.getPointsTotal() + points);
        }

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
            p.getTricks().add(0);
        });
        dealCards();
    }

    public void proceedToGameStateWait() {
        game.setGamestate(GamestateEnum.WAIT);
    }

    public void proceedToGamestateEnd() {
        game.setGamestate(GamestateEnum.END);
    }

    private void dealCards() {
        int numberOfCards = game.getCurrentRound() * game.getPlayers().size();
        List<Integer> allCardIndices = IntStream.range(0, game.getCards().size()).boxed().collect(Collectors.toList());

        game.getPlayers().forEach(p -> p.setCurrentCards(new ArrayList<Card>(game.getCurrentRound())));

        Random rand = new Random();
        for (int iterCards = 0; iterCards < numberOfCards; iterCards++) {
            int cardIndex = allCardIndices.remove(rand.nextInt(allCardIndices.size()));
            game.getPlayers().get(iterCards % game.getPlayers().size()).getCurrentCards().add(game.getCards().get(cardIndex));
        }

        setTrumpCard(allCardIndices.get(rand.nextInt(allCardIndices.size())));
    }

    private void setTrumpCard(int cardIndex) {
        Card trumpCard = game.getCards().get(cardIndex);
        if (CardHelper.isWizard(trumpCard)) {
            trumpCard.setCardColor(selectRandomCardColor());
        }
    }

    private CardColor selectRandomCardColor() {
        Random rand = new Random();
        return CardColor.values()[rand.nextInt(CardColor.values().length - 1)];
    }

}