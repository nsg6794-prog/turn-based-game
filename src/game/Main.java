package game;

import combat.Battle;

public class Main {
    public static void main(String[] args) {
        Player player = createPlayer();
        Enemy enemy = createEnemy();
        
        ConsoleInput input = new ConsoleInput();
        Battle battle = new Battle(player, enemy, input);
        battle.startBattle();
    }

    public static Player createPlayer() {
        return new Player("Player", 100, 100, 20, 2, 3, 12, 15, 1, 0);
    }

    public static Enemy createEnemy() {
        return new Enemy("Villain", 120, 120, 25, 5, 10, 8, 20, 100);
    }
}
