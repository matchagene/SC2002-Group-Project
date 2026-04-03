package domain.item;

public class SmokeBomb implements Item {
    @Override
    public String getName() {
        return "Smoke Bomb";
    }

    @Override
    public String getDescription() {
        return "Enemy attacks do 0 damage this turn and next turn";
    }
}
