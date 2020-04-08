package de.gameplace.games.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.service.GamePlayService;

@RestController
public class PlayController {

    @Autowired
    private GamePlayService gamePlayService;

    @GetMapping("/playCard")
    public void playCard(@RequestParam(value="player", defaultValue = "") String playerName,
                         @RequestParam(value="card", defaultValue = "") String cardString)
                         throws GameException {
        gamePlayService.playCard(playerName, cardString);
    }

    @GetMapping("/playBet")
    public void playBet(@RequestParam(value="player", defaultValue = "") String playerName,
                         @RequestParam(value="bet", defaultValue = "") String bet)
                         throws GameException {
        gamePlayService.playBet(playerName, bet);
    }

    @GetMapping("/playOkay")
    public void playOkay(@RequestParam(value="player", defaultValue = "") String playerName)
                         throws GameException {
        gamePlayService.playOkay(playerName);
    }

}