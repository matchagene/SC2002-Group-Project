package domain.action;

import domain.battle.BattleContext;
import domain.combatant.Combatant;
import domain.combatant.Player;

public class SpecialSkillAction implements Action {
   
    @Override
    public String getName() { return "Special Skill"; }

   
    @Override
    public String execute(Combatant actor, BattleContext context) {
        Player player = (Player) actor;

        if (!player.isSpecialSkillReady()) {
            return player.getSpecialSkillName() + " is on cooldown ("
                    + player.getSpecialSkillCooldown() + " turn(s) remaining)!";
        }

        return player.executeSpecialSkill(context, false);
    }
}
