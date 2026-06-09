package game;

public class Player extends GameCharacter {

    public Player(String name,
                  int healthPoints,
                  int maxHealthPoints,
                  int magicResistance,
                  int baseArmor,
                  int intelligence,
                  int agility,
                  int strength,
                  int level,
                  int experience) {
        super(name, healthPoints, maxHealthPoints, magicResistance, baseArmor,
                intelligence, agility, strength, level, experience);
    }
}
