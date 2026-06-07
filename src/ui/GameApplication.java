package ui;

import game.GameCharacter;
import game.Main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApplication extends Application {
    private static final int WINDOW_WIDTH = 1600;
    private static final int WINDOW_HEIGHT = 1200;

    @Override
    public void start(Stage stage) {
        GameCharacter player = Main.createHero();
        GameCharacter enemy = Main.createVillain();

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
