package de.gameplace.games.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.gameplace.games.model.Game;
import de.gameplace.games.model.GameRound;
import de.gameplace.games.model.Player;

@RestController
public class GamestateController {

    @Autowired
    Game game;

    @GetMapping("/game")
    public Game getGame() {
        return game;
    }

    @GetMapping("/gameround")
    public GameRound getGameRound() {
        return game.getGameRound();
    }

    @GetMapping("/playersintern")
    public List<Player> getPlayersIntern() {
        return game.getPlayers();
    }

}