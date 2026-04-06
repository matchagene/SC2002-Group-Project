package domain.action;

import domain.battle.BattleContext;
import domain.combatant.Combatant;

public interface Action {
    String getName();

    String execute(Combatant actor, BattleContext context);

}
