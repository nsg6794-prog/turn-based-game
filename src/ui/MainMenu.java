package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class MainMenu {
    private static final double BUTTON_WIDTH = 280;
    private static final double BUTTON_HEIGHT = 64;

    public Scene createMenuScene(Stage stage) {
            
        Button startButton = new Button("Start new Game");
        Button loadButton = new Button("Load Game");
        Button exitButton = new Button("Quit");
        configureMenuButton(startButton);
        configureMenuButton(loadButton);
        configureMenuButton(exitButton);

        startButton.setOnAction(e -> {
            GameApplication.startGame(stage);
        });
        
        loadButton.setOnAction(e -> {
            // Implement load game functionality
        });
        exitButton.setOnAction(e -> {
            stage.close();  
        });
        VBox menuLayout = new VBox(20);
        menuLayout.getChildren().addAll(startButton, loadButton, exitButton);
        menuLayout.setAlignment(Pos.CENTER_LEFT);
        menuLayout.setPadding(new Insets(0, 0, 0, 96));
        return new Scene(menuLayout, 800, 600);
            
            
    
           
    }

    private void configureMenuButton(Button button) {
        button.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setMinSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        button.setStyle("-fx-font-size: 22px;");
    }
}
