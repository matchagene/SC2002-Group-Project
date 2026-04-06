package domain.combatant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import domain.effect.StatusEffect;
import domain.effect.StunEffect;

public abstract class Combatant {
    private final String name;
    private final Stats stats;
    private final List<StatusEffect> statusEffects;

    protected Combatant(String name, Stats stats) {
        this.name = name;
        this.stats = stats;
        this.statusEffects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Stats getStats() {
        return stats;
    }

    public List<StatusEffect> getStatusEffects() {
        return statusEffects;
    }

    public void addStatusEffect(StatusEffect effect) {
        statusEffects.add(effect);
    }

    public void removeExpiredEffects() {
        Iterator<StatusEffect> iterator = statusEffects.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isExpired()) {
                iterator.remove();
            }
        }
    }

    public boolean hasEffect(Class<? extends StatusEffect> effectType) {
        for (StatusEffect effect : statusEffects) {
            if (effectType.isInstance(effect)) {
                return true;
            }
        }
        return false;
    }

    public boolean canAct() {
        return !hasEffect(StunEffect.class) && isAlive();
    }

    public boolean isAlive() {
        return stats.isAlive();
    }

   

}
