package com.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class SudokuGrid {
    private GridPane grid;
    private String selectedNumber = null; // Chiffre sélectionné
    private boolean eraseMode = false;    // Mode gomme
    private boolean annotationMode = false; // Mode annotation

    public SudokuGrid() {
        this.grid = new GridPane();
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setPadding(new Insets(10));

        Button[][] cells = new Button[9][9]; // Stocke les boutons des cellules

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // Créer le conteneur pour chaque cellule
                VBox cellContainer = new VBox();
                cellContainer.setAlignment(Pos.CENTER);
                cellContainer.setMinSize(50, 50);
                cellContainer.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                
                // Label pour afficher le chiffre principal
                Label mainNumber = new Label();
                mainNumber.setFont(new Font(18));
                
                // Texte pour afficher les annotations
                Text annotationText = new Text();
                annotationText.setFont(new Font(10));

                cellContainer.getChildren().addAll(mainNumber, annotationText);

                // Créer un bouton pour chaque cellule, qui contient le conteneur
                Button cell = new Button();
                cell.setGraphic(cellContainer);
                cell.setMinSize(50, 50);
                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                cell.setStyle((row / 3 + col / 3) % 2 == 0 ? "-fx-background-color: lightblue;" : "-fx-background-color: white;");

                // Liste pour stocker les annotations
                List<String> annotations = new ArrayList<>();

                // Gestion des actions de clic sur une cellule
                final int r = row;
                final int c = col;
                cell.setOnAction(e -> {
                    if (eraseMode) {
                        // Si le mode gomme est activé
                        mainNumber.setText(""); // Effacer le chiffre principal
                        annotations.clear(); // Effacer les annotations
                        annotationText.setText(""); // Effacer les annotations affichées
                    } else if (annotationMode && selectedNumber != null) {
                        // Si le mode annotation est activé
                        if (!mainNumber.getText().isEmpty()) {
                            return; // Ne rien faire si un chiffre est déjà présent
                        }

                        // Ajouter ou retirer le chiffre sélectionné des annotations
                        if (annotations.contains(selectedNumber)) {
                            annotations.remove(selectedNumber);
                        } else {
                            annotations.add(selectedNumber);
                        }

                        // Réorganiser et afficher les annotations sous forme de 3x3
                        StringBuilder formattedAnnotations = new StringBuilder();
                        String[] positions = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
                        for (int i = 0; i < 9; i++) {
                            if (annotations.contains(positions[i])) {
                                formattedAnnotations.append(positions[i]);
                            } else {
                                formattedAnnotations.append(" ");
                            }

                            if ((i + 1) % 3 == 0) {
                                formattedAnnotations.append("\n"); // Nouvelle ligne après 3 chiffres
                            } else {
                                formattedAnnotations.append(" "); // Espacement entre les chiffres
                            }
                        }
                        annotationText.setText(formattedAnnotations.toString().trim());

                    } else if (selectedNumber != null) {
                        // Mode normal : insérer le chiffre sélectionné dans la cellule
                        mainNumber.setText(selectedNumber);
                        annotations.clear(); // Effacer les annotations
                        annotationText.setText(""); // Effacer les annotations
                    }
                });

                cells[row][col] = cell;
                grid.add(cell, col, row); // Ajouter la cellule dans la grille
            }
        }
    }

    // Getters
    public GridPane getGridPane() {
        return grid;
    }

    public String getSelectedNumber() {
        return selectedNumber;
    }

    public boolean getEraseMode() {
        return eraseMode;
    }

    public boolean getAnnotationMode() {
        return annotationMode;
    }

    // Setters
    public void setSelectedNumber(String number) {
        this.selectedNumber = number;
    }

    public void setEraseMode(boolean erase) {
        this.eraseMode = erase;
    }

    public void setAnnotationMode(boolean annotation) {
        this.annotationMode = annotation;
    }

    // Méthodes pour effacer la grille
    public void clearGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                VBox cellContainer = (VBox) ((Button) grid.getChildren().get(row * 9 + col)).getGraphic();
                Label mainNumber = (Label) cellContainer.getChildren().get(0);
                Text annotationText = (Text) cellContainer.getChildren().get(1);
                mainNumber.setText("");
                annotationText.setText("");
            }
        }
    }

    // MODIFIABLE, il s'agit d'une proposition
    
    // Méthode pour définir les valeurs de la grille
    public void setGrid(String[][] values) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                VBox cellContainer = (VBox) ((Button) grid.getChildren().get(row * 9 + col)).getGraphic();
                Label mainNumber = (Label) cellContainer.getChildren().get(0);
                Text annotationText = (Text) cellContainer.getChildren().get(1);
                mainNumber.setText(values[row][col]);
                annotationText.setText("");
            }
        }
    }
}