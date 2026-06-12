package items;

public class Weapon extends Item {
    final private int damage;
    final private String damageType; // e.g., Melee, Ranged, Magic
    final private String weaponType; // e.g., Sword, Bow, Staff
    

    public Weapon(String name, String description, int value, char rarity,
                  int damage, int durability, String weaponType, String damageType) {
        super(name, description, value, rarity);
        this.damage = damage;
        this.weaponType = weaponType;
        this.damageType = damageType;
    }

    public int getDamage() {
        return damage;
    }
    public String getWeaponType() {
        return weaponType;
    }
    public String getDamageType() {
        return damageType;
    }
    
        
    }


