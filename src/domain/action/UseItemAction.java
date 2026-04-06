package domain.action;

import java.util.List;

import domain.battle.BattleContext;
import domain.combatant.Combatant;
import domain.combatant.Player;
import domain.item.Item;
import ui.User;

public class UseItemAction implements Action {
    @Override
    public String getName() {
        return "Use Item";
    }

    @Override
    public String execute(Combatant actor, BattleContext context) {
        Player player = (Player) actor;

        List<Item> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            return "No items remaining!";
        }

        int choice = User.getInstance().selectItem(inventory);
        Item chosen = player.removeItem(choice);
        return chosen.use(actor, context);
    }
}
