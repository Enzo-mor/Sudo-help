package grp6.intergraph;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class NumberSelection {
    private static VBox numberSelection;
    private static String selectedNumber = null;
    private final ToolsPanel toolsPanel; // Ajout du panneau d'outils
    private SudokuGrid sudokuGrid; // Reference à la grille Sudoku

    public NumberSelection(ToolsPanel toolsPanel) {
        this.toolsPanel = toolsPanel;

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
            numButtonsTop[index].setOnAction(e -> {
                selectNumber(numButtonsTop[index]);

                // Marquage des cellules qui ont la même valeur
                if(Settings.getHighlightNumbers())
                    SudokuDisplay.highlightSameNumbers(SudokuGrid.getGridPane(), SudokuGrid.getGrid(), Integer.parseInt(getSelectedNumber()));
            });

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
            numButtonsBottom[index].setOnAction(e -> {
                selectNumber(numButtonsBottom[index]);

                // Marquage des cellules qui ont la même valeur
                if(Settings.getHighlightNumbers())
                    SudokuDisplay.highlightSameNumbers(SudokuGrid.getGridPane(), SudokuGrid.getGrid(), Integer.parseInt(getSelectedNumber()));
            });

            bottomNumbers.getChildren().add(numButtonsBottom[index]);
        }
    
        numberSelection.getChildren().addAll(topNumbers, bottomNumbers);
    }

    private void selectNumber(Button button) {
        String numberStr = button.getText();
        int number = Integer.parseInt(numberStr);
        Button numberButton = SudokuGrid.getSelectedCell();
    
        if (toolsPanel.getAnnotationMode() && sudokuGrid != null && numberButton != null) {
            
            String cellText = numberButton.getText().trim();

            if (!numberButton.getChildrenUnmodifiable().isEmpty() && numberButton.getChildrenUnmodifiable().get(0) instanceof Label) {
                Label label = (Label) numberButton.getChildrenUnmodifiable().get(0);
                cellText = label.getText().trim();
            }
                
            // On peut ajouter une annotation que si le bouton ne contient pas de nombre donc number vaut 0
            if(cellText.isEmpty() || cellText.equals("0")) {
                // Mode annotation active -> Ajouter/Supprimer une annotation
                int row = SudokuGrid.getSelectedRow();
                int col = SudokuGrid.getSelectedCol();
        
                if (!sudokuGrid.hasAnnotation(row, col, numberStr)) {
                    sudokuGrid.addAnnotationToCell(row, col, numberStr);
                    sudokuGrid.getGame().addAnnotation(row, col, number);
                } else {
                    sudokuGrid.removeAnnotationFromCell(row, col, numberStr);
                    sudokuGrid.getGame().removeAnnotation(row, col, number);
                }
            }
        } else {
            // Mode normal → Selection du nombre
            var buttons = numberSelection.lookupAll(".button");
            buttons.forEach(node -> ((Button) node).setStyle(""));  
            button.setStyle("-fx-background-color: lightgreen;");
    
            // Stocke le nombre selectionne
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
        NumberSelection.selectedNumber = selectedNumber;
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