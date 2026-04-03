package domain.battle;

import java.util.List;

import domain.combatant.Combatant;

public interface TurnOrderStrategy {
    List<Combatant> determineTurnOrder(List<Combatant> combatants);
}
