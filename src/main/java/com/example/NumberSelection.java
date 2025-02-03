package com.example;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NumberSelection {
    private VBox numberSelection;
    private SudokuGrid sudokuGrid;

    public NumberSelection(SudokuGrid grid) {
        this.sudokuGrid = grid;
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
            numButtonsTop[index].setOnAction(e -> sudokuGrid.setSelectedNumber(numButtonsTop[index].getText()));
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
            numButtonsBottom[index].setOnAction(e -> sudokuGrid.setSelectedNumber(numButtonsBottom[index].getText()));
            bottomNumbers.getChildren().add(numButtonsBottom[index]);
        }
    
        numberSelection.getChildren().addAll(topNumbers, bottomNumbers);
    }    

    // Getters
    public VBox getNumberSelection() {
        return numberSelection;
    }

    // Setters
    public void setNumberSelection(VBox numberSelection) {
        this.numberSelection = numberSelection;
    }
}
