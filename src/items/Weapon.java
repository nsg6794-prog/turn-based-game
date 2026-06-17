package items;

public class Weapon extends Item {
    final private int damage;
    final private String damageType; // e.g., Melee, Ranged, Magic
    
    

    public Weapon(String name, String description, int value, char rarity,
                  int damage, String damageType) {
        super(name, description, value, rarity);
        this.damage = damage;
        this.damageType = damageType;
    }

    public int getDamage() {
        return damage;
    }
    public String getDamageType() {
        return damageType;
    }

    
        
    }


