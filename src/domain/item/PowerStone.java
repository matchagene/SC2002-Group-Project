package domain.item;

public class PowerStone implements Item {
    @Override
    public String getName() {
        return "Power Stone";
    }

    @Override
    public String getDescription() {
        return "Trigger special skill once without changing cooldown";
    }
}
