package game;

import combat.Battle;

public class Main {
    public static void main(String[] args) {
        Player player = createPlayer();
        EncounterManager encounterManager = new EncounterManager();
        Enemy enemy = encounterManager.getCurrentEnemy();
        
        ConsoleInput input = new ConsoleInput();
        Battle battle = new Battle(player, enemy, input);
        battle.startBattle();
    }

    public static Player createPlayer() {
        return new Player("Player", 100, 100, 20, 0, 12, 20, 30, 1, 0);
    }

}
