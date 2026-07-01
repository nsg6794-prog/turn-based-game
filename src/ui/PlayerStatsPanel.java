package ui;

import game.Player;
import items.Weapon;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class PlayerStatsPanel extends GridPane {
    private static final int STATS_FONT_SIZE = 18;
    private static final int STATS_GOLD_ICON_SIZE = 27;

    private final Player player;
    private final Label levelValue = new Label();
    private final Label experienceValue = new Label();
    private final Label goldValue = new Label();
    private final Label strengthValue = new Label();
    private final Label agilityValue = new Label();
    private final Label intelligenceValue = new Label();
    private final Label weaponValue = new Label();
    private final Runnable refreshListener = this::refreshOnFxThread;

    public PlayerStatsPanel(Player player) {
        this.player = player;

        levelValue.setId("player-level");
        experienceValue.setId("player-experience");
        goldValue.setId("player-gold");
        strengthValue.setId("player-strength");
        agilityValue.setId("player-agility");
        intelligenceValue.setId("player-intelligence");
        weaponValue.setId("player-weapon");

        setHgap(15);
        setVgap(8);
        setPadding(new Insets(15));
        setStyle("-fx-font-size: " + STATS_FONT_SIZE + "px;");

        addRow(0, new Label("Level:"), levelValue);
        addRow(1, new Label("Current XP:"), experienceValue);
        addRow(2, new Label("Gold:"), goldValue);
        addRow(3, new Label("Strength:"), strengthValue);
        addRow(4, new Label("Agility:"), agilityValue);
        addRow(5, new Label("Intelligence:"), intelligenceValue);
        add(weaponValue, 0, 6, 2, 1);

        sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (oldScene != null) {
                player.removeStatsChangeListener(refreshListener);
            }
            if (newScene != null) {
                player.addStatsChangeListener(refreshListener);
                refresh();
            }
        });

        refresh();
    }

    public void refresh() {
        levelValue.setText(Integer.toString(player.getLevel()));
        experienceValue.setText(Integer.toString(player.getExperience()));
        ImageAssets.setGoldAmount(goldValue, player.getGold(), STATS_GOLD_ICON_SIZE);
        strengthValue.setText(Integer.toString(player.getStrength()));
        agilityValue.setText(Integer.toString(player.getAgility()));
        intelligenceValue.setText(Integer.toString(player.getIntelligence()));
        weaponValue.setText(formatEquippedWeapon());
    }

    public void dispose() {
        player.removeStatsChangeListener(refreshListener);
    }

    private void refreshOnFxThread() {
        if (Platform.isFxApplicationThread()) {
            refresh();
        } else {
            Platform.runLater(this::refresh);
        }
    }

    private String formatEquippedWeapon() {
        Weapon weapon = player.getEquippedWeapon();
        if (weapon == null) {
            return "Weapon: None";
        }

        return "Weapon: " + weapon.getName() + " (+" + weapon.getDamage() + " damage)";
    }
}
