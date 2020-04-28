package de.gameplace.games.model.Wizard;

import de.gameplace.games.model.GameAction;
import de.gameplace.games.model.Card;
import lombok.Data;

@Data
public class ActionPlayCard implements GameAction {
    Card card;
}