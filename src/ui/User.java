package ui;

import java.util.List;
import java.util.Scanner;

import domain.action.SpecialSkillAction;
import domain.action.UseItemAction;
import domain.battle.BattleResult;
import domain.action.Action;
import domain.combatant.Combatant;
import domain.combatant.Enemy;
import domain.combatant.Player;
import domain.item.Item;

public class User {


    private static User instance;
    private final Scanner scanner = new Scanner(System.in);

    private User() {}

    public static User getInstance() {
        if (instance == null) instance = new User();
        return instance;
    }
    

    public Action selectAction(Player player, List<Action> available) {
        System.out.println("\n" + player.getName() + "'s turn "
                + "[HP: " + player.getStats().getCurrentHp() + "/" + player.getStats().getMaxHp() + "]");
        System.out.println("Choose an action:");
        for (int i = 0; i < available.size(); i++) {
            Action a = available.get(i);
            String suffix = "";
            if (a instanceof SpecialSkillAction && !player.isSpecialSkillReady()) {
                suffix = " (cooldown: " + player.getSpecialSkillCooldown() + ")";
            }
            System.out.printf("  %d. %s%s%n", i + 1, a.getName(), suffix);
        }
        while (true) {
            int choice = readInt("Enter choice (1-" + available.size() + "): ", 1, available.size());
            Action chosen = available.get(choice - 1);

            if (chosen instanceof SpecialSkillAction && !player.isSpecialSkillReady()) {
                System.out.println("That skill is currently on cooldown! Please choose a different action.");
                continue; 
            }
            if (chosen instanceof UseItemAction && player.getInventory().isEmpty()) {
                System.out.println("Your inventory is empty! Please choose a different action.");
                continue; 
            }

            return chosen;
        }   
    }

    public Combatant selectTarget(List<Combatant> candidates) {
        if (candidates.size() == 1) {
            System.out.println("  [Auto-targeting: " + candidates.get(0).getName() + "]");
            return candidates.get(0);
        }
        System.out.println("Select target:");
        for (int i = 0; i < candidates.size(); i++) {
            Combatant c = candidates.get(i);
            System.out.printf("  %d. %-12s HP: %d/%d%n",
                    i + 1, c.getName(), c.getStats().getCurrentHp(), c.getStats().getMaxHp());
        }
        int choice = readInt("Enter choice (1-" + candidates.size() + "): ", 1, candidates.size());
        return candidates.get(choice - 1);
    }

    public int selectItem(List<Item> inventory) {
        System.out.println("Your items:");
        for (int i = 0; i < inventory.size(); i++) {
            System.out.printf("  %d. %-12s - %s%n", i + 1,
                    inventory.get(i).getName(), inventory.get(i).getDescription());
        }
        return readInt("Select item (1-" + inventory.size() + "): ", 1, inventory.size()) - 1;
    }

    //print display

    public void printRoundHeader(int round) {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("  ROUND " + round);
        System.out.println("─".repeat(60));
    }

    public void printTurnOrder(List<Combatant> ordered) {
        System.out.print("Turn order: ");
        for (int i = 0; i < ordered.size(); i++) {
            System.out.print(ordered.get(i).getName() + " (SPD " + ordered.get(i).getStats().getSpeed() + ")");
            if (i < ordered.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
    }

    public void printBackupSpawn(List<Enemy> backup) {
        System.out.println("\n*** BACKUP SPAWN! New enemies arrive: ***");
        for (Enemy enemy : backup) {
            System.out.println("- " + enemy.getName()
                    + " (HP: " + enemy.getStats().getMaxHp()
                    + ", ATK: " + enemy.getStats().getAttack()
                    + ", DEF: " + enemy.getStats().getDefense()
                    + ", SPD: " + enemy.getStats().getSpeed() + ")");
        }
        System.out.println();
    }

    public void printEndOfRound(Player player, List<Combatant> livingEnemies, int round) {
        System.out.println("\n--- End of Round " + round + " ---");
        System.out.printf("  %-10s HP: %d/%d", player.getName(),
                player.getStats().getCurrentHp(), player.getStats().getMaxHp());

        List<domain.item.Item> items = player.getInventory();
        if (!items.isEmpty()) {
            System.out.print("  | Items: ");
            items.forEach(i -> System.out.print(i.getName() + " "));
        }
        if (!player.isSpecialSkillReady()) {
            System.out.print("  | Cooldown: " + player.getSpecialSkillCooldown() + " rounds");
        }
        System.out.println();

        for (Combatant e : livingEnemies) {
            System.out.print("  " + e.getName() + " HP: " + e.getStats().getCurrentHp());
            if (!e.getStatusEffects().isEmpty()) {
                System.out.print(" [" + e.getStatusEffects().stream()
                        .map(eff -> eff.getName()).reduce((a, b) -> a + ", " + b).orElse("") + "]");
            }
            System.out.println();
        }
        System.out.println("─".repeat(60));
    }

    public int promptReplayMenu() {
        System.out.println("\nWhat would you like to do?");
        System.out.println("  1. Replay with the same settings");
        System.out.println("  2. Start a new game");
        System.out.println("  3. Exit");
        return readInt("Enter choice (1-3): ", 1, 3);
    }

    public void printGameResult(BattleResult result, Player player, int rounds, int enemiesRemaining) {
        System.out.println("─".repeat(60));
        if (result == BattleResult.VICTORY) {
            System.out.println("  *** VICTORY! Congratulations, you defeated all enemies! ***");
            System.out.printf("  Remaining HP: %d/%d  |  Total Rounds: %d%n",
                    player.getStats().getCurrentHp(), player.getStats().getMaxHp(), rounds);
            if (!player.getInventory().isEmpty()) {
                System.out.print("  Unused items: ");
                player.getInventory().forEach(i -> System.out.print(i.getName() + " "));
                System.out.println();
            }
        } else {
            System.out.println("  *** DEFEATED. Don't give up - try again! ***");
            System.out.println(" Enemies remaining: " + enemiesRemaining + " | Total Rounds Survived: " + rounds);
        }
        System.out.println("─".repeat(60));
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) return value;
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }


}
