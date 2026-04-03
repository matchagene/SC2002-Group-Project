package domain.combatant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import domain.action.Action;
import domain.effect.StatusEffect;
import domain.effect.StunEffect;

public abstract class Combatant {
    private final String name;
    private final Stats stats;
    private final List<StatusEffect> statusEffects;
    private int specialSkillCooldown;

    protected Combatant(String name, Stats stats) {
        this.name = name;
        this.stats = stats;
        this.statusEffects = new ArrayList<>();
        this.specialSkillCooldown = 0;
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

    public int getSpecialSkillCooldown() {
        return specialSkillCooldown;
    }

    public void setSpecialSkillCooldown(int specialSkillCooldown) {
        this.specialSkillCooldown = specialSkillCooldown;
    }

    public void decrementCooldown() {
        if (specialSkillCooldown > 0) {
            specialSkillCooldown--;
        }
    }

    public abstract Action createSpecialSkillAction();
}
