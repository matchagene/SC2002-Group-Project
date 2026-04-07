package domain.combatant;

import java.util.List;
import java.util.stream.Collectors;

import domain.battle.BattleContext;
import domain.effect.StunEffect;
import domain.item.Item;

public class Warrior extends Player {
    public Warrior(List<Item> inventory) {
        super("Warrior", new Stats(260, 40, 20, 30), inventory);
    }


    @Override
    public String getSpecialSkillName() { return "Shield Bash"; }

    @Override
    protected String useSpecialSkill(BattleContext context) {
        List<Combatant> enemies = context.getActive().stream()
            .filter(c -> c instanceof Enemy && c.isAlive())
            .collect(Collectors.toList());

        if (enemies.isEmpty()) return "No enemies to target!";

        Combatant target = context.selectTarget(enemies);
        int damage = Math.max(0, context.getPlayer().getStats().getAttack() - target.getStats().getDefense());
        target.getStats().takeDamage(damage);

        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(" uses Shield Bash on ").append(target.getName())
          .append("! HP: ").append(target.getStats().getCurrentHp() + damage)
          .append(" -> ").append(target.getStats().getCurrentHp())
          .append(" (dmg: ").append(context.getPlayer().getStats().getAttack()).append("-").append(target.getStats().getDefense())
          .append("=").append(damage).append(")");

        if (target.isAlive()) {
            target.addStatusEffect(new StunEffect());
            sb.append(" | ").append(target.getName()).append(" is STUNNED (2 turns)!");
        } else {
            sb.append(" | ").append(target.getName()).append(" ELIMINATED!");
        }
        return sb.toString();
    }
}
