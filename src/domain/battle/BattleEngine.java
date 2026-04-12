package domain.battle;

import domain.action.*; 
import domain.combatant.*;
import domain.effect.*;
import ui.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BattleEngine {

    private final BattleContext context;
    private final User cli;

    private boolean backupSpawned = false;
    private int roundNumber = 0;

    private final List<Action> playerActions;

    public BattleEngine(BattleContext context) {
        this.cli = User.getInstance();
        this.context = context;

        playerActions = new ArrayList<>(List.of(
                new BasicAttackAction(),
                new DefendAction(),
                new UseItemAction(), 
                new SpecialSkillAction()
        ));
    }

    // Main battle 

    public BattleResult run() {
        System.out.println("\n  STARTING GAME !  "); 

        while (true) {
            roundNumber++;
            cli.printRoundHeader(roundNumber);

            List<Combatant> ordered = context.getTurnOrderStrategy().determineTurnOrder(context.getActive());
            cli.printTurnOrder(ordered);

            for (Combatant combatant : ordered) {
                if (!combatant.isAlive()) continue;

                // Process Smoke Bomb at the start of the turn
                processStartOfTurnEffects(combatant);

                // Process stun
                if (!combatant.canAct()) {
                    System.out.println(combatant.getName() + " -> STUNNED: Turn skipped");
                    processStunTurn(combatant);
                    continue; 
                }

                // Execute normal turn
                if (combatant instanceof Player p) {
                    executePlayerTurn(p);
                } else if (combatant instanceof Enemy e) {
                    executeEnemyTurn(e);
                }

                // Decrease special skill cooldown
                if (combatant instanceof Player p) {
                    p.decrementSpecialSkillCooldown();
                }

                if (!context.getPlayer().isAlive()) {
                    cleanupDefendEffects();
                    cli.printEndOfRound(context.getPlayer(), context.getLivingEnemies(), roundNumber);
                    return BattleResult.DEFEAT;
                }
                
                if (context.getLivingEnemies().isEmpty()) {
                    if (!trySpawnBackup()) {
                        cleanupDefendEffects();
                        cli.printEndOfRound(context.getPlayer(), context.getLivingEnemies(), roundNumber);
                        return BattleResult.VICTORY;
                    }
                }
            }

            // Process def at the end of the round
            cleanupDefendEffects();
            cli.printEndOfRound(context.getPlayer(), context.getLivingEnemies(), roundNumber);

            // Backup spawn check at end of round
            if (context.getLivingEnemies().isEmpty()) {
                if (!trySpawnBackup()) {
                    return BattleResult.VICTORY;
                }
            }
        }
    }



    private void executePlayerTurn(Player p) {
        Action chosen = cli.selectAction(p, playerActions);
        
        String log = chosen.execute(p, context); 
        System.out.println(log);
    }


    private void executeEnemyTurn(Enemy e) {
        Action enemyAttack = new BasicAttackAction();
        String log = enemyAttack.execute(e, context);
        System.out.println(log);
    }

    private boolean trySpawnBackup() {
        if (!backupSpawned && context.getLevel().getBackupWave() != null) {
            backupSpawned = true;
            
            List<Enemy> backupEnemies = context.getLevel().getBackupWave().getEnemies();
            
            context.getActive().addAll(backupEnemies);
            cli.printBackupSpawn(backupEnemies);
            
            return true;
        }
        return false;
    }

    private void processStartOfTurnEffects(Combatant combatant) {
        List<StatusEffect> effects = combatant.getStatusEffects();

        for (StatusEffect effect : effects) {
            if (effect instanceof SmokeBombEffect) {
                effect.decrementDuration();
                if (effect.isExpired()) {
                    System.out.println("  [Smoke Bomb effect expires for " + combatant.getName() + "]");
                }
            }
        }

        combatant.removeExpiredEffects();
    }

    private void processStunTurn(Combatant combatant) {
        List<StatusEffect> effects = combatant.getStatusEffects();

        for (StatusEffect effect : effects) {
            if (effect instanceof StunEffect) {
                effect.decrementDuration();
                if (effect.isExpired()) {
                    System.out.println("  [Stun expires for " + combatant.getName() + "]");
                }
            }
        }

        combatant.removeExpiredEffects();
    }

    private void cleanupDefendEffects() {
        List<Combatant> all = new ArrayList<>();
        all.add(context.getPlayer());
        all.addAll(context.getLivingEnemies());

        for (Combatant c : all) {
            c.getStatusEffects().stream()
                .filter(e -> e instanceof DefendEffect)
                .forEach(StatusEffect::decrementDuration);

            List<StatusEffect> expired = c.getStatusEffects().stream()
                    .filter(e -> e instanceof DefendEffect && e.isExpired())
                    .collect(Collectors.toList());
                    
            if (!expired.isEmpty()) {
                c.getStats().decreaseDefense(DefendEffect.DEFENSE_BOOST * expired.size()); 
            }
            
            c.removeExpiredEffects();
        }
    }

    public int getRoundNumber() { return roundNumber; }
}