package domain.combatant;

import java.util.ArrayList;
import java.util.List;
import domain.battle.BattleContext;
import domain.item.Item;

public abstract class Player extends Combatant {
    private final List<Item> inventory;
    private int specialSkillCooldown = 0;
    private static final int SPECIAL_SKILL_COOLDOWN = 3;




    protected Player(String name, Stats stats, List<Item> inventory) {
        super(name, stats);
        this.inventory = new ArrayList<>(inventory);
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public Item removeItem(int index) {
        return inventory.remove(index);
    }

    public boolean isSpecialSkillReady() {
        return specialSkillCooldown == 0;
    }

    public int getSpecialSkillCooldown() {
        return specialSkillCooldown;
    }

    public void decrementSpecialSkillCooldown() {
        if (specialSkillCooldown > 0) {
            specialSkillCooldown--;
        }
    }

    public String executeSpecialSkill(BattleContext context, boolean freeUse) {
        String result = useSpecialSkill(context);
        if (!freeUse) {
            specialSkillCooldown = SPECIAL_SKILL_COOLDOWN;
        }
        return result;
    }

    protected abstract String useSpecialSkill(BattleContext context);

    public abstract String getSpecialSkillName();

}
