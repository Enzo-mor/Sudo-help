package grp6.intergraph;
import grp6.sudocore.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.util.*;

public class ControlButtons {
    private final HBox controlButtons;
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
        Button checkButton = new Button("Verifier");
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
            redoAction();
        });

        // Ajoute l'action sur le bouton "Verifier"
        checkButton.setOnAction(e -> {
            List<int[]> eval = sudokuGame.evaluate();
        
            if (!eval.isEmpty()) {
                // Colorier les cellules avec des erreurs en rouge
                sudokuGrid.setCellsColorError(eval);
        
                // Faire une copie des erreurs pour reinitialiser les couleurs plus tard
                List<int[]> originalErrors = new ArrayList<>(eval);
        
                // Creer une pause de 1 seconde avant d'annuler les erreurs
                Timeline pause = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                    // Annuler les actions jusqu'a corriger les erreurs
                    undoUntilFirstError(eval);
        
                    // Remettre la couleur par defaut en utilisant la copie
                    sudokuGrid.setCellsColorDefault(originalErrors);
        
                    // Nettoyer l'historique apres l'annulation
                    sudokuGame.deleteActionsAfterCurrent();
                }));
        
                pause.setCycleCount(1);
                pause.play();
            }
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

            if (currentAction instanceof NumberCellAction) {
                sudokuGrid.setCellDisplay(row, col, currentAction.getOldNumber());
            } else {
                String numberString = String.valueOf(currentAction.getAnnotation());
                sudokuGrid.removeAnnotationFromCell(row, col, numberString);
            }

            /* Modification cote bdd */
            sudokuGame.undoAction();
        }
    }

    public void redoAction() {
        /* Modification cote bdd */
        sudokuGame.redoAction();

        /* Modification cote affichage */
        Action currentAction = sudokuGame.getLastAction();
        if(currentAction != null) {
            
            int row = currentAction.getRow();
            int col = currentAction.getColumn();
            
            if(currentAction instanceof NumberCellAction) {
                sudokuGrid.setCellDisplay(row, col, currentAction.getRedoNumber());
            } else {
                String redoNumberString = String.valueOf(currentAction.getRedoAnnotation());
                sudokuGrid.addAnnotationToCell(row, col, redoNumberString);
            }
        }
    }

    // Annuler les actions jusqu'a corriger toutes les erreurs
    private void undoUntilFirstError(List<int[]> errors) {
        Action currentAction = sudokuGame.getLastAction();

        while (currentAction != null && !errors.isEmpty()) {
            undoAction(currentAction);
            currentAction = sudokuGame.getLastAction();

            // Met a jour les erreurs restantes
            errors.retainAll(sudokuGame.evaluate());
        }
    }
}