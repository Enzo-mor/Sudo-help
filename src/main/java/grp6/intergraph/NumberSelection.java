package grp6.intergraph;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NumberSelection {
    private static VBox numberSelection;
    private static String selectedNumber = null;
    private ToolsPanel toolsPanel; // Ajout du panneau d'outils
    private SudokuGrid sudokuGrid; // Référence à la grille Sudoku

    public NumberSelection(ToolsPanel toolsPanel) {
        this.toolsPanel = toolsPanel;
        this.sudokuGrid = sudokuGrid;

        numberSelection = new VBox(5);
        numberSelection.setAlignment(Pos.CENTER);
    
        HBox topNumbers = new HBox(5);
        topNumbers.setAlignment(Pos.CENTER);
        Button[] numButtonsTop = new Button[5];
    
        for (int i = 1; i <= 5; i++) {
            final int index = i  - 1;
            numButtonsTop[index] = new Button(String.valueOf(i));
            numButtonsTop[index].setMinSize(50, 50);
            numButtonsTop[index].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            numButtonsTop[index].setOnAction(e -> selectNumber(numButtonsTop[index]));
            topNumbers.getChildren().add(numButtonsTop[index]);
        }
    
        HBox bottomNumbers = new HBox(5);
        bottomNumbers.setAlignment(Pos.CENTER);
        Button[] numButtonsBottom = new Button[4];
    
        for (int i = 6; i <= 9; i++) {
            final int index = i - 6;
            numButtonsBottom[index] = new Button(String.valueOf(i));
            numButtonsBottom[index].setMinSize(50, 50);
            numButtonsBottom[index].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            numButtonsBottom[index].setOnAction(e -> selectNumber(numButtonsBottom[index]));

            bottomNumbers.getChildren().add(numButtonsBottom[index]);
        }
    
        numberSelection.getChildren().addAll(topNumbers, bottomNumbers);
    }

    private void selectNumber(Button button) {
        String numberStr = button.getText();
        Integer number = Integer.valueOf(numberStr);
    
        if (toolsPanel.getAnnotationMode() && sudokuGrid != null && SudokuGrid.getSelectedCell() != null) {
            // Mode annotation activé → Ajouter/Supprimer une annotation
            int row = SudokuGrid.getSelectedRow();
            int col = SudokuGrid.getSelectedCol();
    
            if (!sudokuGrid.hasAnnotation(row, col, numberStr)) {
                sudokuGrid.addAnnotationToCell(row, col, numberStr);
                sudokuGrid.getGame().addAnnotation(row, col, number);
            } else {
                sudokuGrid.removeAnnotationFromCell(row, col, numberStr);
                sudokuGrid.getGame().removeAnnotation(row, col, number);
            }
        } else {
            // Mode normal → Sélection du nombre
            var buttons = numberSelection.lookupAll(".button");
            buttons.forEach(node -> ((Button) node).setStyle(""));  
            button.setStyle("-fx-background-color: lightgreen;");
    
            // Stocke le nombre sélectionné
            setSelectedNumber(numberStr);
        }
    }

    // Getters
    public VBox getNumberSelection() {
        return numberSelection;
    }

    public static String getSelectedNumber() {
        return selectedNumber;
    }

    // Setters
    public void setSelectedNumber(String selectedNumber) {
        this.selectedNumber = selectedNumber;
    }

    public void setSudokuGrid(SudokuGrid sudokuGrid) {
        this.sudokuGrid = sudokuGrid;
    }

    public static void resetSelectedNumber() {
        selectedNumber = null;
    }

    public static void clearSelection() {
        var buttons = numberSelection.lookupAll(".button");
        buttons.forEach(node -> ((Button) node).setStyle("")); 
    }
}