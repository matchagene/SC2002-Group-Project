package domain.item;

import domain.battle.BattleContext;
import domain.combatant.Combatant;
import domain.combatant.Player;

public class PowerStone implements Item {
    @Override
    public String getName() {
        return "Power Stone";
    }

    @Override
    public String getDescription() {
        return "Trigger special skill once without changing cooldown";
    }

    @Override
    public String use(Combatant user, BattleContext context) {
        Player player = (Player) user;
        String result = player.executeSpecialSkill(context, true);
        return "Power Stone activated! " + result;
    }
}
