package factory;

import java.util.List;

import domain.combatant.Player;
import domain.combatant.Warrior;
import domain.combatant.Wizard;
import domain.item.Item;

public class PlayerFactory {
    public static Player createWarrior(List<Item> items) {
        return new Warrior(items);
    }

    public static Player createWizard(List<Item> items) {
        return new Wizard(items);
    }
}
