package grp6.intergraph;
import grp6.sudocore.*;
import grp6.sudocore.SudoTypes.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * Classe SudokuMenu
 * Cette classe represente le menu où l'utilisateur peut selectionner un Sudoku depuis la bibliotheque disponible.
 * Elle gere l'affichage des Sudokus par page, ainsi que les interactions de navigation et les actions possibles sur
 * chaque grille de Sudoku (reprendre ou recommencer un jeu en cours).
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 * @see StyledContent
 * @see SudokuMenu
 * @see DBManager
 * @see Sudoku
 * @see SudokuGame
 */
public class SudokuMenu {

    /**
     * Variable qui garde en memoire la page actuelle du menu de Sudokus.
     */
    private static int currentPage = 0;

    /**
     * Affiche la bibliotheque des grilles de Sudoku disponibles pour l'utilisateur.
     * 
     * @param stage La fenetre principale dans laquelle la bibliotheque est affichee.
     */
    public static void showSudokuLibrary(Stage stage) {
        currentPage = 0;

        // Creation des boutons de navigation
        Button leftArrow = new Button("<");
        leftArrow.setDisable(true);
        Button rightArrow = new Button(">");

        StyledContent.applyArrowButtonStyle(leftArrow);
        StyledContent.applyArrowButtonStyle(rightArrow);

        // Label pour afficher la difficulte
        Label difficultyLabel = new Label();
        difficultyLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Conteneur pour afficher les grilles de Sudoku
        GridPane sudokuContainer = new GridPane();
        sudokuContainer.setPadding(new Insets(20));
        sudokuContainer.setHgap(15);
        sudokuContainer.setVgap(15);
        sudokuContainer.setAlignment(Pos.CENTER);
        sudokuContainer.setMinWidth(600);
        sudokuContainer.setMinHeight(400);

        // Recuperation et initialisation des grilles de Sudoku
        List<Sudoku> sudokus = initializeSudokus();

        // Actions des fleches de navigation
        leftArrow.setOnAction(e -> {
            currentPage--;
            SudokuMenu.showSudokuList(sudokuContainer, stage, sudokus, difficultyLabel, leftArrow, rightArrow);
        });

        rightArrow.setOnAction(e -> {
            currentPage++;
            SudokuMenu.showSudokuList(sudokuContainer, stage, sudokus, difficultyLabel, leftArrow, rightArrow);    
        });

        SudokuMenu.showSudokuList(sudokuContainer, stage, sudokus, difficultyLabel, leftArrow, rightArrow);

        // Mise en page des elements
        HBox navigation = new HBox(20, leftArrow, sudokuContainer, rightArrow);
        navigation.setAlignment(Pos.CENTER);

        Button backButton = new ProfileButton("Retour");
        StyledContent.applyButtonStyle(backButton);
        backButton.setOnAction(e -> GameplayChoice.showGameplayChoice(stage));

        VBox layout = new VBox(15, difficultyLabel, navigation, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 900, 600);
        stage.setTitle("Mode Libre - " + MainMenu.getProfileName());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Cree un conteneur stylise affichant les informations d'un Sudoku specifique.
     * 
     * @param sudoku Le Sudoku a afficher.
     * @return Un conteneur VBox contenant les informations du Sudoku.
     */
    public static VBox createSudokuBox(Sudoku sudoku) {
        VBox sudokuBox = new VBox(8);
        sudokuBox.setAlignment(Pos.CENTER);
        sudokuBox.setPadding(new Insets(15));
        sudokuBox.setMinSize(120, 120);
        sudokuBox.setMaxSize(140, 140);

        StyledContent.applySudokuBoxStyle(sudokuBox);

        Label nameLabel = new Label(sudoku.getName());
        nameLabel.setMinWidth(200);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setWrapText(false);
        nameLabel.setMaxWidth(Region.USE_COMPUTED_SIZE);
        nameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;");

        Label scoreLabel;
        if (sudoku.getGame() != null) {
            if (sudoku.getGame().getGameState() == GameState.FINISHED) {
                scoreLabel = new Label("Score : " + sudoku.getScore());
            } else {
                scoreLabel = new Label(sudoku.getGame().getProgressRate() + " %");
            }
        } else {
            scoreLabel = new Label(" - % ");
        }
        scoreLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        Label bestTimeLabel = new Label("Temps : " + formatTime(sudoku.getBestTime()));
        bestTimeLabel.setAlignment(Pos.CENTER);
        bestTimeLabel.setWrapText(false);
        bestTimeLabel.setMaxWidth(Region.USE_COMPUTED_SIZE);
        bestTimeLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        ImageView statusIcon = new ImageView();
        statusIcon.setFitWidth(28);
        statusIcon.setFitHeight(28);

        switch (sudoku.getStatus()) {
            case FINISHED -> statusIcon.setImage(new Image(SudokuMenu.class.getResourceAsStream("/star.png")));
            case IN_PROGRESS -> statusIcon.setImage(new Image(SudokuMenu.class.getResourceAsStream("/pause.png")));
            default -> statusIcon.setVisible(false);
        }

        VBox contentBox = new VBox(5);
        contentBox.setMinWidth(200);
        contentBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(statusIcon, scoreLabel, bestTimeLabel);

        sudokuBox.getChildren().addAll(nameLabel, contentBox);

        return sudokuBox;
    }

    /**
     * Met a jour le label de difficulte en fonction du Sudoku affiche.
     * 
     * @param difficultyLabel Le label affichant la difficulte.
     * @param sudoku Le Sudoku dont on veut mettre a jour la difficulte.
     */
    private static void updateDifficultyLabel(Label difficultyLabel, Sudoku sudoku) {
        switch (sudoku.getName().charAt(7)) {
            case 'F' -> difficultyLabel.setText("Facile");
            case 'M' -> difficultyLabel.setText("Moyen");
            case 'D' -> difficultyLabel.setText("Difficile");
        }
    }

    /**
     * Initialise la liste des Sudokus disponibles a partir de la base de donnees.
     * 
     * @return Une liste de Sudokus recuperes et initialises.
     */
    private static List<Sudoku> initializeSudokus() {
        List<Game> games = DBManager.getGamesForProfile(MainMenu.getProfileName());
        List<Sudoku> sudokus = new ArrayList<>();

        int sizeEasy = DBManager.getGridSizeWithDifficulty(Difficulty.EASY);
        int sizeMedium = DBManager.getGridSizeWithDifficulty(Difficulty.MEDIUM);
        int sizeHard = DBManager.getGridSizeWithDifficulty(Difficulty.HARD);

        for (int i = 0; i < DBManager.getGridsSize(); i++) {
            String nameS = "Sudoku ";

            if(i < sizeEasy) {
                nameS = nameS + "F-" + (i % sizeEasy + 1);
            }
            else if (i >= sizeEasy && i < (sizeEasy + sizeMedium)) {
                nameS = nameS + "M-" + (i % sizeMedium + 1);
            }
            else {
                nameS = nameS + "D-" + (i % sizeHard + 1);
            }
            
            sudokus.add(new Sudoku(i+1, nameS, 0, 0, GameState.NOT_STARTED));
        }

        games.stream().forEach(game-> {
            sudokus.get(game.getGrid().getId()-1).modifyInfo(game.getElapsedTime(), game.getScore(), game.getGameState(), game);
        });
        return sudokus;
    }


    /**
     * Affiche la liste des Sudokus par difficulte, avec la possibilite de naviguer entre les pages.
     * 
     * @param sudokuContainer Le conteneur où les grilles seront affichees.
     * @param stage La fenetre principale.
     * @param sudokus La liste des Sudokus a afficher.
     * @param difficultyLabel Le label de la difficulte actuelle.
     * @param leftArrow Le bouton de navigation a gauche.
     * @param rightArrow Le bouton de navigation a droite.
     */
    private static void showSudokuList(GridPane sudokuContainer, Stage stage, List<Sudoku> sudokus, Label difficultyLabel, Button leftArrow, Button rightArrow) {
        sudokuContainer.getChildren().clear();
        int sudokusPerPage = 12;
        int startIndex = currentPage * sudokusPerPage;
        int endIndex = Math.min(startIndex + sudokusPerPage, sudokus.size());

        updateDifficultyLabel(difficultyLabel, sudokus.get(startIndex));

        int columns = 4;
        for (int i = startIndex; i < endIndex; i++) {
            Sudoku sudoku = sudokus.get(i);

            VBox sudokuBox = createSudokuBox(sudoku);
            sudokuContainer.add(sudokuBox, (i - startIndex) % columns, (i - startIndex) / columns);

            final int selectedSudokuId = i;
            sudokuBox.setOnMouseClicked(e -> {
                if(sudoku.getStatus() == GameState.IN_PROGRESS || sudoku.getStatus() == GameState.FINISHED) {
                
                    // Creer une nouvelle petite fenetre pop-up (fenetre modale)
                    Stage popupStage = new Stage();
                    popupStage.initModality(Modality.APPLICATION_MODAL); // Bloquer les interactions avec la fenetre principale
                    popupStage.setTitle("Confirmation"); // Titre de la fenetre
                
                    // Composants de l'interface utilisateur
                    Button restartButton = getNewRestartButton(stage, sudokus, selectedSudokuId, popupStage);
                    
                    VBox popupLayout;
                    if(sudoku.getStatus() == GameState.IN_PROGRESS) {
                        Label label = new Label("Voulez vous reprendre votre partie en cours ou recommencer de zéro ?");
                        Button reloadButton = getNewReloadButton(stage, sudokus, selectedSudokuId, popupStage);
                        popupLayout = new VBox(10, label, reloadButton, restartButton);
                    }
                    else {
                        Label label = new Label("Voulez vous recommencer ?");
                        popupLayout = new VBox(10, label, restartButton);
                    }
                    
                    // Creer une mise en page verticale avec un espacement entre les elements
                    popupLayout.setPadding(new Insets(10)); // Ajouter une marge pour une meilleure apparence
                
                    // Creer et appliquer la scene a la fenetre pop-up
                    Scene scene = new Scene(popupLayout, 300, 150);
                    popupStage.setScene(scene);
                
                    // Afficher la fenetre pop-up et attendre l'interaction de l'utilisateur
                    popupStage.showAndWait();
                }
                else {
                    Sudoku selectedSudoku = sudokus.get(selectedSudokuId);
                    SudokuGame.showSudokuGame(stage, selectedSudoku);
                }
            });
        }

        leftArrow.setDisable(currentPage == 0);
        rightArrow.setDisable(endIndex >= sudokus.size());
    }

    /**
     * Formate le temps en secondes en une chaîne de caracteres au format HH:MM:SS.
     * 
     * @param seconds Le temps en secondes a formater.
     * @return La chaîne de caracteres formatee sous la forme HH:MM:SS.
     */
    private static String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    /**
     * Cree un bouton pour recommencer un jeu a partir de la fenetre pop-up.
     * 
     * @param stage La fenetre principale.
     * @param sudokus La liste des Sudokus disponibles.
     * @param selectedSudokuId L'identifiant du Sudoku selectionne.
     * @param popupStage La fenetre pop-up.
     * @return Le bouton de recommencement.
     */
    private static Button getNewRestartButton(Stage stage, List<Sudoku> sudokus, int selectedSudokuId, Stage popupStage) {
        Button restartButton = new Button("Recommencer"); // Bouton pour confirmer la creation du profil
    
        restartButton.setOnAction(event -> {
            Sudoku selectedSudoku = sudokus.get(selectedSudokuId);
            try {
                DBManager.deleteGame(selectedSudoku.getGame().getId());
            } catch (SQLException e1) {
                System.err.println("Error deleting game: " + e1.getMessage());
            }
            selectedSudoku.setStatus(GameState.NOT_STARTED);
            selectedSudoku.getGame().setGameState(GameState.NOT_STARTED);
            SudokuGame.showSudokuGame(stage, selectedSudoku);
            popupStage.close();
        });
    
        return restartButton;
    }

    /**
     * Cree un bouton pour reprendre un jeu a partir de la fenetre pop-up.
     * 
     * @param stage La fenetre principale.
     * @param sudokus La liste des Sudokus disponibles.
     * @param selectedSudokuId L'identifiant du Sudoku selectionne.
     * @param popupStage La fenetre pop-up.
     * @return Le bouton de reprise.
     */
    private static Button getNewReloadButton(Stage stage, List<Sudoku> sudokus, int selectedSudokuId, Stage popupStage) {
        Button reloadButton = new Button("Reprendre"); // Bouton pour confirmer la creation du profil

        reloadButton.setOnAction(event -> {
            Sudoku selectedSudoku = sudokus.get(selectedSudokuId);
            SudokuGame.showSudokuGame(stage, selectedSudoku);
            popupStage.close();
        });

        return reloadButton;
    }
}




