package ui;

import combat.Battle;
import game.Enemy;
import game.EncounterManager;
import game.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.application.Platform;
import javafx.scene.text.TextAlignment;

public class CombatScreen extends VBox {
    private final Player player;
    private final Enemy enemy;
    private final EncounterManager encounterManager;
    private final Battle battle;
    private final PlayerStatsPanel playerStatsPanel;
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
        this.playerStatsPanel = new PlayerStatsPanel(player);

        setAlignment(Pos.TOP_CENTER);
        enemyLabel.setTextAlignment(TextAlignment.CENTER);
        playerLabel.setTextAlignment(TextAlignment.CENTER);
        combatLog.setWrapText(true);
        combatLog.setTextAlignment(TextAlignment.CENTER);
        combatLog.setAlignment(Pos.CENTER);
        combatLog.setMaxWidth(Double.MAX_VALUE);

        attackButton.setOnAction(event -> playerAttack());
        healButton.setOnAction(event -> playerHeal());
        nextEncounterButton.setOnAction(event -> {
            playerStatsPanel.dispose();
            encounterManager.moveToNextEncounter();
            loadNextEncounter.run();
        });
        returnMenuButton.setOnAction(event -> {
            playerStatsPanel.dispose();
            this.returnToMenu.run();
        });
        nextEncounterButton.setVisible(false);
        nextEncounterButton.setManaged(false);

        getChildren().addAll(playerStatsPanel, enemyLabel, combatLog, playerLabel, attackButton, healButton,
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
        boolean levelUpRewardAvailable = false;

        if (!player.isAlive()) {
            message += "\nDefeat!";
            setActionsDisabled(true);
        } else if (!enemy.isAlive()) {
            if (!victoryHandled) {
                battle.awardVictoryRewards();
                victoryHandled = true;
                levelUpRewardAvailable = player.hasPendingLevelUpReward();
            }

            message += "\nVictory!"
                    + "\nRewards: " + enemy.getExperienceReward() + " XP and "
                    + enemy.getGoldReward() + " gold.";
            setActionsDisabled(true);
            updatePostVictoryControls();

            if (!encounterManager.hasNextEncounter()) {
                message += "\nAll encounters complete!";
            }
        }

        playerStatsPanel.refresh();
        combatLog.setText(message);

        if (levelUpRewardAvailable) {
            Platform.runLater(showLevelUpRewards);
        }
    }

    void restoreAfterLevelUp() {
        updateHealthLabels();
        playerStatsPanel.refresh();
        updatePostVictoryControls();
    }

    private void updatePostVictoryControls() {
        boolean nextEncounterAvailable = !enemy.isAlive() && encounterManager.hasNextEncounter();
        nextEncounterButton.setVisible(nextEncounterAvailable);
        nextEncounterButton.setManaged(nextEncounterAvailable);
        nextEncounterButton.setDisable(!nextEncounterAvailable);
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
