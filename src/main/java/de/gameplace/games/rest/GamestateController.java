package de.gameplace.games.rest;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.gameplace.games.exceptions.GameException;
import de.gameplace.games.service.GameManager;
import de.gameplace.games.service.GameManagerService;

@RestController
public class GamestateController {

    @Autowired
    GameManagerService gameManagerService;

    @GetMapping("/all")
    public GameManagerService getAll() {
        return gameManagerService;
    }

    @GetMapping("/getGame")
    public GameManager getGame(@RequestParam(value = "gameId", defaultValue = "") String gameId) throws GameException {
        return gameManagerService.getGameManager(gameId);
    }

    @GetMapping("/getGameManagers")
    public Collection<GameManager> getGameManagers() throws GameException {
        return gameManagerService.getGameManagers();
    }

}