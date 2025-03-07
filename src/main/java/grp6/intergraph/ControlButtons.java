package grp6.intergraph;

import grp6.sudocore.*;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.lang.Thread;

public class ControlButtons {
    private HBox controlButtons;
    private SudokuGrid sudokuGrid;
    private Game sudokuGame;

    public ControlButtons(SudokuGrid grid, Game sudokuG) {
        this.sudokuGame = sudokuG;
        this.sudokuGrid = grid;
        
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

        // Ajoute l'action sur le bouton "Annuler"
        undoButton.setOnAction(e -> {
            Action currentAction = sudokuGame.getLastAction();
            undoAction(currentAction);
        });

        // Ajoute l'action sur le bouton "Refaire"
        redoButton.setOnAction(e -> {
            /* Modification coté bdd */
            sudokuGame.redoAction();

            /* Modification coté affichage */
            Action currentAction = sudokuGame.getLastAction();
            if(currentAction != null) {
                int row = currentAction.getRow();
                int col = currentAction.getColumn();
                sudokuGrid.setCellDisplay(row, col, currentAction.getRedoNumber());
                System.out.println("Redo Number : " + currentAction.getRedoNumber() + "\nRow : " + row + " Column : " + col);
                
                
            }
        });

        // Ajoute l'action sur le bouton "Vérifier"
        checkButton.setOnAction(e -> {
            //TODO: Affichage en rouge des erreurs + undo jusqua la premiere erreur
            System.out.println(sudokuGame.getGrid());
            System.out.println("avnt : " + sudokuGame.getGrid().evaluate().size());
            sudokuGrid.setCellsColorError(sudokuGame.getGrid().evaluate());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            /* Modification coté affichage */
            Action currentAction = sudokuGame.getLastAction();
            while(currentAction != null && !currentAction.getCorrect()) {
                System.out.println("CC");
                undoAction(currentAction);
                currentAction = sudokuGame.getLastAction();
            }

            System.out.println("apres");
            sudokuGrid.setCellsColorDefault();

        });

        controlButtons.getChildren().addAll(undoButton, redoButton, helpButton, checkButton, restartButton);
    }

    // Getters
    public HBox getControlButtons() {
        return controlButtons;
    }

    public void undoAction(Action currentAction){
        if(currentAction != null){
            int row = currentAction.getRow();
            int col = currentAction.getColumn();
            sudokuGrid.setCellDisplay(row, col, currentAction.getOldNumber());

            /* Modification coté bdd */
            sudokuGame.undoAction();
        }
    }
}