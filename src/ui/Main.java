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

        // Main Menu & Setup
        while (true) {
            GameSetupUI setupUI = new GameSetupUI(scanner);
            BattleContext context = setupUI.startNewGame();

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

            // Gameplay
            while (true) {
                BattleEngine engine = new BattleEngine(context);
                BattleResult result = engine.run();

                cli.printGameResult(result, context.getPlayer(), engine.getRoundNumber());

                int choice = cli.promptReplayMenu();
                if (choice == 1) {
                    System.out.println("\nRestarting with the same settings...\n");
                    context = rebuildContext(initialPlayerName, initialItems, initialDifficulty);
                    continue; 
                    
                } else if (choice == 2) {
                    System.out.println("\nReturning to main menu...\n");
                    break;
                    
                } else {
                    System.out.println("\nThanks for playing! Goodbye.");
                    scanner.close();
                    return; 
                }
            }
        }
    }

  
    private static BattleContext rebuildContext(String playerName, List<String> initialItems, Difficulty difficulty) {
        List<Item> freshItems = new ArrayList<>();
        for (String itemName : initialItems) {
            if (itemName.equals("Potion")) freshItems.add(ItemFactory.createItem(1));
            else if (itemName.equals("Power Stone")) freshItems.add(ItemFactory.createItem(2));
            else if (itemName.equals("Smoke Bomb")) freshItems.add(ItemFactory.createItem(3));
        }

        Player freshPlayer;
        if (playerName.equals("Warrior")) {
            freshPlayer = PlayerFactory.createWarrior(freshItems);
        } else {
            freshPlayer = PlayerFactory.createWizard(freshItems);
        }

        Level freshLevel = LevelFactory.createLevel(difficulty);

        List<Combatant> activeCombatants = new ArrayList<>();
        activeCombatants.add(freshPlayer);
        activeCombatants.addAll(freshLevel.getInitialWave().getEnemies());

        return new BattleContext(freshPlayer, freshLevel, activeCombatants, new SpeedTurnOrderStrategy());
    }
}