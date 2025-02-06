package com.grp6;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SudokuLibrary {
    
    
    private final List<Grid> grids;

    public SudokuLibrary() {
        grids = new ArrayList<>();
        // Recuperation des grilles dans la base de donnée (A FAIRE)

    }

    public void show(Stage stage) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setAlignment(Pos.CENTER);

        int columns = 4;
        for (int i = 0; i < grids.size(); i++) {
            Grid sudoku = grids.get(i);

            VBox sudokuBox = new VBox(10);
            sudokuBox.setAlignment(Pos.CENTER);
            sudokuBox.setStyle("-fx-border-color: black; -fx-padding: 10px; -fx-background-color: #F0F0F0;");

            Label nameLabel = new Label("sudoku.getName()");            // Nom de la grille (A FAIRE)
            Label bestTimeLabel = new Label("sudoku.getBestTime()");    // Meilleure temps de la grille (A FAIRE)
            Label scoreLabel = new Label("sudoku.getScore()");          // Score de la grille (A FAIRE)
            Label statusLabel = new Label("sudoku.getStatus()");        // Status de la grille (A FAIRE)

            sudokuBox.getChildren().addAll(nameLabel, bestTimeLabel, scoreLabel, statusLabel);

            gridPane.add(sudokuBox, i % columns, i / columns);

            final int selectedSudokuId = i + 1; // Crée une copie finale
            sudokuBox.setOnMouseClicked(e -> Main.showSudokuGame(stage, selectedSudokuId));
        }

        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> Main.showMainMenu(stage));

        VBox layout = new VBox(20, gridPane, backButton);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 800, 600);

        stage.setTitle("Bibliothèque de Sudoku");
        stage.setScene(scene);
        stage.show();
    }

}


