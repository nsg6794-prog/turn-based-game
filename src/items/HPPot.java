package items;

import game.GameCharacter;

public class HPPot extends Potion {
    int healAmount;

    public HPPot(String name, String description, int value, char rarity, int healAmount) {
        super(name, description, value, rarity);
        this.healAmount = healAmount;
    }
    private int getPrice() {
        return super.getValue();
    }

    public int getHealAmount() {
        return healAmount;
    }
    public void consume(GameCharacter target) {
    int totalHeal = healAmount + getRarityBonus();
    target.heal(totalHeal);

    System.out.println("You consume the " + getName()
        + " and restore " + totalHeal + " HP!");
    }
    public static HPPot createHPPot(String name, String description, int value, char rarity, int healAmount) {
        return new HPPot(name, description, value, rarity, healAmount);
    }
}
