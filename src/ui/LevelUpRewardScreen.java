package ui;

import game.Player;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class LevelUpRewardScreen extends VBox {
    private final Player player;
    private final Runnable returnToCombat;
    private final Label title = new Label();

    public LevelUpRewardScreen(Player player, Runnable returnToCombat) {
        super(15);
        this.player = player;
        this.returnToCombat = returnToCombat;

        Button healthButton = new Button("Health Points (+10)");
        Button strengthButton = new Button("Strength (+2)");
        Button agilityButton = new Button("Agility (+2)");
        Button intelligenceButton = new Button("Intelligence (+2)");

        healthButton.setOnAction(event -> chooseReward(Player.LevelUpReward.MAX_HEALTH));
        strengthButton.setOnAction(event -> chooseReward(Player.LevelUpReward.STRENGTH));
        agilityButton.setOnAction(event -> chooseReward(Player.LevelUpReward.AGILITY));
        intelligenceButton.setOnAction(event -> chooseReward(Player.LevelUpReward.INTELLIGENCE));

        setAlignment(Pos.CENTER);
        getChildren().addAll(title, healthButton, strengthButton, agilityButton, intelligenceButton);
        updateTitle();
    }

    private void chooseReward(Player.LevelUpReward reward) {
        player.chooseLevelUpReward(reward);

        if (player.hasPendingLevelUpReward()) {
            updateTitle();
        } else {
            returnToCombat.run();
        }
    }

    private void updateTitle() {
        title.setText("Level " + player.getLevel() + " - Choose a stat reward ("
                + player.getPendingLevelUpRewards() + " remaining)");
    }
}
