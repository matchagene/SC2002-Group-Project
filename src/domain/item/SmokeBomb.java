package domain.item;

import domain.battle.BattleContext;
import domain.combatant.Combatant;
import domain.effect.SmokeBombEffect;
//Smokebomb initialise
public class SmokeBomb implements Item {
    @Override
    public String getName() {
        return "Smoke Bomb";
    }

    @Override
    public String getDescription() {
        return "Enemy attacks do 0 damage this turn and next turn";
    }

    @Override
    public String use(Combatant user, BattleContext context) {
        user.addStatusEffect(new SmokeBombEffect());
        return user.getName() + " used Smoke Bomb! Enemy attacks will deal 0 damage this turn and next turn.";
    }
}
