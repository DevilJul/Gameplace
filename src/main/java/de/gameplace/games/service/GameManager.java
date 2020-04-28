package de.gameplace.games.service;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.exceptions.IllegalGamestateException;
import de.gameplace.games.exceptions.IllegalPlayActionException;
import de.gameplace.games.model.GameAction;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.Game.GamestateEnum;
import de.gameplace.games.tools.GameHelper;
import de.gameplace.games.model.Player;

public abstract class GameManager {

    Game game;
    PlayerManager playerManager;


    protected void init(Game game) { 
        this.game = game;
        playerManager = new PlayerManager(this.game);
    }

    public Game getGame() {
        return game;
    }

    public void addPlayer(String playerId) throws GameException {
        addPlayer(playerId, playerId);
    }

    public void addPlayer(String playerId, String playerName) throws GameException {

        checkCorrectGamestate(GamestateEnum.INIT);
        playerManager.addPlayer(playerId, playerName);
    }

    public void startGame(String playerId) throws GameException {
        checkCorrectGamestate(GamestateEnum.INIT);

        playerManager.checkIfPlayerIsManager(playerId);
        playerManager.checkCorrectNumberOfPlayersToStart();

        startGameImpl();
    }

    protected abstract void startGameImpl() throws GameException;

    public void playAction(String playerId, GameAction action) throws GameException {
        Player player = playerManager.getPlayerById(playerId);
        checkIsPlayersTurn(player);
        
        playActionImpl(player, action);
    }

    public abstract void playActionImpl(Player player, GameAction action) throws GameException;

    protected void checkCorrectGamestate(GamestateEnum expectedGamestate) throws IllegalGamestateException {
        if (game.getGamestate() != expectedGamestate) {
            throw new IllegalGamestateException();
        }
    }

    protected boolean allPlayersOkay() {
        return game.getPlayers().stream().allMatch(p -> p.isCurrentOkayState());
    }

    protected void incrementCurrentActionPlayer() {
        game.getGameRound().setCurrentActionPlayerIndex(GameHelper.calcIncrementPlayerIndex(game, game.getGameRound().getCurrentActionPlayerIndex()));
    }

    protected void incrementCurrentStartPlayer() {
        game.setCurrentStartPlayerIndex(GameHelper.calcIncrementPlayerIndex(game, game.getCurrentStartPlayerIndex()));
    }

    protected void checkIsPlayersTurn(Player player) throws IllegalPlayActionException{
        if (!game.getPlayers().get(game.getGameRound().getCurrentActionPlayerIndex()).equals(player)) {
            throw new IllegalPlayActionException();
        }
    }
}