package de.gameplace.games.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.model.Card;
import de.gameplace.games.model.Card.CardColor;
import de.gameplace.games.model.Game.GamestateEnum;
import de.gameplace.games.model.GameAction;
import de.gameplace.games.model.GameRound;
import de.gameplace.games.model.Player;
import de.gameplace.games.model.Wizard.ActionOkay;
import de.gameplace.games.model.Wizard.ActionPlaceBet;
import de.gameplace.games.model.Wizard.ActionPlayCard;
import de.gameplace.games.tools.CardHelper;
import de.gameplace.games.tools.GameHelper;
import de.gameplace.games.tools.ListHelper;
import de.gameplace.games.tools.WizardGameHelper;

public class GameManagerWizard extends GameManager {

    public GameManagerWizard(String gameId, Player manager) {
        init(WizardGameHelper.generateWizardGame(gameId, manager));
    }

    @Override
    public void playActionImpl(Player player, GameAction action) throws GameException {
        if (action instanceof ActionPlayCard) {
            checkCorrectGamestate(GamestateEnum.PLAY);
            playCard(player, (ActionPlayCard)action);

        } else if (action instanceof ActionPlaceBet) {
            checkCorrectGamestate(GamestateEnum.BET);
            placeBet(player, (ActionPlaceBet)action);
        
        } else if (action instanceof ActionOkay) {
            checkCorrectGamestate(GamestateEnum.WAIT);
            playOkay(player);
        } else {
            throw new GameException("Illegal game action");
        }
    }

    private void playCard(Player player, ActionPlayCard action) throws GameException {
        GamestatePlayManager.build()
                            .game(game)
                            .player(player)
                            .playCard(action);

        incrementCurrentActionPlayer();

        if (isLastCard()) {
            endRound();
        }
    }

    private void placeBet(Player player, ActionPlaceBet action) throws GameException {

        GamestateBetManager.build()
                           .game(game)
                           .player(player)
                           .placeBet(action);

        if (GameHelper.isCurrentActionPlayerLast(game)) {
            proceedToGamestatePlay();
        } else {
            incrementCurrentActionPlayer();
        }
    }

    private void playOkay(Player player) {
        player.setCurrentOkayState(true);

        if (allPlayersOkay()) {

            if (isGameFinished()) {
                proceedToGamestateEnd();
            } else {
                proceedToGamestateBet();
            }
        }
    }

    private boolean isGameFinished() {
        return game.getCurrentRound() == game.getNumberOfRounds();
    }

    @Override
    protected void startGameImpl() throws GameException {
        game.setCurrentStartPlayerIndex(0);
        game.setCurrentRound(1);
        setNumberOfRounds();

        startRound();
    }

    private void setNumberOfRounds() {
        game.setNumberOfRounds(60 / game.getPlayers().size());  // TODO
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
        incrementCurrentStartPlayer();
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
        // TODO game entfernen aus service
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
            trumpCard.setCardColor(CardHelper.selectRandomCardColor(CardColor.WHITE));
        }
        game.getGameRound().setTrumpCard(trumpCard);
    }

    private boolean isLastCard() {
        return game.getPlayers().stream().map(p -> p.getCurrentCards()).allMatch(l -> l.isEmpty());
    }

}