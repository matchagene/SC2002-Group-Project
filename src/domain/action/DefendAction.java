package domain.action;

import domain.battle.BattleContext;
import domain.combatant.Combatant;
import domain.effect.DefendEffect;
import domain.effect.StatusEffect;
import java.util.Iterator;



public class DefendAction implements Action {



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
                actor.getStats().decreaseDefense(DefendEffect.DEFENSE_BOOST);

                break; 
            }
        }
        StatusEffect defeffect = new DefendEffect();
        actor.getStats().increaseDefense(DefendEffect.DEFENSE_BOOST);
        actor.addStatusEffect(defeffect);

        
        return actor.getName() + " takes a defensive stance! DEF +" + DefendEffect.DEFENSE_BOOST
                + " [DEF now: " + actor.getStats().getDefense() + "] (lasts 2 turns)";
    }
}