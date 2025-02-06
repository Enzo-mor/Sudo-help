package com.grp6;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SudokuGame {
    
    private int selectedNumber = 0;

    public void show(Stage stage) {
        BorderPane root = new BorderPane(); // Utilisation d'une disposition BorderPane
    
        // ---- Grille Sudoku (à gauche) ----
        GridPane sudokuGrid = new GridPane();
        sudokuGrid.setPadding(new Insets(20));
        sudokuGrid.setHgap(5);
        sudokuGrid.setVgap(5);
        sudokuGrid.setAlignment(Pos.CENTER);
        sudokuGrid.setStyle("-fx-border-color: black; -fx-background-color: #F8F8F8;");
    
        // Création de la grille 9x9
        int[][] sudokuData = new int[9][9]; // À remplacer par les données réelles du Sudoku
        Label[][] cells = new Label[9][9]; // Utilisation de labels pour éviter la saisie
        Grid grid = new Grid(null); // Grille de sudoku récuperer depuis la BdD
    
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Label cell = new Label();
                cell.setPrefSize(40, 40);
                cell.setAlignment(Pos.CENTER);
                cell.setStyle("-fx-border-color: gray; -fx-font-size: 16px; -fx-background-color: white;");
    
                if (sudokuData[row][col] != 0) {
                    cell.setText(String.valueOf(grid.getCell(row, col).getNumber()));
                    cell.setStyle("-fx-background-color: #D3D3D3; -fx-font-weight: bold;");
                }
    
                final int r = row, c = col; // Final variables pour la lambda
                cell.setOnMouseClicked(e -> {
                    // Insérer le chiffre sélectionné dans la case cliquée
                    if (selectedNumber != 0) {
                        cell.setText(String.valueOf(selectedNumber));
                        grid.getCell(r, c).setNumber(selectedNumber);
                        System.out.println(grid);
                    }
                });
    
                cells[row][col] = cell;
                sudokuGrid.add(cell, col, row);
            }
        }
    
        root.setLeft(sudokuGrid);
    
        // ---- Chiffres à sélectionner (à droite, sur 2 lignes) ----
        VBox rightPane = new VBox(20);
        rightPane.setPadding(new Insets(20));
        rightPane.setAlignment(Pos.TOP_CENTER);
    
        Label numbersLabel = new Label("Chiffres");
        numbersLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
    
        GridPane numberGrid = new GridPane();
        numberGrid.setHgap(10);
        numberGrid.setVgap(10);
        numberGrid.setAlignment(Pos.CENTER);
    
        // Création des boutons pour les chiffres de 1 à 9
        int buttonSize = 50; // Taille des boutons
        for (int i = 1; i <= 9; i++) {
            Button numberButton = new Button(String.valueOf(i));
            numberButton.setPrefSize(buttonSize, buttonSize);
            numberButton.setStyle("-fx-font-size: 16px;");
    
            // Action : Permet de sélectionner un chiffre
            final int number = i; // Final pour utilisation dans la lambda
            numberButton.setOnAction(e -> {
                selectedNumber = number; // Définir le chiffre sélectionné
            });
    
            // Ajouter les boutons dans la grille (2 lignes)
            if (i <= 5) {
                numberGrid.add(numberButton, i - 1, 0); // Ligne 0
            } else {
                numberGrid.add(numberButton, i - 6, 1); // Ligne 1
            }
        }
    
        // ---- Bouton Retour ----
        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> Main.showSudokuLibrary(stage));
    
        VBox bottomPane = new VBox(backButton);
        bottomPane.setAlignment(Pos.CENTER);
        bottomPane.setPadding(new Insets(10));
        root.setBottom(bottomPane);
    
        // ---- Affichage de la scène ----
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Jeu de Sudoku");
        stage.setScene(scene);
        stage.show();
    }
}
