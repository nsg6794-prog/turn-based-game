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
                int experienceReward,
                int goldReward) {

        super(name, healthPoints, maxHealthPoints, magicResistance, baseArmor, intelligence, agility, strength);
        this.experienceReward = experienceReward;
        this.goldReward = goldReward;
    }
    public int getExperienceReward() {
        return experienceReward;
    }
    public int getGoldReward() {
        return goldReward;
    }
}
