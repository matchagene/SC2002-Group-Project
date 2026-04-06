package domain.combatant;



public class Goblin extends Enemy {
    public Goblin(String label) {
        super("Goblin " + label, new Stats(55, 35, 15, 25));
    }

}
