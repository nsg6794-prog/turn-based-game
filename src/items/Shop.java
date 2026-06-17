package items;

import game.Player;
import java.util.ArrayList;
import java.util.List;

public class Shop {
    List<Item> stock;

    public Shop() {
        stock = new ArrayList<>();
    }
    public void addItem(Item item) {
        stock.add(item);
    }
    public List<Item> getStock() {
        return stock;
    }
    public void initializeStock() {
        stock.add(HPPot.createHPPot("Small Health Potion", "Restores a small amount of health.", 10, 'C', 10));
        stock.add(HPPot.createHPPot("Medium Health Potion", "Restores a moderate amount of health.", 25, 'U', 25));
        stock.add(HPPot.createHPPot("Large Health Potion", "Restores a large amount of health.", 50, 'R', 40));
        stock.add(HPPot.createHPPot("Epic Health Potion", "Restores a significant amount of health.", 100, 'E', 70));
        stock.add(HPPot.createHPPot("Legendary Health Potion", "Restores a massive amount of health.", 200, 'L', 120));
        stock.add(new ShortSword("Short Sword", "A basic short sword.", 50, 'C', 10, "slashing"));
        stock.add(new LongSword("Long Sword", "A basic long sword.", 100, 'U', 15, "slashing"));
        stock.add(new OneHandedAxe("Battle Axe", "A heavy battle axe.", 150, 'R', 20, "slashing"));
    }
    public boolean buyItem(Player player, Item item) {
        if (player.getGold() >= item.getValue()) {
            player.spendGold(item.getValue());
            player.getInventory().addItem(item);
            stock.remove(item);
            return true;
        } else {
            return false;
        }
    }

}
