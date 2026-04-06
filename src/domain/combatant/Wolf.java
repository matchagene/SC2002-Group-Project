package domain.combatant;


public class Wolf extends Enemy {
    public Wolf(String label) {
        super("Wolf " + label, new Stats(40, 45, 5, 35));
    }

  
}
