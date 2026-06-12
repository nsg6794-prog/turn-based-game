package items;

public class Potion extends Item {
    private String name;
    
    public Potion(String name, String description, int value, char rarity) {
        super(name, description, value, rarity);
        this.name = name;
    }
    public String getName() {
        return name;
    }
    

}
