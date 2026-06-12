package items;

import game.GameCharacter;

public class HPPot extends Potion {
    int healAmount;

    public HPPot(String name, String description, int value, char rarity, int healAmount) {
        super(name, description, value, rarity);
        this.healAmount = healAmount;
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
}
