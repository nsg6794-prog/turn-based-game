package game;

public class Enemy extends GameCharacter {

    public Enemy(String name,
                 int healthPoints,
                 int maxHealthPoints,
                 int magicResistance,
                 int baseArmor,
                 int intelligence,
                 int agility,
                 int strength
                 ) {

        super(name, healthPoints, maxHealthPoints, magicResistance, baseArmor, intelligence, agility, strength);
    }
}
