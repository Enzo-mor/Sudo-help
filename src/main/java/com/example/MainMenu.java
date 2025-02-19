package com.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {
    private static String profile = null;

    public static void showMainMenu(Stage stage, String profileName) {
        profile = profileName;

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Bienvenue, " + profile);
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button startGameButton = new ProfileButton("Jouer");
        startGameButton.setOnAction(e -> SudokuMenu.showSudokuLibrary(stage));

        Button settingsButton = new ProfileButton("Paramètres");
        settingsButton.setOnAction(e -> System.out.println("Paramètres ouverts"));

        Button exitButton = new ProfileButton("Quitter");
        exitButton.setOnAction(e -> stage.close());

        layout.getChildren().addAll(welcomeLabel, startGameButton, settingsButton, exitButton);

        Scene scene = new Scene(layout, 640, 480);
        stage.setTitle("Menu Principal");
        stage.setScene(scene);
        stage.show();
    }

    public static String getProfileName() {
        return profile;
    }
}
