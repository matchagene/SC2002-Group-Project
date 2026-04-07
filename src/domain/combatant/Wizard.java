package domain.combatant;

import java.util.List;
import java.util.stream.Collectors;

import domain.battle.BattleContext;
import domain.effect.ArcaneBlastBuffEffect;
import domain.item.Item;

public class Wizard extends Player {
    public Wizard(List<Item> inventory) {
        super("Wizard", new Stats(200, 50, 10, 20), inventory);
    }
    @Override
    public String getSpecialSkillName() { return "Arcane Blast"; }

    @Override
    protected String useSpecialSkill(BattleContext context) {
        List<Combatant> enemies = context.getActive().stream()
            .filter(c -> c instanceof Enemy && c.isAlive())
            .collect(Collectors.toList());


        if (enemies.isEmpty()) return "No enemies to target!";

        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(" unleashes Arcane Blast!\n");

        int kills = 0;

        for (Combatant enemy : enemies) {
            int damage = Math.max(0, context.getPlayer().getStats().getAttack() - enemy.getStats().getDefense());
            int hpBefore = enemy.getStats().getCurrentHp();
            enemy.getStats().takeDamage(damage);
            sb.append("  ").append(enemy.getName())
              .append(" HP: ").append(hpBefore).append(" -> ").append(enemy.getStats().getCurrentHp())
              .append(" (dmg: ").append(context.getPlayer().getStats().getAttack()).append("-").append(enemy.getStats().getDefense())
              .append("=").append(damage).append(")");
            if (!enemy.isAlive()) {
                sb.append(" X ELIMINATED");
                kills++;
            }
            sb.append("\n");
        }

        if (kills > 0) {
            int totalBonus = kills * ArcaneBlastBuffEffect.ATTACK_BONUS;
            int oldAttack = context.getPlayer().getStats().getAttack();
            context.getPlayer().getStats().increaseAttack(totalBonus);
            int newAttack = context.getPlayer().getStats().getAttack();

            if (!context.getPlayer().hasEffect(ArcaneBlastBuffEffect.class)) {
                context.getPlayer().addStatusEffect(new ArcaneBlastBuffEffect());
            }

            sb.append("Arcane kill bonus: ATK increased by ").append(totalBonus)
              .append(" (ATK: ").append(oldAttack).append(" -> ").append(newAttack).append(")")
              .append(" (lasts until end of level)");
        }

        return sb.toString().trim();
    }

  
}
