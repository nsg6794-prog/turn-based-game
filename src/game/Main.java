package game;

import combat.Battle;

public class Main {
    public static void main(String[] args) {
        GameCharacter player = createPlayer();
        GameCharacter enemy = createEnemy();
        
        ConsoleInput input = new ConsoleInput();
        Battle battle = new Battle(player, enemy, input);
        battle.startBattle();
    }

    public static GameCharacter createPlayer() {
        return new GameCharacter("Player", 100, 100, 20, 2, 3, 12, 15, 1, 0);
    }

    public static GameCharacter createEnemy() {
        return new Enemy("Villain", 120, 120, 25, 5, 10, 8, 20,1,0);
    }
}
