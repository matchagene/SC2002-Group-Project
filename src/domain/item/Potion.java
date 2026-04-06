package domain.item;

import domain.battle.BattleContext;
import domain.combatant.Combatant;

public class Potion implements Item {

    private static final int HEAL_AMOUNT = 100;

    @Override
    public String getName() {
        return "Potion";
    }

    @Override
    public String getDescription() {
        return "Heal 100 HP";
    }

    
    @Override
    public String use(Combatant user, BattleContext context) {
        user.getStats().heal(HEAL_AMOUNT);
        return user.getName() + " used Potion and healed " + HEAL_AMOUNT + " HP! "
                + "[HP: " + user.getStats().getCurrentHp() + "/" + user.getStats().getMaxHp() + "]";
    }
}
