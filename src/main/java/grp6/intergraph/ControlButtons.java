package grp6.intergraph;

import grp6.sudocore.*;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ControlButtons {
    private HBox controlButtons;
    private SudokuGrid sudokuGrid;
    private Game sudokuGame;

    public ControlButtons(SudokuGrid sudokuGrid, Game sudokuG) {
        sudokuGame = sudokuG;
        
        controlButtons = new HBox(10);
        controlButtons.setAlignment(Pos.CENTER);

        Button undoButton = new Button("Annuler");
        Button redoButton = new Button("Refaire");
        Button helpButton = new Button("Aide");
        Button checkButton = new Button("Vérifier");
        Button restartButton = new Button("Recommencer");

        // Ajoute l'action sur le bouton "Recommencer"
        restartButton.setOnAction(e -> {
            sudokuGrid.resetInterface();
            sudokuGame.restartGame();
        });

        // Ajoute l'action sur le bouton "Recommencer"
        undoButton.setOnAction(e -> {
            /* Modification coté affichage */
            Action currentAction = sudokuGame.getLastAction();
            if(currentAction != null){
                int row = currentAction.getRow();
                int col = currentAction.getColumn();
                sudokuGrid.setCellDisplay(row, col, currentAction.getOldNumber());
                System.out.println("Row : " + row + " Column : " + col);
                
                /* Modification coté bdd */
                sudokuGame.undoAction();
            }
        });

        // Ajoute l'action sur le bouton "Recommencer"
        redoButton.setOnAction(e -> {
            /* Modification coté affichage */
            Action currentAction = sudokuGame.getLastAction();
            if(currentAction != null) {
                int row = currentAction.getRow();
                int col = currentAction.getColumn();
                sudokuGrid.setCellDisplay(row, col, currentAction.getOldNumber());
                System.out.println("Row : "+ row + "Column : " + col);
                
                /* Modification coté bdd */
                sudokuGame.redoAction();
            }
        });

        controlButtons.getChildren().addAll(undoButton, redoButton, helpButton, checkButton, restartButton);
    }

    // Getters
    public HBox getControlButtons() {
        return controlButtons;
    }
}