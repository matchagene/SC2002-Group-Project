package domain.action;

import java.util.List;

import domain.battle.BattleContext;
import domain.combatant.Combatant;
import domain.combatant.Player;

public class BasicAttackAction implements Action {
    @Override
    public String getName() {
        return "Basic Attack";
    }

    @Override
    public String execute(Combatant actor, BattleContext context) {
        List<Combatant> targets = context.getActive();
        if (targets.isEmpty()) return "No targets available.";

        Combatant target;

        if (actor instanceof Player){
            target = context.selectTarget(targets);
        }
        else {
            target = context.getPlayer();
        }

        int damage = Math.max(0, actor.getStats().getAttack() - target.getStats().getDefense());
        int hpBefore = target.getStats().getCurrentHp();
        target.getStats().takeDamage(damage);

        StringBuilder sb = new StringBuilder();
        sb.append(actor.getName()).append(" → BasicAttack → ").append(target.getName())
          .append(": HP: ").append(hpBefore).append(" → ").append(target.getStats().getCurrentHp())
          .append(" (dmg: ").append(actor.getStats().getAttack()).append("−").append(target.getStats().getDefense())
          .append("=").append(damage).append(")");

        if (!target.isAlive()) {
            sb.append(" ✗ ELIMINATED");
        }
        return sb.toString();
    }
}
