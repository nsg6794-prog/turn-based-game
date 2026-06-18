package ui;

import game.EncounterManager;
import game.Main;
import game.Player;
import items.Shop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApplication extends Application {
    static final int WINDOW_WIDTH = 800;
    static final int WINDOW_HEIGHT = 600;


    @Override
    public void start(Stage stage) {
        MainMenu mainMenu = new MainMenu();

        stage.setTitle("Turn Based Game");
        showScene(stage, mainMenu.createMenuScene(stage));
    }

    public static void startGame(Stage stage) {
        Player player = Main.createPlayer();
        EncounterManager encounterManager = new EncounterManager();

        showCombatScreen(stage, player, encounterManager);
    }

    static void showCombatScreen(Stage stage, Player player, EncounterManager encounterManager) {
        Scene[] combatScene = new Scene[1];
        CombatScreen[] combatScreenReference = new CombatScreen[1];
        CombatScreen combatScreen = new CombatScreen(
                player,
                encounterManager,
                () -> showShopScreen(stage, player, encounterManager, () -> {
                    combatScreenReference[0].restoreAfterInventory();
                    showScene(stage, combatScene[0]);
                }),
                () -> {
                    encounterManager.moveToNextEncounter();
                    showCombatScreen(stage, player, encounterManager);
                },
                () -> showInventoryScreen(stage, player, () -> {
                    combatScreenReference[0].restoreAfterInventory();
                    showScene(stage, combatScene[0]);
                }),
                () -> showLevelUpRewardScreen(stage, player, () -> {
                    combatScreenReference[0].restoreAfterLevelUp();
                    showScene(stage, combatScene[0]);
                }),
                () -> showMainMenu(stage));
        combatScreenReference[0] = combatScreen;
        combatScene[0] = new Scene(combatScreen, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("Game Name");
        showScene(stage, combatScene[0]);
    }

    private static void showShopScreen(Stage stage,
                                       Player player,
                                       EncounterManager encounterManager,
                                       Runnable returnToCombat) {
        Shop shop = new Shop();
        shop.initializeStock();
        showScene(stage, new Scene(new ShopScreen(player, shop, stage, encounterManager, returnToCombat),
                WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    private static void showInventoryScreen(Stage stage, Player player, Runnable returnToCombat) {
        showScene(stage, new Scene(new InventoryUI(player, returnToCombat), WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    private static void showLevelUpRewardScreen(Stage stage, Player player, Runnable returnToCombat) {
        LevelUpRewardScreen rewardScreen = new LevelUpRewardScreen(player, returnToCombat);
        showScene(stage, new Scene(rewardScreen, WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    public static void showMainMenu(Stage stage) {
        MainMenu mainMenu = new MainMenu();

        stage.setTitle("Turn Based Game");
        showScene(stage, mainMenu.createMenuScene(stage));
    }

    static void showScene(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.setFullScreenExitHint("");
        if (!stage.isShowing()) {
            stage.show();
        }
        enforceFullScreen(stage);
    }

    private static void enforceFullScreen(Stage stage) {
        stage.setMaximized(true);
        stage.setFullScreen(true);

        Platform.runLater(() -> {
            stage.setMaximized(true);
            stage.setFullScreen(true);

            Platform.runLater(() -> {
                stage.setMaximized(true);
                stage.setFullScreen(true);
            });
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
