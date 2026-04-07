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

    // ── Main battle loop ───────────────────────────────────────────────────

    public BattleResult run() {
        System.out.println("\n  STARTING GAME !  "); 

        while (true) {
            roundNumber++;
            cli.printRoundHeader(roundNumber);

            List<Combatant> ordered = context.getTurnOrderStrategy().determineTurnOrder(context.getActive());
            cli.printTurnOrder(ordered);

            for (Combatant combatant : ordered) {
                if (!combatant.isAlive()) continue;

                // 1. Process Smoke Bomb at the START of the turn!
                processTurnEffects(combatant);

                // 2. Process STUN
                if (combatant.hasEffect(StunEffect.class)) {
                    System.out.println(combatant.getName() + " → STUNNED: Turn skipped");
                    
                    combatant.getStatusEffects().stream()
                        .filter(e -> e instanceof StunEffect)
                        .findFirst()
                        .ifPresent(e -> {
                            e.decrementDuration(); 
                            if (e.isExpired()) {
                                System.out.println("  [Stun expires for " + combatant.getName() + "]");
                            }
                        });
    
                    combatant.removeExpiredEffects(); 
                    continue; 
                }

                // 3. Execute normal turn
                if (combatant instanceof Player p) {
                    executePlayerTurn(p);
                } else if (combatant instanceof Enemy e) {
                    executeEnemyTurn(e);
                }

                // 4. Decrement special skill cooldown
                if (combatant instanceof Player p) {
                    p.decrementSpecialSkillCooldown();
                }

                // Check for game-ending conditions mid-round
                if (!context.getPlayer().isAlive()) {
                    cleanupDefendEffects();
                    cli.printEndOfRound(context.getPlayer(), getLivingEnemies(), roundNumber);
                    return BattleResult.DEFEAT;
                }
                
                if (getLivingEnemies().isEmpty()) {
                    if (!trySpawnBackup()) {
                        cleanupDefendEffects();
                        cli.printEndOfRound(context.getPlayer(), getLivingEnemies(), roundNumber);
                        return BattleResult.VICTORY;
                    }
                }
            }

            // Process DEFEND at the end of the round
            cleanupDefendEffects();
            cli.printEndOfRound(context.getPlayer(), getLivingEnemies(), roundNumber);

            // Backup spawn check at end of round
            if (getLivingEnemies().isEmpty()) {
                if (!trySpawnBackup()) {
                    return BattleResult.VICTORY;
                }
            }
        }
    }

    // ── Private helpers ────────────────────────────────────────────────────

    private List<Combatant> getLivingEnemies() {
        return context.getActive().stream()
                .filter(c -> c instanceof Enemy && c.isAlive())
                .collect(Collectors.toList());
    }

    private void executePlayerTurn(Player p) {
        List<Action> available = getAvailableActions(p);
        Action chosen = cli.selectAction(p, available);
        
        String log = chosen.execute(p, context); 
        System.out.println(log);
    }

    private List<Action> getAvailableActions(Player p) {
        List<Action> available = new ArrayList<>();
        for (Action a : playerActions) {
            if (a instanceof UseItemAction && p.getInventory().isEmpty()) continue;
            if (a instanceof SpecialSkillAction && !p.isSpecialSkillReady()) continue;
            available.add(a);
        }
        return available;
    }

    private void executeEnemyTurn(Enemy e) {
        Action enemyAttack = new BasicAttackAction();
        String log = enemyAttack.execute(e, context);
        System.out.println(log);
    }

    private boolean trySpawnBackup() {
        // FIXED: Check if getBackupWave() is not null (Easy difficulty has a null backup wave)
        if (!backupSpawned && context.getLevel().getBackupWave() != null) {
            backupSpawned = true;
            
            // FIXED: Extract the List<Enemy> using getEnemies() from the EnemyWave object
            List<Enemy> backupEnemies = context.getLevel().getBackupWave().getEnemies();
            
            context.getActive().addAll(backupEnemies);
            cli.printBackupSpawn(backupEnemies);
            
            return true;
        }
        return false;
    }

    private void processTurnEffects(Combatant combatant) {
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

    private void cleanupDefendEffects() {
        List<Combatant> all = new ArrayList<>();
        all.add(context.getPlayer());
        all.addAll(getLivingEnemies());

        for (Combatant c : all) {
            c.getStatusEffects().stream()
                .filter(e -> e instanceof DefendEffect)
                .forEach(StatusEffect::decrementDuration);

            List<StatusEffect> expired = c.getStatusEffects().stream()
                    .filter(e -> e instanceof DefendEffect && e.isExpired())
                    .collect(Collectors.toList());
                    
            for (StatusEffect e : expired) {
                c.getStats().decreaseDefense(10); 
            }
            
            c.removeExpiredEffects();
        }
    }

    public int getRoundNumber() { return roundNumber; }
}