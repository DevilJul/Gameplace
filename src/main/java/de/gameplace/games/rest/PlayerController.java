package de.gameplace.games.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gameplace.games.exceptions.PlayerNameException;
import de.gameplace.games.model.PlayerOut;
import de.gameplace.games.service.GameService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class PlayerController {

    @Autowired
    private GameService gameService;

    @GetMapping("/player")
    public PlayerOut getPlayer(@RequestParam(value="name") String name) throws PlayerNameException {
        return gameService.getPlayerOut(name);
    }

    @PostMapping("addPlayer")
    public PlayerOut addPlayer(@RequestBody PlayerOut playerFromUser) throws PlayerNameException {       
        return gameService.addPlayer(playerFromUser);
    }
    
}