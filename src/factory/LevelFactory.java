package factory;

import java.util.List;

import domain.combatant.Goblin;
import domain.combatant.Wolf;
import domain.level.Difficulty;
import domain.level.EnemyWave;
import domain.level.Level;

public class LevelFactory {
    public static Level createLevel(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return new Level(
                        Difficulty.EASY,
                        new EnemyWave(List.of(
                                new Goblin(), new Goblin(), new Goblin()
                        )),
                        null
                );
            case MEDIUM:
                return new Level(
                        Difficulty.MEDIUM,
                        new EnemyWave(List.of(
                                new Goblin(), new Wolf()
                        )),
                        new EnemyWave(List.of(
                                new Wolf(), new Wolf()
                        ))
                );
            case HARD:
                return new Level(
                        Difficulty.HARD,
                        new EnemyWave(List.of(
                                new Goblin(), new Goblin()
                        )),
                        new EnemyWave(List.of(
                                new Goblin(), new Wolf(), new Wolf()
                        ))
                );
            default:
                throw new IllegalArgumentException("Unknown difficulty");
        }
    }
}
