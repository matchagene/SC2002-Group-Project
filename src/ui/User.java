package ui;

import java.util.List;
import java.util.Scanner;

import domain.action.SpecialSkillAction;
import domain.action.Action;
import domain.combatant.Combatant;
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
        int choice = readInt("Enter choice (1-" + available.size() + "): ", 1, available.size());
        return available.get(choice - 1);
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
            System.out.printf("  %d. %-12s — %s%n", i + 1,
                    inventory.get(i).getName(), inventory.get(i).getDescription());
        }
        return readInt("Select item (1-" + inventory.size() + "): ", 1, inventory.size()) - 1;
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
