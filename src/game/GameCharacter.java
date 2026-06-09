package game;
public class GameCharacter {
    private String name;
    private int healthPoints;
    private int maxHealthPoints;
    private int magicResistance; 
    private int baseArmor;
    private int intelligence;
    private int agility;
    private int strength;
    private int level;
    private int experience;
    private int experienceToNextLevel = 100; // Initial XP required for level up
    
    public GameCharacter(String name,
                     int healthPoints,
                     int maxHealthPoints,
                     int magicResistance,
                     int baseArmor,
                     int intelligence,
                     int agility,
                     int strength,
                     int level,
                     int experience) {
        this.name = name;
        this.healthPoints = healthPoints;
        this.maxHealthPoints = maxHealthPoints;
        this.magicResistance = magicResistance;
        this.baseArmor = baseArmor;
        this.intelligence = intelligence;
        this.agility = agility;
        this.strength = strength;
        this.level = level;
        this.experience = experience;
                     }
                     public int rollInitiative() {
                        
                        // Initative formula might need a change for more balancing and reducing randomness.
                         int initiative = agility/5 + (int)(Math.random() * 20);
                         System.out.println(name + " has an initiative of " + initiative);
                         return initiative;
                     }
                     public String getName() {

                         return name;
                     } 
                     public int takeDamage(int damage) {
                        // DO SMT for the equation plsssssssss
                         int damageAfterArmor = damage - baseArmor;
                         if (damageAfterArmor < 0) {
                             damageAfterArmor = 0;
                         }
                         damageAfterArmor = Math.round(damageAfterArmor);
                         healthPoints -= damageAfterArmor;
                         if (healthPoints < 0) {
                             healthPoints = 0;
                         }
                         return damageAfterArmor;
                     }
                     public boolean isAlive() {
                         return healthPoints > 0;
                     }
                     public int getHealthpoints() {
                         return healthPoints;
                     }
                     public int getMaxHealthpoints() {
                         return maxHealthPoints;
                     }
                     public int heal(GameCharacter target, int healAmount) {
                         target.healthPoints += healAmount;
                         if (target.healthPoints > target.maxHealthPoints) {
                             target.healthPoints = target.maxHealthPoints;
                         }
                         return target.healthPoints;
                     }
                     public int takeMagicDamage(int damage) {
                         int damageAfterResistance = (int) (damage * (1 - (magicResistance / 100.0)));
                         if (damageAfterResistance < 0) {
                             damageAfterResistance = 0;
                         }
                         damageAfterResistance = Math.round(damageAfterResistance);
                         healthPoints -= damageAfterResistance;
                         if (healthPoints < 0) {
                             healthPoints = 0;
                         }
                         return damageAfterResistance;
                     }
                        public int getStrength() {
                            return strength;
                        }
                        public void gainExperience(int amount) {
                            experience += amount;
                            System.out.println(name + " gained " + amount + " XP!");

                            while (experience >= experienceToNextLevel) {
                            levelUp();
    }
}
                        private void levelUp() {
                            experience -= experienceToNextLevel;
                            level++;
                            experienceToNextLevel += 50;

                            maxHealthPoints += 10;
                            healthPoints = maxHealthPoints;
                            strength += 2;
                            agility += 2;

                            System.out.println(name + " leveled up to level " + level + "!");
}
}