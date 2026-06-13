package game;

public class Enemy extends GameCharacter {

    private int experienceReward;
    private int goldReward;

    public Enemy(String name,
                 int healthPoints,
                 int maxHealthPoints,
                 int magicResistance,
                 int baseArmor,
                 int intelligence,
                 int agility,
                 int strength,
                int experienceReward) {

        super(name, healthPoints, maxHealthPoints, magicResistance, baseArmor, intelligence, agility, strength);
        this.experienceReward = experienceReward;

    }
    public int getExperienceReward() {
        return experienceReward;
    }
}
