package com.example;

import com.bdd.*;

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
        
        // Créer le panneau de sélection des chiffres
        NumberSelection numberSelection = new NumberSelection(); // Création avant pour la référence
        
        // Utilisation de la Base de Données
        // Exemple: Récupération de la grille à l'identifiant n°3
        Grid gridSudoku = DBManager.getGrid(3);

        // Créer la grille de Sudoku
        SudokuGrid grid = new SudokuGrid(numberSelection, gridSudoku); // Passer NumberSelection à SudokuGrid

        grid.setGrid();
        
        // Créer le panneau des outils
        ToolsPanel toolsPanel = new ToolsPanel(grid);

        // Créer le panneau des contrôles
        ControlButtons controlsButtons = new ControlButtons(grid);

        // Disposition de la scène de droite
        VBox rightPanel = new VBox(20);
        rightPanel.getChildren().addAll(numberSelection.getNumberSelection(), toolsPanel.getTools(), controlsButtons.getControlButtons());
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

    // @Override
    // public void start(Stage primaryStage) {
    //     MenusController.launcher(primaryStage);
    // }

    public static void main(String[] args) {
        try {
            // Initialiser la base de données
            DBManager.init();
            launch(args);
        } catch (Exception e) {
            // Gestion des execptions lié à l'initilisation
            System.err.println("Erreur : " + e.getMessage());
        }
    }
}