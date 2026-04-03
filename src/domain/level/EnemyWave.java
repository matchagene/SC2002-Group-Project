package domain.level;

import java.util.ArrayList;
import java.util.List;

import domain.combatant.Enemy;

public class EnemyWave {
    private final List<Enemy> enemies;

    public EnemyWave(List<Enemy> enemies) {
        this.enemies = new ArrayList<>(enemies);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}
