package domain.item;

import domain.battle.BattleContext;
import domain.combatant.Combatant;

public interface Item {
    String getName();
    String getDescription();
    String use(Combatant user, BattleContext context);

}
