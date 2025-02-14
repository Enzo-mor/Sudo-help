package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SudokuMenu {
    /*
    private void showSudokuLibrary(Stage stage) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.CENTER);

        int columns = 4;
        for (int i = 0; i < sudokus.size(); i++) {
            Sudoku sudoku = sudokus.get(i);

            VBox sudokuBox = new VBox(10);
            sudokuBox.setAlignment(Pos.CENTER);
            sudokuBox.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #F0F0F0;");

            Label nameLabel = new Label(sudoku.getName());
            Label bestTimeLabel = new Label(sudoku.getBestTime());
            Label scoreLabel = new Label(sudoku.getScore());
            Label statusLabel = new Label(sudoku.getStatus());

            sudokuBox.getChildren().addAll(nameLabel, bestTimeLabel, scoreLabel, statusLabel);

            gridPane.add(sudokuBox, i % columns, i / columns);

            final int selectedSudokuId = i + 1; // Crée une copie finale
            sudokuBox.setOnMouseClicked(e -> showSudokuGame(stage, selectedSudokuId));
        }

        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> showMainMenu(stage, selectedProfile));

        VBox layout = new VBox(20, gridPane, backButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 800, 600);

        stage.setTitle("Bibliothèque de Sudoku");
        stage.setScene(scene);
        stage.show();
    }*/
}
