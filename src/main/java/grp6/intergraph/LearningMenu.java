package grp6.intergraph;
import grp6.sudocore.*;
import grp6.syshelp.*;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Classe LearningMenu
 * Cette classe represente le menu o l'utilisateur peut selectionner un Sudoku depuis la bibliotheque pour apprendre une technique.
 * Elle gere l'affichage des grilles, ainsi que les interactions de navigation et les actions possibles sur chaque grille de Sudoku.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 */
public class LearningMenu {
    
    /**
     * Affiche la bibliotheque d'apprentissage contenant les grilles de Sudoku pour apprendre differentes techniques.
     * 
     * @param stage La fenetre principale de l'application ou s'affiche le menu d'apprentissage.
     */
    public static void showLearningLibrairy(Stage stage) {
        
        // Label pour afficher le mode de jeu actuel
        Label learningLabel = new Label("Apprentissage");
        learningLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Label pour afficher le nom des techniques
        Label techniquesLabel = new Label();
        techniquesLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Conteneur pour afficher les grilles de Sudoku contenant les techniques à apprendre
        GridPane techniquesContainer = new GridPane();
        techniquesContainer.setPadding(new Insets(20));
        techniquesContainer.setHgap(15);
        techniquesContainer.setVgap(15);
        techniquesContainer.setAlignment(Pos.CENTER);
        techniquesContainer.setMinWidth(600);
        techniquesContainer.setMinHeight(400);
        
        // Recuperation et initialisation des grilles d'apprentissage
        List<Technique> techs = DBManager.getTechs();
        
        // Affichage des grilles de Sudoku à apprendres
        showLearningSudokuList(techniquesContainer, stage, techs, techniquesLabel);
        
        // Bouton pour retourner au menu principal
        Button backButton = new Button("Retour");
        StyledContent.applyButtonStyle(backButton);
        backButton.setOnAction(e -> GameplayChoice.showGameplayChoice(stage));

        VBox layout = new VBox(15, learningLabel, techniquesContainer, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 900, 600);
        stage.setTitle("Mode Apprentissage - " + MainMenu.getProfileName());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Affiche la liste des grilles de Sudoku disponibles pour l'apprentissage.
     * 
     * @param techniquesContainer Le conteneur ou afficher les grilles de Sudoku.
     * @param stage La fenetre principale de l'application.
     * @param learningSudokus La liste des techniques de Sudoku a afficher.
     * @param techniquesLabel Label affichant le titre de la technique en cours.
     */
    private static void showLearningSudokuList(GridPane techniquesContainer, Stage stage, List<Technique> learningSudokus, Label techniquesLabel) {
        techniquesContainer.getChildren().clear();
        int sudokusPerPage = 12;
        int startIndex = 0;
        int endIndex = Math.min(startIndex + sudokusPerPage, learningSudokus.size());

        int columns = 4;
        for (int i = startIndex; i < endIndex; i++) {
            Technique learningSudoku = learningSudokus.get(i);

            VBox sudokuBox = createLearningSudokuBox(learningSudoku);
            techniquesContainer.add(sudokuBox, (i - startIndex) % columns, (i - startIndex) / columns);
        }
    }

    /**
     * Cree un conteneur affichant une technique d'apprentissage du Sudoku.
     * 
     * @param learningSudoku L'objet representant une technique de Sudoku.
     * @return Un VBox contenant les elements graphiques representant la technique d'apprentissage.
     */
    private static VBox createLearningSudokuBox(Technique learningSudoku) {
        VBox sudokuBox = new VBox(8);
        sudokuBox.setAlignment(Pos.CENTER);
        sudokuBox.setPadding(new Insets(15));
        sudokuBox.setMinSize(125, 125);
        sudokuBox.setMaxSize(150, 150);

        StyledContent.applySudokuBoxStyle(sudokuBox);

        Label nameLabel = new Label(learningSudoku.getName());
        nameLabel.setMinWidth(200);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setWrapText(false);
        nameLabel.setMaxWidth(Region.USE_COMPUTED_SIZE);
        nameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;");

        ImageView statusIcon = new ImageView();
        statusIcon.setFitWidth(28);
        statusIcon.setFitHeight(28);

        if (MainMenu.getProfile().getAlreadyLearn(learningSudoku)) {
            statusIcon.setImage(new Image(SudokuMenu.class.getResourceAsStream("/star.png")));
        }
            
        VBox contentBox = new VBox(5);
        contentBox.setMinWidth(200);
        contentBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(statusIcon);

        sudokuBox.getChildren().addAll(nameLabel, contentBox);

        return sudokuBox;
    }
}
