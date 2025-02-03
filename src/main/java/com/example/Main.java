package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private static final double MIN_WIDTH = 600;
    private static final double MIN_HEIGHT = 500;

    @Override
    public void start(Stage primaryStage) {
        // Créer la grille de Sudoku
        SudokuGrid grid = new SudokuGrid();
        
        // Créer le panneau des outils
        ToolsPanel toolsPanel = new ToolsPanel(grid);

        // Créer le panneau de sélection des chiffres
        NumberSelection numberSelection = new NumberSelection(grid);

        // Créer le panneau des contrôles
        ControlButtons controlsButtons = new ControlButtons();

        // Disposition de la scène de droite
        VBox rightPanel = new VBox(20);
        rightPanel.getChildren().addAll( numberSelection.getNumberSelection(), toolsPanel.getTools(), controlsButtons.getControlButtons());
        // Disposition de la scène principale
        HBox layout = new HBox(20);
        layout.getChildren().addAll(grid.getGridPane(), rightPanel);

        // Création de la scène
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setTitle("Sudoku - Niveau facile 3");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}