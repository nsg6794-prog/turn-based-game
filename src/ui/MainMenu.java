package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class MainMenu {
    public Scene createMenuScene(Stage stage) {
            
        Button startButton = new Button("Start new Game");
        Button loadButton = new Button("Load Game");
        Button exitButton = new Button("Quit");

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
        menuLayout.setAlignment(Pos.CENTER);
        return new Scene(menuLayout, 400, 300);
            
            
    
           
    }

}
