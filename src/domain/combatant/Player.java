package domain.combatant;

import java.util.ArrayList;
import java.util.List;

import domain.item.Item;

public abstract class Player extends Combatant {
    private final List<Item> inventory;

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

    public void removeItem(Item item) {
        inventory.remove(item);
    }
}
