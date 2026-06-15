package ui;

import game.EncounterManager;
import game.Main;
import game.Player;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApplication extends Application {
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;


    @Override
    public void start(Stage stage) {
        MainMenu mainMenu = new MainMenu();

        stage.setTitle("Turn Based Game");
        stage.setScene(mainMenu.createMenuScene(stage, this));
        stage.show();
    }

    public void startGame(Stage stage) {
        Player player = Main.createPlayer();
        EncounterManager encounterManager = new EncounterManager();

        showCombatScreen(stage, player, encounterManager);
    }

    private void showCombatScreen(Stage stage, Player player, EncounterManager encounterManager) {
        Scene[] combatScene = new Scene[1];
        CombatScreen[] combatScreenReference = new CombatScreen[1];
        CombatScreen combatScreen = new CombatScreen(
                player,
                encounterManager,
                () -> showCombatScreen(stage, player, encounterManager),
                () -> showLevelUpRewardScreen(stage, player, () -> {
                    combatScreenReference[0].restoreAfterLevelUp();
                    stage.setScene(combatScene[0]);
                }),
                () -> showMainMenu(stage));
        combatScreenReference[0] = combatScreen;
        combatScene[0] = new Scene(combatScreen, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("Game Name");
        stage.setScene(combatScene[0]);
        stage.show();
    }

    private void showLevelUpRewardScreen(Stage stage, Player player, Runnable returnToCombat) {
        LevelUpRewardScreen rewardScreen = new LevelUpRewardScreen(player, returnToCombat);
        stage.setScene(new Scene(rewardScreen, WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    public void showMainMenu(Stage stage) {
        MainMenu mainMenu = new MainMenu();

        stage.setTitle("Turn Based Game");
        stage.setScene(mainMenu.createMenuScene(stage, this));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
