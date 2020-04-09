package de.gameplace.games.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.model.Player;
import de.gameplace.games.service.GamePlayService;

@RestController
public class PlayController {

    @Autowired
    private GamePlayService gamePlayService;

    @GetMapping("/playCard")
    public Player playCard(@RequestParam(value="player", defaultValue = "") String playerName,
                         @RequestParam(value="cardColor", defaultValue = "") String cardColorString,
                         @RequestParam(value="cardValue", defaultValue = "") String cardValueString)
                         throws GameException {
        return gamePlayService.playCard(playerName, cardColorString, cardValueString);
    }

    @GetMapping("/playBet")
    public Player playBet(@RequestParam(value="player", defaultValue = "") String playerName,
                         @RequestParam(value="bet", defaultValue = "") String bet)
                         throws GameException {
        return gamePlayService.playBet(playerName, bet);
    }

    @GetMapping("/playOkay")
    public Player playOkay(@RequestParam(value="player", defaultValue = "") String playerName)
                         throws GameException {
        return gamePlayService.playOkay(playerName);
    }

}