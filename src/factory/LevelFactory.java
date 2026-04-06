package factory;

import java.util.List;

import domain.combatant.Goblin;
import domain.combatant.Wolf;
import domain.level.Difficulty;
import domain.level.EnemyWave;
import domain.level.Level;

//Setting up difficulty

public class LevelFactory {
    public static Level createLevel(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return new Level(
                        Difficulty.EASY,
                        new EnemyWave(List.of(
                                new Goblin("A"), new Goblin("B"), new Goblin("C")
                        )),
                        null
                );
            case MEDIUM:
                return new Level(
                        Difficulty.MEDIUM,
                        new EnemyWave(List.of(
                                new Goblin("A"), new Wolf("A")
                        )),
                        new EnemyWave(List.of(
                                new Wolf("B"), new Wolf("C")
                        ))
                );
            case HARD:
                return new Level(
                        Difficulty.HARD,
                        new EnemyWave(List.of(
                                new Goblin("A"), new Goblin("B")
                        )),
                        new EnemyWave(List.of(
                                new Goblin("C"), new Wolf("A"), new Wolf("B")
                        ))
                );
            default:
                throw new IllegalArgumentException("Unknown difficulty");
        }
    }
}
