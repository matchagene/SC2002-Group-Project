package domain.combatant;

import domain.action.Action;
import domain.action.BasicAttackAction;

public class Wolf extends Enemy {
    public Wolf() {
        super("Wolf", new Stats(40, 45, 5, 35));
    }

    @Override
    public Action createSpecialSkillAction() {
        return new BasicAttackAction();
    }
}
