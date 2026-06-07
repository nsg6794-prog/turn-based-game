package game;

import combat.Battle;

public class Main {
    public static void main(String[] args) {
        CharacterConfig hero = createHero();
        CharacterConfig villain = createVillain();
        
        ConsoleInput input = new ConsoleInput();
        Battle battle = new Battle(hero, villain, input);
        battle.startBattle();
    }

    public static CharacterConfig createHero() {
        return new CharacterConfig("Hero", 100, 100, 20, 2, 3, 12, 15);
    }

    public static CharacterConfig createVillain() {
        return new CharacterConfig("Villain", 120, 120, 25, 5, 10, 8, 20);
    }
}
