package ui;

import game.CharacterConfig;
import game.Main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApplication extends Application {
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;

    @Override
    public void start(Stage stage) {
        CharacterConfig player = Main.createHero();
        CharacterConfig enemy = Main.createVillain();

        CombatScreen combatScreen = new CombatScreen(player, enemy);
        Scene scene = new Scene(combatScreen, WINDOW_WIDTH, WINDOW_HEIGHT);

        stage.setTitle("Roguelike");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
