package combat;
import game.GameCharacter;
import game.PlayerInput;
public class Battle {
   
    private GameCharacter player;
    private GameCharacter enemy;
    private PlayerInput input;

    public Battle(GameCharacter player, GameCharacter enemy, PlayerInput input) {
        this.player = player;
        this.enemy = enemy;
        this.input = input;
    }
    public void startBattle(){
        int playerInitiative = player.inititive();
        int enemyInitiative = enemy.inititive();
        boolean playerGoesFirst = playerInitiative >= enemyInitiative;

        System.out.println(player.getName() + " Rolled " + playerInitiative);
        System.out.println(enemy.getName() + " Rolled " + enemyInitiative);
        if(playerGoesFirst) {
            System.out.println(player.getName() + " goes first!");
        } else {
            System.out.println(enemy.getName() + " goes first!");
        }

        while (player.isAlive() && enemy.isAlive()) {
            System.out.println(player.getName() + ": " +
                   player.getHealthpoints() + " HP");

            System.out.println(enemy.getName() + ": " +
                   enemy.getHealthpoints() + " HP");

            if (playerGoesFirst) {
                playerTurn();

                if (!enemy.isAlive()) {
                    System.out.println("Victory!");
                    break;
                }

                enemyTurn();
            } else {
                enemyTurn();

                if (!player.isAlive()) {
                    System.out.println("Defeat!");
                    break;
                }

                playerTurn();
            }
        }
    }
    public int calculateDamage(GameCharacter attacker) {
        int strengthBonus = attacker.getStrength() / 5;
        int damageRoll = (int)(Math.random() * 8) + 1;
        return strengthBonus + damageRoll;
    }
    public int attack(GameCharacter attacker, GameCharacter defender) {
        int damage = calculateDamage(attacker);
        int damageDealt = defender.takeDamage(damage);
        System.out.println(attacker.getName() + " attacks " + defender.getName() + " for " + damageDealt + " damage!");
        if (!defender.isAlive()) {
            System.out.println(defender.getName() + " has been defeated!");
        }
        return damageDealt;
    }
    public int healUp(GameCharacter healed) {
        healed.heal(10);
        System.out.println(healed.getName() + " heals for 10 health points!");
        return healed.getHealthpoints();
    }
    public void playerTurn() {
        System.out.println("\n" + player.getName() + "'s turn:");
        int choice = input.chooseAction();

        if (choice == 1) {
            attack(player, enemy);
        } else if (choice == 2) {
            healUp(player);
        }
    }
    public void enemyTurn() {
        System.out.println("\n" + enemy.getName() + "'s turn:");
        int choice = (int)(Math.random() * 2) + 1;

        if (choice == 1) {
            attack(enemy, player);
        } else if (choice == 2) {
            healUp(enemy);
        }
    }
}
