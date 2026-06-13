package game;

import items.Inventory;
import java.util.Scanner;

public class Player extends GameCharacter {
    private static final int INITIAL_EXPERIENCE_TO_NEXT_LEVEL = 100;
    private static final int EXPERIENCE_INCREASE_PER_LEVEL = 50;

    private final Inventory inventory;
    private int level;
    private int experience;
    private int experienceToNextLevel;
    private int gold;

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
                intelligence, agility, strength);
        this.inventory = new Inventory();
        this.level = level;
        this.experience = experience;
        this.experienceToNextLevel = INITIAL_EXPERIENCE_TO_NEXT_LEVEL;
    }

    public void gainExperience(int amount) {
        experience += amount;
        System.out.println(getName() + " gained " + amount + " XP!");

        while (experience >= experienceToNextLevel) {
            levelUp();
        }
    }

    private void levelUp() {
        experience -= experienceToNextLevel;
        level++;
        experienceToNextLevel += EXPERIENCE_INCREASE_PER_LEVEL;
        Scanner scanner = new Scanner(System.in);
        System.out.println(getName() + " leveled up to level " + level + "!");
        System.out.println("Choose a stat to increase: ");
        System.out.println("1. Health Points (+10)");
        System.out.println("2. Strength (+2)");
        System.out.println("3. Agility (+2)");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 ->  increaseMaxHealthPoints(10);
            case 2 -> increaseStrength(2);
            case 3 -> increaseAgility(2);
            default -> System.out.println("Invalid choice. No stat increased.");

        }
    }
}