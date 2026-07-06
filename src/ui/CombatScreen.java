package ui;

import combat.ActionEconomy;
import combat.Battle;
import game.Enemy;
import game.EncounterManager;
import game.Player;
import items.HPPot;
import spells.Spell;
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
    private final ActionEconomy actionEconomy = new ActionEconomy();
    private final PlayerStatsPanel playerStatsPanel;
    private final Label enemyLabel = new Label();
    private final Label playerLabel = new Label();
    private final Label combatLog = new Label();
    private final Label actionEconomyLabel = new Label();
    private final Label rewardExperienceLabel = new Label();
    private final Label rewardGoldLabel = new Label();
    private final HBox rewardRow = new HBox(8);
    private final Button attackButton = new Button("Attack");
    private final Button spellsButton = new Button("Spells");
    private final Button endTurnButton = new Button("End Turn");
    private final Button inventoryButton = new Button("Go to Inventory");
    private final Button visitShopButton = new Button("Visit Shop");
    private final Button nextEncounterButton = new Button("Next Encounter");
    private final Button returnMenuButton = new Button("Return to Menu");
    private final VBox spellListPanel = new VBox(6);
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
        this.actionEconomy.startTurn();

        setAlignment(Pos.TOP_CENTER);
        enemyLabel.setTextAlignment(TextAlignment.CENTER);
        playerLabel.setTextAlignment(TextAlignment.CENTER);
        actionEconomyLabel.setTextAlignment(TextAlignment.CENTER);
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
        spellsButton.setOnAction(event -> showSpellList());
        endTurnButton.setOnAction(event -> playerEndsTurn());
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
        spellListPanel.setAlignment(Pos.CENTER);
        spellListPanel.setPadding(new Insets(8));
        spellListPanel.setVisible(false);
        spellListPanel.setManaged(false);

        VBox statsAndInventory = new VBox(5, playerStatsPanel, inventoryButton);
        statsAndInventory.setAlignment(Pos.TOP_LEFT);
        statsAndInventory.setPadding(new Insets(0, 0, 0, 10));
        HBox topBar = new HBox(statsAndInventory);
        topBar.setAlignment(Pos.TOP_LEFT);
        topBar.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(statsAndInventory, Priority.ALWAYS);

        Region actionSpacer = new Region();
        VBox.setVgrow(actionSpacer, Priority.ALWAYS);

        VBox actionBox = new VBox(8,
                actionEconomyLabel,
                attackButton,
                spellsButton,
                spellListPanel,
                endTurnButton,
                rewardRow,
                visitShopButton,
                nextEncounterButton,
                returnMenuButton);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.setPadding(new Insets(0, 0, 36, 0));

        getChildren().addAll(topBar, enemyLabel, combatLog, playerLabel, actionSpacer, actionBox);
        updateHealthLabels();
        updateActionControls();
        combatLog.setText("A wild " + enemy.getName() + " appears!");
    }

    private void playerAttack() {
        if (!actionEconomy.spendActionPoint()) {
            updateActionControls();
            return;
        }

        int damage = battle.attack(player, enemy);
        String message = player.getName() + " attacked " + enemy.getName() + " for " + damage + " damage!";

        if (enemy.isAlive() && actionEconomy.isTurnFinished()) {
            message += "\n" + enemyTurn();
            startNewPlayerTurnIfCombatContinues();
        }

        resolveCombatState(message);
    }

    private void playerEndsTurn() {
        actionEconomy.endTurn();
        String message = "";

        if (enemy.isAlive() && actionEconomy.isTurnFinished()) {
            message = enemyTurn();
            startNewPlayerTurnIfCombatContinues();
        }

        resolveCombatState(message);
    }

    String usePotionFromInventory(HPPot potion) {
        if (!actionEconomy.spendBonusActionPoint()) {
            updateActionControls();
            return "No bonus action left.";
        }

        potion.consume(player);
        player.getInventory().removeItem(potion);

        String message = "Used " + potion.getName();
        if (enemy.isAlive() && actionEconomy.isTurnFinished()) {
            message += "\n" + enemyTurn();
            startNewPlayerTurnIfCombatContinues();
        }

        resolveCombatState(message);
        return message;
    }

    private void showSpellList() {
        refreshSpellList();
        setSpellListVisible(true);
    }

    private void closeSpellList() {
        setSpellListVisible(false);
    }

    private void setSpellListVisible(boolean visible) {
        spellListPanel.setVisible(visible);
        spellListPanel.setManaged(visible);
    }

    private void refreshSpellList() {
        spellListPanel.getChildren().clear();

        Label titleLabel = new Label("Spells");
        spellListPanel.getChildren().add(titleLabel);

        if (player.getKnownSpells().isEmpty()) {
            spellListPanel.getChildren().add(new Label("No spells known."));
        } else {
            for (Spell spell : player.getKnownSpells()) {
                spellListPanel.getChildren().add(createSpellRow(spell));
            }
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> closeSpellList());
        spellListPanel.getChildren().add(backButton);
    }

    private HBox createSpellRow(Spell spell) {
        Label spellLabel = new Label(spell.getName() + formatSpellCost(spell));
        Button castButton = new Button("Cast");
        castButton.setDisable(!isCombatActive() || !canAffordSpell(spell));
        castButton.setOnAction(event -> castSpell(spell));

        HBox spellRow = new HBox(10, spellLabel, castButton);
        spellRow.setAlignment(Pos.CENTER);
        return spellRow;
    }

    private String formatSpellCost(Spell spell) {
        return " (AP: " + spell.getActionPointCost()
                + ", Bonus AP: " + spell.getBonusActionPointCost() + ")";
    }

    private boolean canAffordSpell(Spell spell) {
        return actionEconomy.getActionPoints() >= spell.getActionPointCost()
                && actionEconomy.getBonusActionPoints() >= spell.getBonusActionPointCost();
    }

    private void castSpell(Spell spell) {
        if (actionEconomy.getActionPoints() < spell.getActionPointCost()) {
            combatLog.setText("Not enough action points.");
            refreshSpellList();
            updateActionControls();
            return;
        }

        if (actionEconomy.getBonusActionPoints() < spell.getBonusActionPointCost()) {
            combatLog.setText("Not enough bonus action points.");
            refreshSpellList();
            updateActionControls();
            return;
        }

        spendSpellCosts(spell);
        int enemyHealthBeforeCast = enemy.getHealthpoints();
        spell.cast(player, enemy);
        int damageTaken = Math.max(0, enemyHealthBeforeCast - enemy.getHealthpoints());
        closeSpellList();

        String message = player.getName() + " casts " + spell.getName() + " on " + enemy.getName()
                + " for " + damageTaken + " damage.";
        if (enemy.getHealthpoints() > 0 && actionEconomy.isTurnFinished()) {
            message += "\n" + enemyTurn();
            startNewPlayerTurnIfCombatContinues();
        }

        resolveCombatState(message);
    }

    private void spendSpellCosts(Spell spell) {
        for (int i = 0; i < spell.getActionPointCost(); i++) {
            actionEconomy.spendActionPoint();
        }

        for (int i = 0; i < spell.getBonusActionPointCost(); i++) {
            actionEconomy.spendBonusActionPoint();
        }
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

    private void startNewPlayerTurnIfCombatContinues() {
        if (player.isAlive() && enemy.isAlive()) {
            actionEconomy.startTurn();
        }
    }

    private void resolveCombatState(String message) {
        updateHealthLabels();
        boolean levelUpRewardAvailable = false;

        if (!player.isAlive()) {
            message += "\nDefeat!";
            setActionsDisabled(true);
        } else if (enemy.getHealthpoints() <= 0) {
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
        updateActionControls();
        combatLog.setText(message);

        if (levelUpRewardAvailable) {
            Platform.runLater(showLevelUpRewards);
        }
    }

    void restoreAfterLevelUp() {
        updateHealthLabels();
        playerStatsPanel.refresh();
        updateActionControls();
        updatePostVictoryControls();
    }

    void restoreAfterInventory() {
        updateHealthLabels();
        playerStatsPanel.refresh();
        updateActionControls();
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
        if (disabled) {
            attackButton.setDisable(true);
            spellsButton.setDisable(true);
            endTurnButton.setDisable(true);
            closeSpellList();
            return;
        }

        updateActionControls();
    }

    private void updateActionControls() {
        actionEconomyLabel.setText("Action Points: " + actionEconomy.getActionPoints()
                + " | Bonus Action Points: " + actionEconomy.getBonusActionPoints());

        boolean combatActive = isCombatActive();
        attackButton.setDisable(!combatActive || !actionEconomy.hasActionPoints());
        spellsButton.setDisable(!combatActive || player.getKnownSpells().isEmpty());
        endTurnButton.setDisable(!combatActive);

        if (spellListPanel.isManaged()) {
            refreshSpellList();
        }
    }

    private boolean isCombatActive() {
        return player.isAlive() && enemy.isAlive();
    }
}
