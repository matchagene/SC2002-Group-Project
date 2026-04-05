package ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import domain.battle.BattleContext;
import domain.battle.SpeedTurnOrderStrategy;
import domain.combatant.Player;
import domain.item.Item;
import domain.level.Difficulty;
import domain.level.Level;
import factory.ItemFactory;
import factory.LevelFactory;
import factory.PlayerFactory;

public class GameSetupUI {
    private final Scanner scanner;

    public GameSetupUI(Scanner scanner) {
        this.scanner = scanner;
    }

    public BattleContext startNewGame() {
        displayTitle();
        displayPlayerOptions();
        Player player = choosePlayer();

        List<Item> chosenItems = chooseTwoItems();
        player = rebuildPlayerWithItems(player, chosenItems);

        displayDifficultyOptions();
        Difficulty difficulty = chooseDifficulty();

        Level level = LevelFactory.createLevel(difficulty);

        return new BattleContext(
                player,
                level,
                level.getInitialWave().getEnemies(),
                new SpeedTurnOrderStrategy()
        );
    }

    private void displayTitle() {
        System.out.println("==============================================");
        System.out.println("        TURN-BASED COMBAT ARENA");
        System.out.println("==============================================");
        System.out.println();
    }

    private void displayPlayerOptions() {
        System.out.println("Choose Your Player:");
        System.out.println("1. Warrior");
        System.out.println("   HP: 260 | ATK: 40 | DEF: 20 | SPD: 30");
        System.out.println("   Special Skill: Shield Bash");
        System.out.println("   Effect: Deal basic attack damage to one enemy and stun it");
        System.out.println("   for the current turn and the next turn.");
        System.out.println();

        System.out.println("2. Wizard");
        System.out.println("   HP: 200 | ATK: 50 | DEF: 10 | SPD: 20");
        System.out.println("   Special Skill: Arcane Blast");
        System.out.println("   Effect: Deal basic attack damage to all enemies.");
        System.out.println("   Each enemy defeated adds +10 Attack until end of level.");
        System.out.println();
    }

    private Player choosePlayer() {
        int choice = readChoice("Enter player choice (1-2): ", 1, 2);
        if (choice == 1) {
            return PlayerFactory.createWarrior(new ArrayList<>());
        } else {
            return PlayerFactory.createWizard(new ArrayList<>());
        }
    }

    private List<Item> chooseTwoItems() {
        System.out.println("Choose 2 items (duplicates allowed):");
        displayItemOptions();

        List<Item> items = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            int choice = readChoice("Choose item " + i + " (1-3): ", 1, 3);
            items.add(ItemFactory.createItem(choice));
        }

        return items;
    }

    private void displayItemOptions() {
        System.out.println("1. Potion");
        System.out.println("   Effect: Heal 100 HP");
        System.out.println();
        System.out.println("2. Power Stone");
        System.out.println("   Effect: Trigger special skill once without affecting cooldown");
        System.out.println();
        System.out.println("3. Smoke Bomb");
        System.out.println("   Effect: Enemy attacks deal 0 damage this turn and next turn");
        System.out.println();
    }

    private void displayDifficultyOptions() {
        System.out.println("Choose Difficulty:");
        System.out.println("1. Easy");
        System.out.println("   Initial Spawn: 3 Goblins");
        System.out.println();
        System.out.println("2. Medium");
        System.out.println("   Initial Spawn: 1 Goblin, 1 Wolf");
        System.out.println("   Backup Spawn: 2 Wolves");
        System.out.println();
        System.out.println("3. Hard");
        System.out.println("   Initial Spawn: 2 Goblins");
        System.out.println("   Backup Spawn: 1 Goblin, 2 Wolves");
        System.out.println();
        displayEnemyInfo();
    }

    private void displayEnemyInfo() {
        System.out.println("Enemy Information:");
        System.out.println("Goblin -> HP: 55 | Attack: 35 | Defense: 15 | Speed: 25");
        System.out.println("Wolf   -> HP: 40 | Attack: 45 | Defense: 5  | Speed: 35");
        System.out.println();
    }

    private Difficulty chooseDifficulty() {
        int choice = readChoice("Enter difficulty choice (1-3): ", 1, 3);
        switch (choice) {
            case 1:
                return Difficulty.EASY;
            case 2:
                return Difficulty.MEDIUM;
            case 3:
                return Difficulty.HARD;
            default:
                throw new IllegalArgumentException("Invalid difficulty");
        }
    }

    private int readChoice(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) {
                    return choice;
                }
            } catch (NumberFormatException e) {
                
            }

            System.out.println("Invalid input. Please try again.");
        }
    }

    private Player rebuildPlayerWithItems(Player player, List<Item> items) {
        if (player.getName().equals("Warrior")) {
            return PlayerFactory.createWarrior(items);
        } else {
            return PlayerFactory.createWizard(items);
        }
    }
}
