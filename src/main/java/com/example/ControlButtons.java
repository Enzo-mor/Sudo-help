package com.example;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ControlButtons {
    private HBox controlButtons;
    private SudokuGrid sudokuGrid;

    public ControlButtons(SudokuGrid sudokuGrid) {
        controlButtons = new HBox(10);
        controlButtons.setAlignment(Pos.CENTER);

        Button undoButton = new Button("Annuler");
        Button redoButton = new Button("Refaire");
        Button helpButton = new Button("Aide");
        Button checkButton = new Button("Vérifier");
        Button restartButton = new Button("Recommencer");

        // Ajoute l'action sur le bouton "Recommencer"
        restartButton.setOnAction(e -> {
            sudokuGrid.resetGame();
            SudokuTimer.resetTimer();
        });

        controlButtons.getChildren().addAll(undoButton, redoButton, helpButton, checkButton, restartButton);
    }

    // Getters
    public HBox getControlButtons() {
        return controlButtons;
    }
}