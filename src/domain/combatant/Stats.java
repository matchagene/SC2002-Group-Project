package domain.combatant;

public class Stats {
    private final int maxHp;
    private int currentHp;
    private int attack;
    private int defense;
    private int speed;

    public Stats(int maxHp, int attack, int defense, int speed) {
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public void takeDamage(int amount) {
        currentHp = Math.max(0, currentHp - amount);
    }

    public void increaseAttack(int amount) {
        attack += amount;
    }

    public void increaseDefense(int amount) {
        defense += amount;
    }

    public boolean isAlive() {
        return currentHp > 0;
    }
}