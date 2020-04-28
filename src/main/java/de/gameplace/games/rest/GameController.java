package de.gameplace.games.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.exceptions.IllegalPlayActionException;
import de.gameplace.games.model.Game;
import de.gameplace.games.model.GameAction;
import de.gameplace.games.model.Wizard.ActionOkay;
import de.gameplace.games.model.Wizard.ActionPlaceBet;
import de.gameplace.games.model.Wizard.ActionPlayCard;
import de.gameplace.games.service.GameManagerService;
import de.gameplace.games.tools.CardHelper;

@RestController
public class GameController {

    @Autowired
    private GameManagerService gameManagerService;

    @GetMapping("/initGame")
    public Game initGame(@RequestParam(value="gameId", defaultValue="") String gameId,
                         @RequestParam(value="gameName", defaultValue = "") String gameName,
                         @RequestParam(value="player", defaultValue = "") String playerId)
                         throws GameException {
        return gameManagerService.initNewGame(gameId, gameName, playerId);
    }

    @GetMapping("/initWizardGame")
    public Game initWizardGame(@RequestParam(value="gameId", defaultValue="") String gameId,
                         @RequestParam(value="player", defaultValue = "") String playerId)
                         throws GameException {
        return gameManagerService.initNewGame(gameId, "wizard", playerId); // TODO
    }

    @GetMapping("/joinGame")
    public void playerJoin(@RequestParam(value="gameId", defaultValue="") String gameId,
                                @RequestParam(value="player", defaultValue="") String player) throws GameException {
        gameManagerService.joinGame(gameId, player);
    }

    @GetMapping("/startGame")
    public void startGame(@RequestParam(value="gameId", defaultValue="") String gameId,
                          @RequestParam(value="player", defaultValue="") String player) throws GameException {
        gameManagerService.startGame(gameId, player);
    }

    @GetMapping("/playAction")
    public void playAction(@RequestParam(value="gameId", defaultValue="") String gameId,
                          @RequestParam(value="player", defaultValue="") String player,
                          @RequestParam(value="action", defaultValue="") String actionString) throws GameException {
        gameManagerService.playAction(gameId, player, convertAction(actionString));
    }

    private GameAction convertAction(String actionString) throws GameException {
        String[] vals = actionString.split("|");
        if (vals[0].equals("playCard")) {
            ActionPlayCard action = new ActionPlayCard();
            action.setCard(CardHelper.generateCard(vals[1], vals[2]));
            return action;
        } else if (vals[0].equals("setBet")) {
            try {
                int bet = Integer.parseInt(vals[1]);
                if (bet < 0) {
                    throw new IllegalPlayActionException();
                }
                ActionPlaceBet action = new ActionPlaceBet();
                action.setTricks(bet);
                return action;
            } catch (NumberFormatException e) {
                throw new IllegalPlayActionException();
            }
        } else if (vals[0].equals("okay")) {
            return new ActionOkay();
        } else {
            throw new IllegalPlayActionException();
        }
    }

}