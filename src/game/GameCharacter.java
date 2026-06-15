package game;
public abstract class GameCharacter {
    private String name;
    private int healthPoints;
    private int maxHealthPoints;
    private int magicResistance; 
    private int baseArmor;
    private int intelligence;
    private int agility;
    private int strength;

    public GameCharacter(String name,
                            int healthPoints,
                            int maxHealthPoints,
                            int magicResistance,
                            int baseArmor,
                            int intelligence,
                            int agility,
                            int strength) {
        this.name = name;
        this.healthPoints = healthPoints;
        this.maxHealthPoints = maxHealthPoints;
        this.magicResistance = magicResistance;
        this.baseArmor = baseArmor;
        this.intelligence = intelligence;
        this.agility = agility;
        this.strength = strength;
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
                     public int heal(int healAmount) {
                         if (healAmount < 0) {
                             throw new IllegalArgumentException("Heal amount cannot be negative");
                         }
                         healthPoints += healAmount;
                         if (healthPoints > maxHealthPoints) {
                             healthPoints = maxHealthPoints;
                         }
                         return healthPoints;
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
                        public int getAgility() {
                            return agility;
                        }
                        public int getIntelligence() {
                            return intelligence;
                        }
                        protected void increaseMaxHealthPoints(int amount) {
                            maxHealthPoints += amount;
                            healthPoints = maxHealthPoints;
                        }
                        protected void increaseStrength(int amount) {
                            strength += amount;
                        }
                        protected void increaseAgility(int amount) {
                            agility += amount;
                        }
}
