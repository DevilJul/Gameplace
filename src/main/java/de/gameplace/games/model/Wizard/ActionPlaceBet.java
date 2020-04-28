package de.gameplace.games.model.Wizard;

import de.gameplace.games.model.GameAction;
import lombok.Data;

@Data
public class ActionPlaceBet implements GameAction {

    int tricks;

}