package items;
import game.GameCharacter;
import game.Player;
public class HPPot extends Potion {
    int healAmount;

    public HPPot(String name, String description, int value, char rarity, int healAmount) {
        super(name, description, value, rarity);
        this.healAmount = healAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }
    public void consume() {
        // Logic to heal the player by healAmount
        System.out.println("You consume the " + getName() + " and restore " + healAmount + " HP!");
        heal(healAmount + getRarityBonus(), Player);
    }
}
