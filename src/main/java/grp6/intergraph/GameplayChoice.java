package grp6.intergraph;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Classe GameplayChoice
 * Cette classe gere l'affichage du choix du mode de jeu.
 * Elle permet de selectionner entre le mode Apprentissage et le mode Libre.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 * @see ProfileButton
 * @see StyledContent
 * @see SudokuMenu
 * @see MainMenu
 */
public class GameplayChoice {
    
    /**
     * Affiche la fenetre de selection du mode de jeu.
     * 
     * @param stage Fenetre principale de l'application [Stage]
     */
    public static void showGameplayChoice(Stage stage) {

        VBox layout = new VBox(30);
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(640, 480);

        Label choiceLabel = new Label("Choix du mode");
        choiceLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Choix du jeu sur une ligne verticale
        VBox gameMode = new VBox(20);
        gameMode.setAlignment(Pos.CENTER);

        Button learningMode = new Button("Mode Apprentissage");
        Button freeMode = new Button("Mode Libre");
        Button exitButton = new Button("Retour");

        // Appliquer le style SudokuBox sur les boutons learningMode et freeMode
        StyledContent.applyButtonBoxStyle(learningMode);
        StyledContent.applyButtonBoxStyle(freeMode);

        // Appliquer le style classique sur le bouton exitButton
        StyledContent.applyButtonStyle(exitButton);

        learningMode.setOnAction(e -> System.out.println("Parametres ouverts"));
        freeMode.setOnAction(e -> SudokuMenu.showSudokuLibrary(stage));
        exitButton.setOnAction(e -> MainMenu.showMainMenu(stage, MainMenu.getProfile()));

        gameMode.getChildren().addAll(freeMode, learningMode);

        layout.getChildren().addAll(choiceLabel, gameMode, exitButton);

        Scene scene = new Scene(layout, 640, 480);
        stage.setTitle("Choix Mode - " + MainMenu.getProfileName());
        stage.setScene(scene);
        stage.show();
    }
}