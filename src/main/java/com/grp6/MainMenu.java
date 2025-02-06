package com.grp6;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {

    public MainMenu() {

    }
    

    public void show(Stage stage) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Bienvenue, " + Main.selectedProfile);
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button startGameButton = new Button("Jouer");
        startGameButton.setOnAction(e -> Main.showSudokuLibrary(stage));

        Button settingsButton = new Button("Paramètres");
        settingsButton.setOnAction(e -> System.out.println("Paramètres ouverts"));

        Button exitButton = new Button("Quitter");
        exitButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(welcomeLabel, startGameButton, settingsButton, exitButton);

        Scene scene = new Scene(layout, 640, 480);
        stage.setTitle("Menu Principal");
        stage.setScene(scene);
        stage.show();
    }
}
