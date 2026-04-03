package domain.effect;

public abstract class StatusEffect {
    private final String name;
    private int remainingTurns;

    protected StatusEffect(String name, int remainingTurns) {
        this.name = name;
        this.remainingTurns = remainingTurns;
    }

    public String getName() {
        return name;
    }

    public int getRemainingTurns() {
        return remainingTurns;
    }

    public void decrementDuration() {
        if (remainingTurns > 0) {
            remainingTurns--;
        }
    }

    public boolean isExpired() {
        return remainingTurns <= 0;
    }
}
