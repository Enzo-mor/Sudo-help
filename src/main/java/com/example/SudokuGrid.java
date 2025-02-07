package com.example;
import com.bdd.*;
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
    private boolean eraseMode = false;    // Mode gomme
    private boolean annotationMode = false; // Mode annotation
    private NumberSelection numberSelection; // Panneau de sélection des chiffres
    private Button selectedCell = null; // Bouton de la cellule sélectionnée
    private Button[][] cells = new Button[9][9]; // Stocke les boutons des cellules
    private Grid gridSudoku; // Grille de sudoku

    public SudokuGrid(NumberSelection numberSelection, Grid gridData) {
        this.grid = new GridPane();
        this.numberSelection = numberSelection;
        this.gridSudoku = gridData;
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setPadding(new Insets(10));


        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // Créer un bouton pour chaque cellule
                Button cell = new Button();
                cell.setMinSize(50, 50);
                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                cell.setStyle((row / 3 + col / 3) % 2 == 0 ? "-fx-background-color: lightblue;" : "-fx-background-color: white;");

                // Label pour le nombre principal
                Label mainNumber = new Label();
                mainNumber.setFont(new Font(18));

                // Texte pour les annotations
                Text annotationText = new Text();
                annotationText.setFont(new Font(10));

                // Liste des annotations
                List<String> annotations = new ArrayList<>();

                // Initialement, afficher uniquement le nombre principal
                cell.setGraphic(mainNumber);

                final int r = row;
                final int c = col;
                cell.setOnAction(e -> {
                    selectedCell = cell;
                    String selectedStr = numberSelection.getSelectedNumber();
                    Cell currentCell = gridData.getCell(r, c);  // gridData étant l'instance de la grille de données

                    // Ne pas permettre de modifier les cellules fixes
                    if (!currentCell.isEditable()) {
                        // Afficher un message ou ne rien faire
                        System.out.println("Cette cellule est fixe et ne peut pas être modifiée.");
                        return;  // Sortie de la méthode, empêchant toute modification de la cellule fixe
                    }

                    if (eraseMode) {
                        mainNumber.setText(""); // Effacer le nombre
                        annotations.clear(); // Effacer les annotations
                        annotationText.setText("");
                        cell.setGraphic(mainNumber); // Revenir au mode principal
                    }

                    else if (annotationMode && selectedStr != null) {
                        if (mainNumber.getText().isEmpty()) { // Autoriser les annotations seulement si la case est vide
                            if (!annotations.contains(selectedStr)) {
                                annotations.add(selectedStr);
                            } else {
                                annotations.remove(selectedStr);
                            }

                            // Formater les annotations en 3x3
                            StringBuilder formattedAnnotations = new StringBuilder();
                            String[] positions = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
                            for (int i = 0; i < 9; i++) {
                                if (annotations.contains(positions[i])) {
                                    formattedAnnotations.append(positions[i]);
                                } else {
                                    formattedAnnotations.append(" ");
                                }

                                if ((i + 1) % 3 == 0) {
                                    formattedAnnotations.append("\n");
                                } else {
                                    formattedAnnotations.append(" ");
                                }
                            }
                            annotationText.setText(formattedAnnotations.toString().trim());

                            // Afficher les annotations à la place du nombre
                            cell.setGraphic(annotationText);
                        }
                    }

                    else if (selectedStr != null) {
                        mainNumber.setText(selectedStr);
                        annotations.clear(); // Effacer les annotations
                        annotationText.setText("");

                        // Afficher uniquement le nombre principal
                        cell.setGraphic(mainNumber);
                    }
                });

                cells[row][col] = cell;
                grid.add(cell, col, row);

                /* Modifier historique */
            }
        }
    }    

    // Getters
    public GridPane getGridPane() {
        return grid;
    }

    public boolean getEraseMode() {
        return eraseMode;
    }

    public boolean getAnnotationMode() {
        return annotationMode;
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

    // Méthode pour définir les valeurs de la grille
    public void setGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cell = gridSudoku.getCell(row, col);
                Button cellButton = cells[row][col];
                Label mainNumber = new Label();
                Text annotationText = new Text();

                if (cell instanceof FixCell) {
                    // Pour les cellules fixes, on affiche le nombre principal
                    FixCell fixCell = (FixCell) cell;
                    if (fixCell.getNumber() == 0) {
                        mainNumber.setText("");
                    } else {
                        mainNumber.setText(String.valueOf(fixCell.getNumber()));
                    }                    
                    annotationText.setText(""); // Pas d'annotations pour les cellules fixes
                    cellButton.setGraphic(mainNumber);
                } else if (cell instanceof FlexCell) {
                    // Pour les cellules flexibles, on affiche les annotations
                    FlexCell flexCell = (FlexCell) cell;
                    List<Integer> annotations = flexCell.getAnnotations();
                    StringBuilder formattedAnnotations = new StringBuilder();

                    // Formater les annotations en 3x3
                    String[] positions = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
                    for (int i = 0; i < 9; i++) {
                        if (annotations.contains(i + 1)) {
                            formattedAnnotations.append(positions[i]);
                        } else {
                            formattedAnnotations.append(" ");
                        }

                        if ((i + 1) % 3 == 0) {
                            formattedAnnotations.append("\n");
                        } else {
                            formattedAnnotations.append(" ");
                        }
                    }

                    annotationText.setText(formattedAnnotations.toString().trim());
                    cellButton.setGraphic(annotationText);
                }
            }
        }
    }
}
