package ui;

import combat.Battle;
import game.Enemy;
import game.EncounterManager;
import game.Player;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

public class CombatScreen extends VBox {
    private final Player player;
    private final Enemy enemy;
    private final EncounterManager encounterManager;
    private final Battle battle;
    private final Label enemyLabel = new Label();
    private final Label playerLabel = new Label();
    private final Label combatLog = new Label();
    private final Button attackButton = new Button("Attack");
    private final Button healButton = new Button("Heal");
    private final Button nextEncounterButton = new Button("Next Encounter");
    private final Button returnMenuButton = new Button("Return to Menu");
    private final Runnable returnToMenu;
    private final Runnable showLevelUpRewards;
    private boolean victoryHandled;

    public CombatScreen(Player player,
                        EncounterManager encounterManager,
                        Runnable loadNextEncounter,
                        Runnable showLevelUpRewards,
                        Runnable returnToMenu) {
        super(10);
        this.player = player;
        this.encounterManager = encounterManager;
        this.enemy = encounterManager.getCurrentEnemy();
        this.returnToMenu = returnToMenu;
        this.showLevelUpRewards = showLevelUpRewards;
        this.battle = new Battle(player, this.enemy, () -> 1);

        combatLog.setWrapText(true);

        attackButton.setOnAction(event -> playerAttack());
        healButton.setOnAction(event -> playerHeal());
        nextEncounterButton.setOnAction(event -> {
            encounterManager.moveToNextEncounter();
            loadNextEncounter.run();
        });
        returnMenuButton.setOnAction(event -> this.returnToMenu.run());
        nextEncounterButton.setVisible(false);
        nextEncounterButton.setManaged(false);

        getChildren().addAll(enemyLabel, combatLog, playerLabel, attackButton, healButton,
                nextEncounterButton, returnMenuButton);
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
        battle.heal(Battle.PLAYER_HEAL_AMOUNT, player);
        int healthRecovered = player.getHealthpoints() - healthBeforeHeal;
        endTurn(player.getName() + " healed for " + healthRecovered + " HP!\n" + enemyTurn());
    }

    private String enemyTurn() {
        int enemyChoice = (int) (Math.random() * 2) + 1;

        if (enemyChoice == 1 || enemy.getHealthpoints() == enemy.getMaxHealthpoints()) {
            int damage = battle.attack(enemy, player);
            return enemy.getName() + " attacked " + player.getName() + " for " + damage + " damage!";
        }

        int healthBeforeHeal = enemy.getHealthpoints();
        battle.heal(Battle.ENEMY_HEAL_AMOUNT, enemy);
        int healthRecovered = enemy.getHealthpoints() - healthBeforeHeal;
        return enemy.getName() + " healed for " + healthRecovered + " HP!";
    }

    private void endTurn(String message) {
        updateHealthLabels();

        if (!player.isAlive()) {
            message += "\nDefeat!";
            setActionsDisabled(true);
        } else if (!enemy.isAlive()) {
            if (!victoryHandled) {
                battle.awardVictoryRewards();
                victoryHandled = true;

                if (player.hasPendingLevelUpReward()) {
                    Platform.runLater(showLevelUpRewards);
                }
            }

            message += "\nVictory!"
                    + "\nRewards: " + enemy.getExperienceReward() + " XP and "
                    + enemy.getGoldReward() + " gold.";
            setActionsDisabled(true);

            if (encounterManager.hasNextEncounter()) {
                nextEncounterButton.setVisible(true);
                nextEncounterButton.setManaged(true);
            } else {
                message += "\nAll encounters complete!";
            }
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
