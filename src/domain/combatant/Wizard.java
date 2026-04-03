package domain.combatant;

import java.util.List;

import domain.action.Action;
import domain.action.SpecialSkillAction;
import domain.item.Item;

public class Wizard extends Player {
    public Wizard(List<Item> inventory) {
        super("Wizard", new Stats(200, 50, 10, 20), inventory);
    }

    @Override
    public Action createSpecialSkillAction() {
        return new SpecialSkillAction("Arcane Blast");
    }
}
