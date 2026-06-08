package ui;

import game.GameCharacter;
import game.Main;

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
        GameCharacter player = Main.createPlayer();
        GameCharacter enemy = Main.createEnemy();

        CombatScreen combatScreen = new CombatScreen(player, enemy, () -> showMainMenu(stage));
        Scene scene = new Scene(combatScreen, WINDOW_WIDTH, WINDOW_HEIGHT);
        
        stage.setTitle("Game Name");
        stage.setScene(scene);
        stage.show();
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
