package grp6.intergraph;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class GameplayChoice {

    public static void showGameplayChoice(Stage stage) {
        
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);

        Label choiceLabel = new Label("Choix du mode");
        choiceLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        /* Choix du jeu sur une ligne horizontale */
        HBox gameMode = new HBox(2);
        gameMode.setAlignment(Pos.CENTER);

        Button learningMode = new ProfileButton("Mode Apprentissage");
        learningMode.setOnAction(e -> System.out.println("Paramètres ouverts"));

        Button freeMode = new ProfileButton("Mode Libre");
        freeMode.setOnAction(e -> SudokuMenu.showSudokuLibrary(stage));

        gameMode.getChildren().addAll(learningMode, freeMode);
        /*************/

        Button exitButton = new ProfileButton("Retour");
        exitButton.setOnAction(e -> MainMenu.showMainMenu(stage, MainMenu.getProfileName()));

        layout.getChildren().addAll(choiceLabel, gameMode, exitButton);

        Scene scene = new Scene(layout, 640, 480);
        stage.setTitle("Choix Mode - " + MainMenu.getProfileName());
        stage.setScene(scene);
        stage.show();
    }
}