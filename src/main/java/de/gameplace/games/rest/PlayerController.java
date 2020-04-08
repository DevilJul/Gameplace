package de.gameplace.games.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.model.PlayerOut;
import de.gameplace.games.service.GamestateInitService;
import de.gameplace.games.service.PlayerService;


@RestController
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GamestateInitService gameInitService;

    @GetMapping("/player")
    public PlayerOut getPlayer(@RequestParam(value="name", defaultValue="") String name) throws GameException {
        return playerService.getPlayerOut(name);
    }

    @GetMapping("/players")
    public List<PlayerOut> getPlayers() throws GameException {
        return playerService.getPlayersOut();
    }

    @GetMapping("/addPlayer")
    public PlayerOut addPlayer(@RequestParam(value="name", defaultValue="") String name) throws GameException {
        return gameInitService.addPlayer(name);
    }

    @PostMapping("addPlayer")
    public PlayerOut addPlayer(@RequestBody PlayerOut playerFromUser) throws GameException {       
        return playerService.addPlayer(playerFromUser);
    }
    
}