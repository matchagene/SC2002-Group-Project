package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import domain.battle.BattleContext;
import domain.battle.BattleEngine;
import domain.battle.BattleResult;
import domain.battle.SpeedTurnOrderStrategy;
import domain.combatant.Combatant;
import domain.combatant.Enemy;
import domain.combatant.Player;
import domain.item.Item;
import domain.level.Difficulty;
import domain.level.Level;
import factory.ItemFactory;
import factory.LevelFactory;
import factory.PlayerFactory;

public class Main {

    private static final User cli = User.getInstance();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // OUTER LOOP: Main Menu & Setup
        while (true) {
            GameSetupUI setupUI = new GameSetupUI(scanner);
            BattleContext context = setupUI.startNewGame();

            // --- SAVE INITIAL STATE FOR REPLAY ---
            // (We must save these BEFORE the battle starts, otherwise items are consumed!)
            String initialPlayerName = context.getPlayer().getName();
            List<String> initialItems = new ArrayList<>();
            for (Item item : context.getPlayer().getInventory()) {
                initialItems.add(item.getName());
            }
            Difficulty initialDifficulty = context.getLevel().getDifficulty();
            // -------------------------------------

            // Display summary 
            System.out.println();
            System.out.println("==============================================");
            System.out.println(" Game setup complete!");
            System.out.println("==============================================");

            // Display player 
            Player player = context.getPlayer();
            System.out.println("Chosen Player: " + player.getName());
            System.out.println("Stats:");
            System.out.println("HP: " + player.getStats().getMaxHp());
            System.out.println("Attack: " + player.getStats().getAttack());
            System.out.println("Defense: " + player.getStats().getDefense());
            System.out.println("Speed: " + player.getStats().getSpeed());

            // Display items
            System.out.println();
            System.out.println("Items:");
            if (player.getInventory().isEmpty()) {
                System.out.println("- None");
            } else {
                for (Item item : player.getInventory()) {
                    System.out.println("- " + item.getName());
                }
            }

            // Display difficulty
            System.out.println();
            System.out.println("Difficulty: " + context.getLevel().getDifficulty());

            System.out.println();
            System.out.println("Initial Enemies:");
            for (Enemy enemy : context.getLevel().getInitialWave().getEnemies()) {
                System.out.println("- " + enemy.getName()
                        + " (HP: " + enemy.getStats().getMaxHp()
                        + ", ATK: " + enemy.getStats().getAttack()
                        + ", DEF: " + enemy.getStats().getDefense()
                        + ", SPD: " + enemy.getStats().getSpeed() + ")");
            }

            System.out.println();
            System.out.println("Backup Enemies:");
            // FIXED: Added a null check so Easy Difficulty doesn't crash here!
            if (context.getLevel().getBackupWave() != null) {
                for (Enemy enemy : context.getLevel().getBackupWave().getEnemies()) {
                    System.out.println("- " + enemy.getName()
                            + " (HP: " + enemy.getStats().getMaxHp()
                            + ", ATK: " + enemy.getStats().getAttack()
                            + ", DEF: " + enemy.getStats().getDefense()
                            + ", SPD: " + enemy.getStats().getSpeed() + ")");
                }
            } else {
                System.out.println("- None");
            }

            System.out.println();
            System.out.println("BattleContext is ready for gameplay module.");

            // INNER LOOP: Gameplay & Replay handling
            while (true) {
                BattleEngine engine = new BattleEngine(context);
                BattleResult result = engine.run();

                cli.printGameResult(result, context.getPlayer(), engine.getRoundNumber());

                int choice = cli.promptReplayMenu();
                if (choice == 1) {
                    System.out.println("\nRestarting with the same settings...\n");
                    // Rebuilds a fresh context with full HP and reset items!
                    context = rebuildContext(initialPlayerName, initialItems, initialDifficulty);
                    continue; // Restarts the INNER loop (Runs the battle again)
                    
                } else if (choice == 2) {
                    System.out.println("\nReturning to main menu...\n");
                    break; // Breaks INNER loop, goes back to OUTER loop (GameSetupUI)
                    
                } else {
                    System.out.println("\nThanks for playing! Goodbye.");
                    scanner.close();
                    return; // Completely exits the application
                }
            }
        }
    }

    /**
     * Safely rebuilds a completely fresh BattleContext using the saved initial settings.
     */
    private static BattleContext rebuildContext(String playerName, List<String> initialItems, Difficulty difficulty) {
        // 1. Rebuild fresh items
        List<Item> freshItems = new ArrayList<>();
        for (String itemName : initialItems) {
            if (itemName.equals("Potion")) freshItems.add(ItemFactory.createItem(1));
            else if (itemName.equals("Power Stone")) freshItems.add(ItemFactory.createItem(2));
            else if (itemName.equals("Smoke Bomb")) freshItems.add(ItemFactory.createItem(3));
        }

        // 2. Rebuild Player
        Player freshPlayer;
        if (playerName.equals("Warrior")) {
            freshPlayer = PlayerFactory.createWarrior(freshItems);
        } else {
            freshPlayer = PlayerFactory.createWizard(freshItems);
        }

        // 3. Rebuild Level
        Level freshLevel = LevelFactory.createLevel(difficulty);

        // 4. Rebuild Active Combatants
        List<Combatant> activeCombatants = new ArrayList<>();
        activeCombatants.add(freshPlayer);
        activeCombatants.addAll(freshLevel.getInitialWave().getEnemies());

        // 5. Return the brand new context
        return new BattleContext(freshPlayer, freshLevel, activeCombatants, new SpeedTurnOrderStrategy());
    }
}