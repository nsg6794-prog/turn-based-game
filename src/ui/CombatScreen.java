package ui;

import combat.Battle;
import game.GameCharacter;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CombatScreen extends VBox {
    private final GameCharacter player;
    private final GameCharacter enemy;
    private final Battle battle;
    private final Label enemyLabel = new Label();
    private final Label playerLabel = new Label();
    private final Label combatLog = new Label();
    private final Button attackButton = new Button("Attack");
    private final Button healButton = new Button("Heal");

    public CombatScreen(GameCharacter player, GameCharacter enemy) {
        super(10);
        this.player = player;
        this.enemy = enemy;
        this.battle = new Battle(player, enemy, () -> 1);

        combatLog.setWrapText(true);

        attackButton.setOnAction(event -> playerAttack());
        healButton.setOnAction(event -> playerHeal());

        getChildren().addAll(enemyLabel, combatLog, playerLabel, attackButton, healButton);
        updateHealthLabels();
        combatLog.setText("A wild " + enemy.getName() + " appears!");
    }

    private void playerAttack() {
        int damage = battle.attack(player, enemy);
        String message = player.getName() + " attacked " + enemy.getName() + " for " + damage + " damage!";

        if (enemy.isAlive()) {
            message += "\n" + enemyTurn();
        }

        endTurn(message);
    }

    private void playerHeal() {
        int healthBeforeHeal = player.getHealthpoints();
        battle.healUp(player);
        int healthRecovered = player.getHealthpoints() - healthBeforeHeal;
        endTurn(player.getName() + " healed for " + healthRecovered + " HP!\n" + enemyTurn());
    }

    private String enemyTurn() {
        int enemyChoice = (int) (Math.random() * 2) + 1;

        if (enemyChoice == 1) {
            int damage = battle.attack(enemy, player);
            return enemy.getName() + " attacked " + player.getName() + " for " + damage + " damage!";
        }

        int healthBeforeHeal = enemy.getHealthpoints();
        battle.healUp(enemy);
        int healthRecovered = enemy.getHealthpoints() - healthBeforeHeal;
        return enemy.getName() + " healed for " + healthRecovered + " HP!";
    }

    private void endTurn(String message) {
        updateHealthLabels();

        if (!player.isAlive()) {
            message += "\nDefeat!";
            setActionsDisabled(true);
        } else if (!enemy.isAlive()) {
            message += "\nVictory!";
            setActionsDisabled(true);
        }

        combatLog.setText(message);
    }

    private void updateHealthLabels() {
        enemyLabel.setText(enemy.getName() + " HP: " + enemy.getHealthpoints());
        playerLabel.setText(player.getName() + " HP: " + player.getHealthpoints());
    }

    private void setActionsDisabled(boolean disabled) {
        attackButton.setDisable(disabled);
        healButton.setDisable(disabled);
    }
}
