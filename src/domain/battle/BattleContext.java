package domain.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import domain.combatant.Combatant;
import domain.combatant.Enemy;
import domain.combatant.Player;
import domain.level.Level;
import ui.User;


public class BattleContext {
    private final Player player;
    private final Level level;
    private final List<Combatant> active;
    private final TurnOrderStrategy turnOrderStrategy;

    public BattleContext(Player player, Level level, List<Combatant> active, TurnOrderStrategy turnOrderStrategy) {
        this.player = player;
        this.level = level;
        this.active = new ArrayList<>(active);
        this.turnOrderStrategy = turnOrderStrategy;
    }

    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public List<Combatant> getActive() {
        return active;
    }

    public TurnOrderStrategy getTurnOrderStrategy() {
        return turnOrderStrategy;
    }

    public List<Combatant> getLivingEnemies() {
        return active.stream()
                .filter(c -> c instanceof Enemy && c.isAlive())
                .collect(Collectors.toList());
    }

    public Combatant selectTarget(List<Combatant> candidates) {
        if (candidates.isEmpty()) {
            return null; 
        }
        
        if (candidates.size() == 1) {
            return candidates.get(0);
        }
        
        return User.getInstance().selectTarget(candidates);
    }

}

