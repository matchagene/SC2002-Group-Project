package domain.combatant;

import domain.action.Action;
import domain.action.BasicAttackAction;

public class Goblin extends Enemy {
    public Goblin() {
        super("Goblin", new Stats(55, 35, 15, 25));
    }

    @Override
    public Action createSpecialSkillAction() {
        return new BasicAttackAction();
    }
}
