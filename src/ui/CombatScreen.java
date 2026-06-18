package ui;

import combat.Battle;
import game.Enemy;
import game.EncounterManager;
import game.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
    private final Label rewardExperienceLabel = new Label();
    private final Label rewardGoldLabel = new Label();
    private final HBox rewardRow = new HBox(8);
    private final Button attackButton = new Button("Attack");
    private final Button inventoryButton = new Button("Go to Inventory");
    private final Button visitShopButton = new Button("Visit Shop");
    private final Button nextEncounterButton = new Button("Next Encounter");
    private final Button returnMenuButton = new Button("Return to Menu");
    private final Runnable returnToMenu;
    private final Runnable showLevelUpRewards;
    private boolean victoryHandled;
    

    public CombatScreen(Player player,
                        EncounterManager encounterManager,
                        Runnable showShop,
                        Runnable loadNextEncounter,
                        Runnable showInventory,
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
        rewardRow.setAlignment(Pos.CENTER);
        rewardRow.getChildren().addAll(rewardExperienceLabel, rewardGoldLabel);
        rewardRow.setVisible(false);
        rewardRow.setManaged(false);
        ImageAssets.applyAttackButtonGraphic(attackButton);

        attackButton.setOnAction(event -> playerAttack());
        inventoryButton.setOnAction(event -> showInventory.run());
        visitShopButton.setOnAction(event -> {
            showShop.run();
        });
        nextEncounterButton.setOnAction(event -> {
            playerStatsPanel.dispose();
            loadNextEncounter.run();
        });
        returnMenuButton.setOnAction(event -> {
            playerStatsPanel.dispose();
            this.returnToMenu.run();
        });
        visitShopButton.setVisible(false);
        visitShopButton.setManaged(false);
        nextEncounterButton.setVisible(false);
        nextEncounterButton.setManaged(false);

        VBox statsAndInventory = new VBox(5, playerStatsPanel, inventoryButton);
        statsAndInventory.setAlignment(Pos.TOP_LEFT);
        statsAndInventory.setPadding(new Insets(0, 0, 0, 10));
        HBox topBar = new HBox(statsAndInventory);
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(statsAndInventory, Priority.ALWAYS);

        Region actionSpacer = new Region();
        VBox.setVgrow(actionSpacer, Priority.ALWAYS);

        VBox actionBox = new VBox(8, attackButton, rewardRow, visitShopButton, nextEncounterButton, returnMenuButton);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setPadding(new Insets(0, 0, 36, 0));

        getChildren().addAll(topBar, enemyLabel, combatLog, playerLabel, actionSpacer, actionBox);
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

            message += "\n\nVictory!";
            rewardExperienceLabel.setText("Rewards: " + enemy.getExperienceReward() + " XP and");
            ImageAssets.setGoldAmount(rewardGoldLabel, enemy.getGoldReward());
            rewardRow.setVisible(true);
            rewardRow.setManaged(true);
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

    void restoreAfterInventory() {
        updateHealthLabels();
        playerStatsPanel.refresh();
        updatePostVictoryControls();
    }

    private void updatePostVictoryControls() {
        boolean shopAvailable = !enemy.isAlive() && encounterManager.hasNextEncounter();
        visitShopButton.setVisible(shopAvailable);
        visitShopButton.setManaged(shopAvailable);
        visitShopButton.setDisable(!shopAvailable);
        nextEncounterButton.setVisible(shopAvailable);
        nextEncounterButton.setManaged(shopAvailable);
        nextEncounterButton.setDisable(!shopAvailable);
    }

    private void updateHealthLabels() {
        enemyLabel.setText(enemy.getName() + " HP: " + enemy.getHealthpoints());
        playerLabel.setText(player.getName() + " HP: " + player.getHealthpoints());
    }

    private void setActionsDisabled(boolean disabled) {
        attackButton.setDisable(disabled);
    }
}
