package domain.combatant;

public abstract class Enemy extends Combatant {
    protected Enemy(String name, Stats stats) {
        super(name, stats);
    }
}
