package grp6.intergraph;
import grp6.sudocore.*;
import grp6.syshelp.*;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * Classe ControlButtons
 * Cette classe represente un ensemble de boutons de contrôle pour interagir avec une grille de Sudoku.
 * Elle permet d'annuler, refaire, verifier et recommencer le jeu.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 * @see SudokuGrid
 * @see Game
 * @see StyledContent
 * @see SudokuDisplay
 * @see SudokuGame
 * @see Action  
 */
public class ControlButtons {

    /** 
     * Conteneur des boutons de contrôle 
     */
    private final HBox controlButtons;

    /** 
     * Grille de Sudoku associee 
     */
    private SudokuGrid sudokuGrid;

    /** 
     * Jeu de Sudoku en cours 
     */
    private static Game sudokuGame;

    /** 
     * Instance de Help pour afficher l'aide 
     */
    private static Help help;

    /**
     * Numero de l'aide actuel
     */
    private static int currentHelp;

    /*
     * Bouton d'aide
     */
    private final Button helpButton ;


    /**
     * Constructeur de ControlButtons
     * 
     * @param grid Grille de Sudoku associee [SudokuGrid]
     * @param sudokuG Instance du jeu de Sudoku [Game]
     */
    public ControlButtons(SudokuGrid grid, Game sudokuG) {
        sudokuGame = sudokuG;
        this.sudokuGrid = grid;
        currentHelp = 0;
        
        controlButtons = new HBox(5);
        controlButtons.setAlignment(Pos.CENTER);

        Button undoButton = new Button("Annuler");
        Button redoButton = new Button("Refaire");
        helpButton = new Button("Aide");
        Button checkButton = new Button("Vérifier");
        Button restartButton = new Button("Recommencer");
        Button annotation = new Button("Auto");
        
        // Appliquer le style aux boutons
        StyledContent.applyButtonStyle(undoButton);
        StyledContent.applyButtonStyle(redoButton);
        StyledContent.applyButtonStyle(helpButton);
        StyledContent.applyButtonStyle(checkButton);
        StyledContent.applyButtonStyle(restartButton);

        annotation.setOnAction(e -> {
            Grid g =sudokuGrid.getGame().getGrid();
            for (int i=0 ;i <9;i++){
                for(int y=0;y<9;y++){
                    AutoAnnotation.generate(g, g.getCell(i, y), i, y);
                    for (Integer f : g.getCell(i, y).getAnnotations()){
                        SudokuGrid.addAnnotationToCell(i,y,String.valueOf(f));
                    }
                }
            }
        });
        
        // Ajoute l'action sur le bouton "Annuler"
        undoButton.setOnAction(e -> {
            SudokuDisplay.resetGrid(SudokuGrid.getGridPane());
            Action currentAction = sudokuGame.getLastAction();
            undoAction(currentAction);

            SudokuGame.resetTimer();
        });

        // Ajoute l'action sur le bouton "Refaire"
        redoButton.setOnAction(e -> {
            SudokuDisplay.resetGrid(SudokuGrid.getGridPane());
            redoAction();

            SudokuGame.resetTimer();
        });

        //SYSHELP
        helpButton.setOnAction(e -> {
            help = SysHelp.generateHelp(sudokuGame.getGrid(), sudokuGame.getProfile());
            currentHelp = 1;
            if(evaluateWithUndoRedo().isEmpty()) {
                if(help != null) {
                    SudokuGame.setHelpText(help.getMessage(currentHelp));
                    SudokuGame.setHelpOverlayTrue();
                }else{
                    System.out.println("Aucune aide trouvée.");
                }
            }
            else{
                currentHelp = 3;
                SudokuGame.setDisableSeeMoreButton(true);
                SudokuGame.setHelpText("La grille n'est pas correcte.");
                SudokuGame.setHelpOverlayTrue();
            }

            SudokuGame.resetTimer();
        });

        // Ajoute l'action sur le bouton "Verifier"
        checkButton.setOnAction(e -> {
            sudokuGame.decreaseScore("check");
            SudokuDisplay.resetGrid(SudokuGrid.getGridPane());
            putErrorsRed();

            SudokuGame.resetTimer();
        });

        // Ajoute l'action sur le bouton "Recommencer"
        restartButton.setOnAction(e -> {
            sudokuGrid.resetInterface();
            sudokuGame.restartGame();

            // Forcer la suppression des actions passees
            sudokuGame.deleteActionsAfterCurrent();

            SudokuGame.resetTimer();
        });

        controlButtons.getChildren().addAll(undoButton, redoButton, helpButton, checkButton, restartButton,annotation);
    }

    /**
     * Retourne l'ensemble des boutons de contrôle.
     * 
     * @return Conteneur HBox contenant les boutons [HBox]
     */
    public HBox getControlButtons() {
        return controlButtons;
    }

    /*
     * Retourne le bouton d'aide
     * 
     * @return Le bouton d'aide [Button]
     */
    public Button getHelpButton(){
        return helpButton;
    }

