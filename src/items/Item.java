package items;

public class Item {
    private final String name;
    private final String description;
    private final int value;
    private final char rarity; // Common, Uncommon, Rare, Epic, Legendary 

    public Item(String name, String description, int value, char rarity) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getValue() {
        return value;
    }

    public char getRarity() {
        return rarity;
    }
    public int getRarityBonus() {
        return switch (getRarity()) {
            case 'C' -> 0;// Common
            case 'U' -> 5;// Uncommon
            case 'R' -> 10;// Rare
            case 'E' -> 20;// Epic
            case 'L' -> 30;// Legendary
            default -> 0;// Unknown
        };
    }
}
