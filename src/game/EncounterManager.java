package game;

import java.util.List;

public class EncounterManager {
    private List<Enemy> enemies;
    private int currentEncounterIndex = 0;

    public EncounterManager() {
        enemies = List.of(
            new Enemy("Goblin", 30, 30, 5, 1, 5, 3, 2, 20),
            new Enemy("Orc", 50, 50, 10, 3, 8, 4, 5, 50),
            new Enemy("Troll", 80, 80, 15, 5, 12, 6, 8, 100)
        );
    }
    public Enemy getCurrentEnemy() {
        return enemies.get(currentEncounterIndex);
        
    }
    public boolean hasNextEncounter() {
        return currentEncounterIndex < enemies.size()-1;
    }
    public void moveToNextEncounter() {
        if (hasNextEncounter()) {
            currentEncounterIndex++;
        }
    }


}
