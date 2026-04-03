package domain.combatant;

import java.util.List;

import domain.action.Action;
import domain.action.SpecialSkillAction;
import domain.item.Item;

public class Warrior extends Player {
    public Warrior(List<Item> inventory) {
        super("Warrior", new Stats(260, 40, 20, 30), inventory);
    }

    @Override
    public Action createSpecialSkillAction() {
        return new SpecialSkillAction("Shield Bash");
    }
}