    /**
     * Annule la derniere action effectuee sur la grille.
     * 
     * @param currentAction Derniere action realisee [Action]
     */
    public static void undoAction(Action currentAction){
        /* Modification cote affichage */
        if(currentAction != null){
            int row = currentAction.getRow();
            int col = currentAction.getColumn();
            if(SudokuGrid.isLastActionEraser(row, col)) {
                SudokuGrid.modifyStateEraser(row, col, false);
                if (currentAction instanceof NumberCellAction) {
                    SudokuGrid.setCellDisplay(row, col, currentAction.getNumber());
                } else {
                    SudokuGrid.setAnnotationDisplay(SudokuGrid.getButton(row, col), row, col, null);
                }
            } else {
                if (currentAction instanceof NumberCellAction) {
                    if(currentAction.getOldNumber() == 0 && SudokuGrid.hasAnnotations(row, col))
                        SudokuGrid.setAnnotationDisplay(SudokuGrid.getButton(row, col), row, col, null);
                    else
                        SudokuGrid.setCellDisplay(row, col, currentAction.getOldNumber());
                } else {
                    String numberString = String.valueOf(currentAction.getAnnotation());
                    if (!SudokuGrid.hasAnnotation(row, col, numberString))
                        SudokuGrid.addAnnotationToCell(row, col, numberString);
                    else
                        SudokuGrid.removeAnnotationFromCell(row, col, numberString);
                }
    
                /* Modification cote bdd */
                sudokuGame.undoAction();
            }

        }
    }

    /**
     * Refait la derniere action annulee sur la grille.
     */
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
                    SudokuGrid.setCellDisplay(row, col, currentAction.getRedoNumber());
                } else {
                    String redoNumberString = String.valueOf(currentAction.getRedoAnnotation());
                    if (!SudokuGrid.hasAnnotation(row, col, redoNumberString))
                        SudokuGrid.addAnnotationToCell(row, col, redoNumberString);
                    else
                        SudokuGrid.removeAnnotationFromCell(row, col, redoNumberString);
                }
            }

        } catch (IllegalStateException  e) {
            System.err.println(e.getMessage());
        }

    }

    /**
     * Met en evidence les erreurs detectees dans la grille de Sudoku.
     */
    private void putErrorsRed() {
        // etape 1 : Identifier les erreurs sans effectuer d'undo / redo
        List<int[]> eval = evaluateWithUndoRedo();
    
        // Si des erreurs ont ete detectees
        if (!eval.isEmpty()) {
            // Colorier les cellules avec des erreurs en rouge
            SudokuGrid.setCellsColorError(eval);
    
            // Creer une pause de 1 seconde (1000 ms) avant d'annuler les erreurs
            Timeline pause = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                // Annuler les actions jusqu'a la premiere erreur
                undoActionsUntilErrorsResolved();
    
                // Remettre la couleur par defaut apres avoir annule les erreurs
                SudokuGrid.setCellsColorDefault(eval);
    
                // Supprimer les actions passees apres l'etat actuel
                sudokuGame.deleteActionsAfterCurrent();
            }));
    
            pause.setCycleCount(1);
            pause.play();
        }
    }

    /**
     * Met en evidence une erreur detectee dans la grille de Sudoku pour une action donnee.
     *
     * @param action L'action a verifier et mettre en evidence si incorrecte.
     */
    public static void putErrorRed(Action action) {

        if (action == null) {
            return;
        }
        
        int x = action.getRow();
        int y = action.getColumn();

        List<int[]> eval = new ArrayList<>();
        eval.add(new int[] {x, y});
        
        SudokuGrid.setCellsColorError(eval);
        
        // Creer une pause de 1 seconde (1000 ms) avant d'annuler les erreurs
        Timeline pause = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            
            undoAction(action);

            // Remettre la couleur par defaut apres avoir annule les erreurs
            SudokuGrid.setCellsColorDefault(eval);

            // Supprimer les actions passees apres l'etat actuel
            sudokuGame.deleteActionsAfterCurrent();
        }));

        pause.setCycleCount(1);
        pause.play();
    }
    
    /**
     * Annule les actions jusqu'a ce que les erreurs soient corrigees.
     */
    private void undoActionsUntilErrorsResolved() {
        // Annuler les actions jusqu'a ce que `evaluate()` retourne une liste vide
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
    

    /**
     * Evalue les erreurs avec un processus d'annulation/refaire.
     * 
     * @return Liste des coordonnees des cellules en erreur [List<int[]>]
     */
    private List<int[]> evaluateWithUndoRedo() {
        // Initialiser les listes pour stocker les coordonnees des erreurs
        List<int[]> errorCells = new ArrayList<>();
        
        // etape 1 : Annuler les actions jusqu'a ce que `evaluate()` retourne une liste vide
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
    
        // Retourner la liste des coordonnees a colorier
        return errorCells;
    }

    /**
     * Retourne le numéro de l'aide actuel
     * 
     * @return le numéro de l'aide actuel [int]
     */
    public static int getCurrentHelp() {
        return currentHelp;
    }

    /**
     * Modifie le numéro de l'aide actuel
     * 
     * @param currentHelp le nouveau numéro d'aide [int]
     */
    public static void setCurrentHelp(int ch) {
        currentHelp = ch;
    }

    /**
     * Retourne l'instance de l'aide actuel
     * 
     * @return l'aide actuelle [Help]
     */
    public static Help getHelp() {
        return help;
    }

    /**
     * Methode pour desactiver le bouton de l'aide
     */
    public void disableHelpButton() {
        helpButton.setDisable(true);
    }
}