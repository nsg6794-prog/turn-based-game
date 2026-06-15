package game;

import java.util.List;

public class EncounterManager {
    private final List<Enemy> enemies;
    private int currentEncounterIndex = 0;

    public EncounterManager() {
        enemies = List.of(
            new Enemy("Goblin", 30, 30, 5, 1, 5, 3, 2, 20, 5),
            new Enemy("Orc", 50, 50, 10, 3, 8, 4, 5, 50, 10),
            new Enemy("Troll", 80, 80, 15, 5, 12, 6, 8, 100, 20),
            new Enemy("Goblin", 30, 30, 0, 2, 2, 5, 6, 40, 10),
            new Enemy("Skeleton", 40, 40, 0, 3, 1, 4, 8, 50, 15),
            new Enemy("Bandit", 45, 45, 0, 2, 3, 7, 7, 60, 20),
            new Enemy("Orc", 60, 60, 0, 4, 2, 4, 11, 80, 25),
            new Enemy("Dark Mage", 50, 50, 4, 1, 9, 5, 5, 100, 30),
            new Enemy("Troll", 90, 90, 0, 6, 1, 3, 14, 130, 40),
            new Enemy("Dungeon Boss", 140, 140, 5, 8, 6, 6, 18, 250, 100)
        );
    }
    public Enemy getCurrentEnemy() {
        return enemies.get(currentEncounterIndex);
    }
    public boolean hasNextEncounter() {
        return currentEncounterIndex < enemies.size() - 1;
    }
    public void moveToNextEncounter() {
        if (hasNextEncounter()) {
            currentEncounterIndex++;
        }
    }
    public boolean runIsComplete() {
        return currentEncounterIndex >= enemies.size() - 1
                && !getCurrentEnemy().isAlive();
    }
}
