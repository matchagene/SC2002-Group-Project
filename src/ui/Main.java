package ui;

import java.util.Scanner;

import domain.battle.BattleContext;
import domain.combatant.Enemy;
import domain.combatant.Player;
import domain.item.Item;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        
        GameSetupUI setupUI = new GameSetupUI(scanner);
        BattleContext context = setupUI.startNewGame();

        // Display summary 
        System.out.println();
        System.out.println("==============================================");
        System.out.println(" Game setup complete!");
        System.out.println("==============================================");

        //Display player 
        Player player = context.getPlayer();

        System.out.println("Chosen Player: " + player.getName());
        System.out.println("Stats:");
        System.out.println("HP: " + player.getStats().getMaxHp());
        System.out.println("Attack: " + player.getStats().getAttack());
        System.out.println("Defense: " + player.getStats().getDefense());
        System.out.println("Speed: " + player.getStats().getSpeed());

        //Display items
        System.out.println();
        System.out.println("Items:");
        for (Item item : player.getInventory()) {
            System.out.println("- " + item.getName());
        }

        //Display difficulty

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
        for (Enemy enemy : context.getLevel().getBackupWave().getEnemies()) {
            System.out.println("- " + enemy.getName()
                    + " (HP: " + enemy.getStats().getMaxHp()
                    + ", ATK: " + enemy.getStats().getAttack()
                    + ", DEF: " + enemy.getStats().getDefense()
                    + ", SPD: " + enemy.getStats().getSpeed() + ")");
        }

        System.out.println();
        System.out.println("BattleContext is ready for gameplay module.");









        
        scanner.close();
    }
}
