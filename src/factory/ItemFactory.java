package factory;

import domain.item.Item;
import domain.item.Potion;
import domain.item.PowerStone;
import domain.item.SmokeBomb;

public class ItemFactory {
    public static Item createItem(int choice) {
        switch (choice) {
            case 1:
                return new Potion();
            case 2:
                return new PowerStone();
            case 3:
                return new SmokeBomb();
            default:
                throw new IllegalArgumentException("Invalid item choice");
        }
    }
}
