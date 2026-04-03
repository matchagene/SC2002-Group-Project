package domain.item;

public class Potion implements Item {
    @Override
    public String getName() {
        return "Potion";
    }

    @Override
    public String getDescription() {
        return "Heal 100 HP";
    }
}
