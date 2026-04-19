package domain.level;

public class Level {
    private final Difficulty difficulty;
    private final EnemyWave initialWave;
    private final EnemyWave backupWave;

    //difficult + Enemy wave 
    public Level(Difficulty difficulty, EnemyWave initialWave, EnemyWave backupWave) {
        this.difficulty = difficulty;
        this.initialWave = initialWave;
        this.backupWave = backupWave;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public EnemyWave getInitialWave() {
        return initialWave;
    }

    public EnemyWave getBackupWave() {
        return backupWave;
    }
}
