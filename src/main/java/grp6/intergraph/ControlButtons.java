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

            // Forcer la suppression des actions passees
            sudokuGame.deleteActionsAfterCurrent();
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
            putErrorsRed();
        });


        controlButtons.getChildren().addAll(undoButton, redoButton, helpButton, checkButton, restartButton);
    }

    // Getters
    public HBox getControlButtons() {
        return controlButtons;
    }

    public void undoAction(Action currentAction){
        /* Modification cote affichage */
        if(currentAction != null){
            int row = currentAction.getRow();
            int col = currentAction.getColumn();
            if(sudokuGrid.isLastActionEraser(row, col)) {
                sudokuGrid.modifyStateEraser(row, col, false);
                if (currentAction instanceof NumberCellAction) {
                    sudokuGrid.setCellDisplay(row, col, currentAction.getNumber());
                } else {
                    sudokuGrid.setAnnotationDisplay(SudokuGrid.getButton(row, col), row, col, null);
                }
            } else {
                if (currentAction instanceof NumberCellAction) {
                    if(currentAction.getOldNumber() == 0 && sudokuGrid.hasAnnotations(row, col))
                        sudokuGrid.setAnnotationDisplay(SudokuGrid.getButton(row, col), row, col, null);
                    else
                        sudokuGrid.setCellDisplay(row, col, currentAction.getOldNumber());
                } else {
                    String numberString = String.valueOf(currentAction.getAnnotation());
                    if (!sudokuGrid.hasAnnotation(row, col, numberString))
                        sudokuGrid.addAnnotationToCell(row, col, numberString);
                    else
                        sudokuGrid.removeAnnotationFromCell(row, col, numberString);
                }
    
                /* Modification cote bdd */
                sudokuGame.undoAction();
            }

        }
    }

    public void redoAction() {
        /* Modification cote bdd */
        try {
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
                    if (!sudokuGrid.hasAnnotation(row, col, redoNumberString))
                        sudokuGrid.addAnnotationToCell(row, col, redoNumberString);
                    else
                        sudokuGrid.removeAnnotationFromCell(row, col, redoNumberString);
                }
            }

        } catch (IllegalStateException  e) {
            System.err.println(e.getMessage());
        }

    }

    private void putErrorsRed() {
        // etape 1 : Identifier les erreurs sans effectuer d'undo / redo
        List<int[]> eval = evaluateWithUndoRedo();
    
        // Si des erreurs ont ete detectees
        if (!eval.isEmpty()) {
            // Colorier les cellules avec des erreurs en rouge
            sudokuGrid.setCellsColorError(eval);
    
            // Creer une pause de 1 seconde (1000 ms) avant d'annuler les erreurs
            Timeline pause = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                // Annuler les actions jusqu'à la premiere erreur
                undoActionsUntilErrorsResolved();
    
                // Remettre la couleur par defaut apres avoir annule les erreurs
                sudokuGrid.setCellsColorDefault(eval);
    
                // Supprimer les actions passees apres l'etat actuel
                sudokuGame.deleteActionsAfterCurrent();
            }));
    
            pause.setCycleCount(1);
            pause.play();
        }
    }
    
    private void undoActionsUntilErrorsResolved() {
        // Annuler les actions jusqu'à ce que `evaluate()` retourne une liste vide
        List<int[]> finalEval = sudokuGame.evaluate();
        
        while (!finalEval.isEmpty()) {
            Action currentAction = sudokuGame.getLastAction();
            if (currentAction != null) {
                undoAction(currentAction);  // Annuler l'action
            }
            // Reevaluer l'etat du Sudoku apres chaque annulation
            finalEval = sudokuGame.evaluate();
        }
    }
    

    private List<int[]> evaluateWithUndoRedo() {
        // Initialiser les listes pour stocker les coordonnees des erreurs
        List<int[]> errorCells = new ArrayList<>();
        
        // etape 1 : Annuler les actions jusqu'à ce que `evaluate()` retourne une liste vide
        List<int[]> currentEvaluation = sudokuGame.evaluate();
        int undoCount = 0;  // Compter le nombre d'actions undo
    
        while (!currentEvaluation.isEmpty()) {
            
            // Annuler l'action la plus recente
            Action currentAction = sudokuGame.getLastAction();
            if (currentAction != null) {
                int[] coordinates = {currentAction.getRow(), currentAction.getColumn()};
                errorCells.add(coordinates);
                undoAction(currentAction);
                undoCount++;  // Incrementer le compteur d'undo
            }
    
            // Reevaluer l'etat du Sudoku apres l'annulation
            currentEvaluation = sudokuGame.evaluate();
        }
    
        // etape 2 : Refaites exactement `undoCount` actions annulees avec redoAction()
        for (int i = 0; i < undoCount; i++) {
            redoAction();  // Reapplique chaque action annulee
        }
    
        // Retourner la liste des coordonnees à colorier
        return errorCells;
    }
}