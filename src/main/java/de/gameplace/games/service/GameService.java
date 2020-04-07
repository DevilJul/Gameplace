package de.gameplace.games.service;

import org.springframework.stereotype.Service;

import de.gameplace.games.exceptions.PlayerNameException;
import de.gameplace.games.model.PlayerOut;

@Service
public class GameService {

    public PlayerOut getPlayerOut(String name) throws PlayerNameException {
        return null; // TODO
    }

    public PlayerOut addPlayer(PlayerOut playerFromUser) throws PlayerNameException {
        // TODO create player if allowed
        return getPlayerOut(playerFromUser.getName());
    }

}