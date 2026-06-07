package game;

import combat.Battle;

public class Main {
    public static void main(String[] args) {
        GameCharacter hero = createHero();
        GameCharacter villain = createVillain();
        
        ConsoleInput input = new ConsoleInput();
        Battle battle = new Battle(hero, villain, input);
        battle.startBattle();
    }

    public static GameCharacter createHero() {
        return new GameCharacter("Hero", 100, 100, 20, 2, 3, 12, 15);
    }

    public static GameCharacter createVillain() {
        return new GameCharacter("Villain", 120, 120, 25, 5, 10, 8, 20);
    }
}
