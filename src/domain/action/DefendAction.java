package domain.action;

import domain.battle.BattleContext;
import domain.combatant.Combatant;
import domain.effect.DefendEffect;
import domain.effect.StatusEffect;
import java.util.Iterator;



public class DefendAction implements Action {

    private static final int defense = 10;



    @Override
    public String getName() {
        return "Defend";
    }

    @Override
    public String execute(Combatant actor, BattleContext context) {

        Iterator<StatusEffect> iterator = actor.getStatusEffects().iterator();
    
        while (iterator.hasNext()) {
            StatusEffect effect = iterator.next(); 
            if (effect instanceof DefendEffect) {            
                iterator.remove(); 
                actor.getStats().decreaseDefense(defense);

                break; 
            }
        }
        StatusEffect defeffect = new DefendEffect();
        actor.getStats().increaseDefense(defense);
        actor.addStatusEffect(defeffect);

        
        return actor.getName() + " takes a defensive stance! DEF +" + defense
                + " [DEF now: " + actor.getStats().getDefense() + "] (lasts 2 turns)";
    }
}