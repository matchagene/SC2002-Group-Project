package domain.battle;

import java.util.ArrayList;
import java.util.List;

import domain.combatant.Enemy;
import domain.combatant.Player;
import domain.level.Level;

public class BattleContext {
    private final Player player;
    private final Level level;
    private final List<Enemy> activeEnemies;
    private final TurnOrderStrategy turnOrderStrategy;

    public BattleContext(Player player, Level level, List<Enemy> activeEnemies, TurnOrderStrategy turnOrderStrategy) {
        this.player = player;
        this.level = level;
        this.activeEnemies = new ArrayList<>(activeEnemies);
        this.turnOrderStrategy = turnOrderStrategy;
    }

    public Player getPlayer() {
        return player;
    }

    public Level getLevel() {
        return level;
    }

    public List<Enemy> getActiveEnemies() {
        return activeEnemies;
    }

    public TurnOrderStrategy getTurnOrderStrategy() {
        return turnOrderStrategy;
    }
}
