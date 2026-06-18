package ui;

import game.Player;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class PlayerStatsPanel extends GridPane {
    private final Player player;
    private final Label levelValue = new Label();
    private final Label experienceValue = new Label();
    private final Label goldValue = new Label();
    private final Label strengthValue = new Label();
    private final Label agilityValue = new Label();
    private final Label intelligenceValue = new Label();
    private final Runnable refreshListener = this::refreshOnFxThread;

    public PlayerStatsPanel(Player player) {
        this.player = player;

        levelValue.setId("player-level");
        experienceValue.setId("player-experience");
        goldValue.setId("player-gold");
        strengthValue.setId("player-strength");
        agilityValue.setId("player-agility");
        intelligenceValue.setId("player-intelligence");

        setHgap(10);
        setVgap(5);
        setPadding(new Insets(10));

        addRow(0, new Label("Level:"), levelValue);
        addRow(1, new Label("Current XP:"), experienceValue);
        addRow(2, new Label("Gold:"), goldValue);
        addRow(3, new Label("Strength:"), strengthValue);
        addRow(4, new Label("Agility:"), agilityValue);
        addRow(5, new Label("Intelligence:"), intelligenceValue);

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
        ImageAssets.setGoldAmount(goldValue, player.getGold());
        strengthValue.setText(Integer.toString(player.getStrength()));
        agilityValue.setText(Integer.toString(player.getAgility()));
        intelligenceValue.setText(Integer.toString(player.getIntelligence()));
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
}
