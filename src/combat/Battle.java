package combat;
import game.Enemy;
import game.GameCharacter;
import game.Player;
import game.PlayerInput;
public class Battle {
    public static final int PLAYER_HEAL_AMOUNT = 10;
    public static final int ENEMY_HEAL_AMOUNT = 5;
   
    private Player player;
    private Enemy enemy;
    private PlayerInput input;

    public Battle(Player player, Enemy enemy, PlayerInput input) {
        this.player = player;
        this.enemy = enemy;
        this.input = input;
    }
    public void startBattle(){
        int playerInitiative = player.rollInitiative();
        int enemyInitiative = enemy.rollInitiative();
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
        if (player.isAlive() && !enemy.isAlive()) {
            player.gainExperience(enemy.getExperienceReward());
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
    public int heal(int healAmount, GameCharacter target) {
        target.heal(healAmount);
        System.out.println(target.getName() + " heals for " + healAmount + " health points!");
        return target.getHealthpoints();
    }
    public void playerTurn() {
        System.out.println("\n" + player.getName() + "'s turn:");
        int choice = input.chooseAction();

        if (choice == 1) {
            attack(player, enemy);
        } else if (choice == 2) {
            heal(PLAYER_HEAL_AMOUNT, player);
        }
    }
    public void enemyTurn() {
        System.out.println("\n" + enemy.getName() + "'s turn:");
        int choice = (int)(Math.random() * 2) + 1;

        if (choice == 1 || enemy.getHealthpoints() == enemy.getMaxHealthpoints()) {
            attack(enemy, player);
        } else if (choice == 2) {
            heal(ENEMY_HEAL_AMOUNT, enemy);
        }
    }
}
