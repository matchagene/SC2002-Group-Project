package domain.battle;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import domain.combatant.Combatant;

public class SpeedTurnOrderStrategy implements TurnOrderStrategy {
    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> combatants) {
        return combatants.stream()
                .filter(Combatant::isAlive)
                .sorted(Comparator.comparingInt((Combatant c) -> c.getStats().getSpeed()).reversed())
                .collect(Collectors.toList());
    }
}
