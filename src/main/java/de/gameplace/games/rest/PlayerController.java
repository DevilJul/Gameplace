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
import de.gameplace.games.service.GameService;


@RestController
public class PlayerController {

    @Autowired
    private GameService gameService;

    @GetMapping("/player")
    public PlayerOut getPlayer(@RequestParam(value="name", defaultValue="") String name) throws GameException {
        return gameService.getPlayerOut(name);
    }

    @GetMapping("/players")
    public List<PlayerOut> getPlayers() throws GameException {
        return gameService.getPlayersOut();
    }

    @GetMapping("/addPlayer")
    public PlayerOut addPlayer(@RequestParam(value="name", defaultValue="") String name) throws GameException {
        return gameService.addPlayer(name);
    }

    @PostMapping("addPlayer")
    public PlayerOut addPlayer(@RequestBody PlayerOut playerFromUser) throws GameException {       
        return gameService.addPlayer(playerFromUser);
    }
    
}