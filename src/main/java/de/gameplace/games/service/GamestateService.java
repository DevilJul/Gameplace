package de.gameplace.games.service;

import org.springframework.stereotype.Service;

import de.gameplace.games.model.Player;


@Service
public abstract class GamestateService {

    protected abstract void proceedToNextState();

    public abstract void setPlayerOkay(Player player);
}