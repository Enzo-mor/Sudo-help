package grp6.intergraph;
import java.util.ArrayList;
import java.util.List;

import grp6.sudocore.Action;
import grp6.sudocore.Cell;
import grp6.sudocore.FlexCell;
import grp6.sudocore.Game;
import grp6.sudocore.NumberCellAction;
import grp6.syshelp.AutoAnnotation;
import grp6.syshelp.Help;
import grp6.syshelp.SysHelp;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

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
        Button checkButton = new Button("Vérifier");
        Button restartButton = new Button("Recommencer");

        //SYSHELP
        Button sysHelpButton = new Button("Sudo-Help");
        Button autoAnnotButton = new Button("AA");

        //SYSHELP
        sysHelpButton.setStyle("-fx-background-color: purple; -fx-text-fill: white;");
        autoAnnotButton.setStyle("-fx-background-color: purple; -fx-text-fill: white;");

        //SYSHELP
        sysHelpButton.setOnAction(e -> {
            Help help = SysHelp.generateHelp(sudokuGame.getGrid());
            if(help != null){
                System.out.println(help);
            }else{
                System.out.println("Aucune aide trouvée.");
            }
        });
        //SYSHELP
        autoAnnotButton.setOnAction(e -> {
            for(int i = 0;i<9;i++){
                for(int j = 0;j<9;j++){
                    Cell c = sudokuGame.getGrid().getCell(i, j);
                    if(c instanceof FlexCell){
                        AutoAnnotation.generate(sudokuGame.getGrid(), c, i, j);
                        for(int z = 0;z<9;z++){
                            if(c.getAnnotationsBool()[z]){
                                grid.addAnnotationToCell(i,j,""+(z+1));
                            }
                        }
                    }
                }
            }
        });

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
            }
        });

        // Ajoute l'action sur le bouton "Vérifier"
        checkButton.setOnAction(e -> {
            List<int[]> eval = sudokuGame.evaluate();
        
            if (!eval.isEmpty()) {
                // Colorier les cellules avec des erreurs en rouge
                sudokuGrid.setCellsColorError(eval);
        
                // Faire une copie des erreurs pour réinitialiser les couleurs plus tard
                List<int[]> originalErrors = new ArrayList<>(eval);
        
                // Créer une pause de 1 seconde avant d'annuler les erreurs
                Timeline pause = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                    // Annuler les actions jusqu'à corriger les erreurs
                    undoUntilFirstError(eval);
        
                    // Remettre la couleur par défaut en utilisant la copie
                    sudokuGrid.setCellsColorDefault(originalErrors);
        
                    // Nettoyer l'historique après l'annulation
                    sudokuGame.deleteActionsAfterCurrent();
                }));
        
                pause.setCycleCount(1);
                pause.play();
            }
        });

        //SYSHELP
        controlButtons.getChildren().addAll(undoButton, redoButton, helpButton, checkButton, restartButton, sysHelpButton,autoAnnotButton);
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
                String oldNumberString = String.valueOf(currentAction.getOldNumber());
                sudokuGrid.removeAnnotationFromCell(row, col, oldNumberString);
            }

            /* Modification coté bdd */
            sudokuGame.undoAction();
        }
    }

    // Annuler les actions jusqu'à corriger toutes les erreurs
    private void undoUntilFirstError(List<int[]> errors) {
        Action currentAction = sudokuGame.getLastAction();

        while (currentAction != null && !errors.isEmpty()) {
            undoAction(currentAction);
            currentAction = sudokuGame.getLastAction();

            // Met à jour les erreurs restantes
            errors.retainAll(sudokuGame.evaluate());
        }
    }
}