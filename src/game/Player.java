package game;

import items.Inventory;
import java.util.ArrayList;
import java.util.List;

public class Player extends GameCharacter {
    public enum LevelUpReward {
        MAX_HEALTH,
        STRENGTH,
        AGILITY
    }

    private static final int INITIAL_EXPERIENCE_TO_NEXT_LEVEL = 100;
    private static final int EXPERIENCE_INCREASE_PER_LEVEL = 50;

    private final Inventory inventory;
    private int level;
    private int experience;
    private int experienceToNextLevel;
    private int gold;
    private int pendingLevelUpRewards;
    private final List<Runnable> statsChangeListeners = new ArrayList<>();

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

        notifyStatsChanged();
    }

    public void gainGold(int amount) {
        gold += amount;
        System.out.println(getName() + " gained " + amount + " gold!");
        notifyStatsChanged();
    }

    public int getGold() {
        return gold;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public boolean hasPendingLevelUpReward() {
        return pendingLevelUpRewards > 0;
    }

    public int getPendingLevelUpRewards() {
        return pendingLevelUpRewards;
    }

    public void chooseLevelUpReward(LevelUpReward reward) {
        if (!hasPendingLevelUpReward()) {
            throw new IllegalStateException("No level-up reward is available.");
        }

        switch (reward) {
            case MAX_HEALTH -> increaseMaxHealthPoints(10);
            case STRENGTH -> increaseStrength(2);
            case AGILITY -> increaseAgility(2);
        }

        pendingLevelUpRewards--;
        notifyStatsChanged();
    }

    public void addStatsChangeListener(Runnable listener) {
        if (!statsChangeListeners.contains(listener)) {
            statsChangeListeners.add(listener);
        }
    }

    public void removeStatsChangeListener(Runnable listener) {
        statsChangeListeners.remove(listener);
    }

    private void notifyStatsChanged() {
        for (Runnable listener : List.copyOf(statsChangeListeners)) {
            listener.run();
        }
    }

    private void levelUp() {
        experience -= experienceToNextLevel;
        level++;
        experienceToNextLevel += EXPERIENCE_INCREASE_PER_LEVEL;
        pendingLevelUpRewards++;
        System.out.println(getName() + " leveled up to level " + level + "!");
    }
}
