package grp6.intergraph;
import grp6.sudocore.*;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SudokuMenu {
    public static void showSudokuLibrary(Stage stage) {

        List<Game> games = DBManager.getGamesForProfile(MainMenu.getProfileName());

        int currentPage = 0;

        Button leftArrow = new Button("<");
        leftArrow.setDisable(true);

        Button rightArrow = new Button(">");

        Label difficultyLabel = new Label("Facile");
        difficultyLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        final List<Sudoku> sudokus = new ArrayList<>();
        games.stream().forEach(game-> {
            
        });
        

        for (int i = 1; i <= 12; i++) {
            if (i == 1) {
                sudokus.add(new Sudoku("Sudoku F-" + i, "00.01.58", "100", "completed"));
            } else if (i == 2) {
                sudokus.add(new Sudoku("Sudoku F-" + i, "00.03.02", "", "in_progress"));
            } else {
                sudokus.add(new Sudoku("Sudoku F-" + i, "00.00.00", "", "not_started"));
            }
        }

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setAlignment(Pos.CENTER);

        int columns = 4;
        for (int i = 0; i < sudokus.size(); i++) {
            Sudoku sudoku = sudokus.get(i);

            VBox sudokuBox = new VBox(5);
            sudokuBox.setAlignment(Pos.CENTER);
            sudokuBox.setStyle("-fx-background-color: #939cb5; -fx-padding: 10; -fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 5;");

            Label nameLabel = new Label(sudoku.getName());
            Label scoreLabel = new Label("Score : " + (sudoku.getScore().isEmpty() ? "-" : sudoku.getScore()));
            Label bestTimeLabel = new Label("Temps : " + sudoku.getBestTime());

            ImageView statusIcon = new ImageView();
            statusIcon.setFitWidth(24);
            statusIcon.setFitHeight(24);

            switch (sudoku.getStatus()) {
                case "completed":
                    statusIcon.setImage(new Image(SudokuMenu.class.getResourceAsStream("/star.png")));
                    break;
                case "in_progress":
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

            gridPane.add(sudokuBox, i % columns, i / columns);

            final int selectedSudokuId = i + 1;
            sudokuBox.setOnMouseClicked(e -> SudokuGame.showSudokuGame(stage, selectedSudokuId));
        }

        Button backButton = new ProfileButton("Retour");
        backButton.setOnAction(e -> GameplayChoice.showGameplayChoice(stage));

        VBox layout = new VBox(15, difficultyLabel, gridPane, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 900, 600);

        stage.setTitle("Mode Libre - " + MainMenu.getProfileName());
        stage.setScene(scene);
        stage.show();
    }

    private static class Sudoku {
        private final String name;
        private final String bestTime;
        private final String score;
        private final String status;

        public Sudoku(String name, String bestTime, String score, String status) {
            this.name = name;
            this.bestTime = bestTime;
            this.score = score;
            this.status = status;
        }

        public String getName() { 
            return name;
        }

        public String getBestTime() { 
            return bestTime;
        }

        public String getScore() { 
            return score;
        }

        public String getStatus() { 
            return status;
        }
    }
}