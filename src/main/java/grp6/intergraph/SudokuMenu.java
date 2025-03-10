package grp6.intergraph;
import grp6.sudocore.*;
import grp6.sudocore.SudoTypes.*;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SudokuMenu {
    private static int currentPage = 0;
    public static void showSudokuLibrary(Stage stage) {
        List<Game> games = DBManager.getGamesForProfile(MainMenu.getProfileName());
        currentPage = 0;
        int currentPage = 0;

        Button leftArrow = new Button("<");
        leftArrow.setDisable(true);

        Button rightArrow = new Button(">");

        Label difficultyLabel = new Label();
        difficultyLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        /* Initialisation */
        final List<Sudoku> sudokus = new ArrayList<>();
        
        int sizeEasy = DBManager.getGridSizeWithDifficulty(Difficulty.EASY);
        int sizeMedium = DBManager.getGridSizeWithDifficulty(Difficulty.MEDIUM);
        int sizeHard = DBManager.getGridSizeWithDifficulty(Difficulty.HARD);

        GridPane sudokuContainer = new GridPane();
        sudokuContainer.setPadding(new Insets(20));
        sudokuContainer.setHgap(15);
        sudokuContainer.setVgap(15);
        sudokuContainer.setAlignment(Pos.CENTER);

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
            
            sudokus.add(new Sudoku(i, nameS, 0, 0, GameState.NOT_STARTED));
        }

        games.stream().forEach(game-> {
            sudokus.get(game.getGrid().getId()).modifyInfo(game.getElapsedTime(), game.getScore(), game.getGameState(), game);
        });

        leftArrow.setOnAction(e -> {
            SudokuMenu.currentPage--;
            showSudokuList(sudokuContainer, stage, sudokus, SudokuMenu.currentPage, difficultyLabel, leftArrow, rightArrow);
        });

        rightArrow.setOnAction(e -> {
            SudokuMenu.currentPage++;
            showSudokuList(sudokuContainer, stage, sudokus, SudokuMenu.currentPage, difficultyLabel, leftArrow, rightArrow);
        });

        showSudokuList(sudokuContainer, stage, sudokus, currentPage, difficultyLabel, leftArrow, rightArrow);
        sudokuContainer.requestLayout();

        HBox navigation = new HBox(10, leftArrow, sudokuContainer, rightArrow);
        navigation.setSpacing(20); // Ajoute un espace entre les éléments
        navigation.setPadding(new Insets(10)); // Évite qu'ils soient trop collés
        navigation.setAlignment(Pos.CENTER);

        navigation.autosize();

        Button backButton = new ProfileButton("Retour");
        backButton.setOnAction(e -> GameplayChoice.showGameplayChoice(stage));

        VBox layout = new VBox(15, difficultyLabel, navigation, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 900, 600);

        stage.setTitle("Mode Libre - " + MainMenu.getProfileName());
        stage.setScene(scene);
        stage.show();
    }

    private static void showSudokuList(GridPane sudokuContainer, Stage stage, List<Sudoku> sudokus, int currentPage, Label difficultyLabel, Button leftArrow, Button rightArrow) {
        sudokuContainer.getChildren().clear();
        int sudokusPerPage = 12;
        int startIndex = currentPage * sudokusPerPage;
        int endIndex = Math.min(startIndex + sudokusPerPage, sudokus.size());

        switch (sudokus.get(startIndex).getName().charAt(7)) {
            case 'F':
                difficultyLabel.setText("Facile");
                break;
            case 'M':
                difficultyLabel.setText("Moyen");
                break;
            case 'D':
                difficultyLabel.setText("Difficile");
                break;
        }

        int columns = 4;
        for (int i = startIndex; i < endIndex; i++) {
            Sudoku sudoku = sudokus.get(i);

            VBox sudokuBox = new VBox(5);
            sudokuBox.setAlignment(Pos.CENTER);
            sudokuBox.setStyle("-fx-background-color: #939cb5; -fx-padding: 10; -fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 5;");

            Label nameLabel = new Label(sudoku.getName());
            Label scoreLabel = new Label("Score : " + (sudoku.getScore()==0 ? "-" : sudoku.getScore()));
            Label bestTimeLabel = new Label("Temps : " + sudoku.getBestTime());

            ImageView statusIcon = new ImageView();
            statusIcon.setFitWidth(24);
            statusIcon.setFitHeight(24);

            switch (sudoku.getStatus()) {
                case FINISHED:
                    statusIcon.setImage(new Image(SudokuMenu.class.getResourceAsStream("/star.png")));
                    break;
                case IN_PROGRESS:
                    statusIcon.setImage(new Image(SudokuMenu.class.getResourceAsStream("/pause.png")));
                    break;
                default:
                    statusIcon = null;
                    break;
            }

            VBox contentBox = new VBox(3);
            contentBox.setAlignment(Pos.CENTER);

            if (statusIcon != null) {
                contentBox.getChildren().add(statusIcon);
            }
            contentBox.getChildren().addAll(scoreLabel, bestTimeLabel);

            sudokuBox.getChildren().addAll(nameLabel, contentBox);

            sudokuContainer.add(sudokuBox, i % columns, i / columns);

            final int selectedSudokuId = i;
            sudokuBox.setOnMouseClicked(e -> SudokuGame.showSudokuGame(stage, sudokus.get(selectedSudokuId)));
        }

        leftArrow.setDisable(currentPage == 0);
        rightArrow.setDisable(endIndex == sudokus.size());
        
        
    }
}