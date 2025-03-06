package grp6.intergraph;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import grp6.sudocore.*;

public class SudokuGrid {
    private GridPane grid;
    private ToolsPanel toolsPanel; // Panneau des outils
    private NumberSelection numberSelection; // Panneau de sélection des chiffres
    private Button[][] cells = new Button[9][9]; // Stocke les boutons des cellules
    private Grid gridSudoku; // Grille de sudoku
    private Game actualGame;

    public SudokuGrid(NumberSelection numberSelection, Grid gridData, ToolsPanel toolsP, Game actualGame) {
        this.grid = new GridPane();
        this.toolsPanel = toolsP;
        this.numberSelection = numberSelection;
        this.gridSudoku = gridData;
        this.actualGame=actualGame;

        grid.setHgap(2);
        grid.setVgap(2);
        grid.setPadding(new Insets(10));


        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Button cell = new Button();
                cell.setMinSize(50, 50);
                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                cell.setStyle((row / 3 + col / 3) % 2 == 0 ? "-fx-background-color: lightblue;" : "-fx-background-color: white;");
                
                Label mainNumber = new Label();
                mainNumber.setFont(new Font(18));
                
                Text annotationText = new Text();
                annotationText.setFont(new Font(10));
                
                List<String> annotations = new ArrayList<>();
                
                cell.setGraphic(mainNumber);
                
                setupCellInteraction(cell, row, col, annotations, mainNumber, annotationText);
                
                cells[row][col] = cell;
                grid.add(cell, col, row);
            }
        }
    }

    private void resetCellDisplay(Button cellButton, Label mainNumber, Text annotationText) {
        mainNumber.setText("");
        annotationText.setText("");
        cellButton.setGraphic(mainNumber);  // Revenir au mode principal
    }

    public void setCellDisplay(int row, int col, int oldNumber) {
        if(row >= 0 && col >= 0 && oldNumber >= 0) {
            
            Button cell = cells[row][col];
            Label labelTemp = new Label();
            labelTemp.setFont(new Font(18));
            Text textTemp = new Text();
            textTemp.setFont(new Font(10));

            if (oldNumber == 0){
                resetCellDisplay(cell, labelTemp, textTemp);
            }
            else{
                labelTemp.setText(String.format("%d", oldNumber));
                textTemp.setText("");
                cell.setGraphic(labelTemp);
            }
            
            cells[row][col] = cell;
        }
    }

    private void setAnnotationDisplay(Button cellButton, List<String> annotations, Text annotationText, Label mainNumber) {
        StringBuilder formattedAnnotations = new StringBuilder();
        String[] positions = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        
        for (int i = 0; i < 9; i++) {
            if (annotations.contains(positions[i])) {
                formattedAnnotations.append(positions[i]);
            } else {
                formattedAnnotations.append(" ");
            }
    
            if ((i + 1) % 3 == 0) {
                formattedAnnotations.append("\n");
            } else {
                formattedAnnotations.append(" ");
            }
        }
        
        annotationText.setText(formattedAnnotations.toString().trim());
        annotationText.setStyle("-fx-fill: blue;");
        cellButton.setGraphic(annotationText);
    }

    private void setNumberDisplay(Button cellButton, String selectedStr, Label mainNumber, Text annotationText) {
        mainNumber.setText(selectedStr);
        mainNumber.setStyle("-fx-text-fill: blue;");
        annotationText.setText("");  // Effacer les annotations
        cellButton.setGraphic(mainNumber);  // Afficher le nombre principal
    }
    
    private void setupCellInteraction(Button cellButton, final int r, final int c, List<String> annotations, Label mainNumber, Text annotationText) {
        cellButton.setOnAction(e -> {
            String selectedStr = numberSelection.getSelectedNumber();
            Cell currentCell = gridSudoku.getCell(r, c);
    
            if (!currentCell.isEditable()) {
                System.out.println("Cette cellule est fixe et ne peut pas être modifiée.");
                return;
            }
    
            if (toolsPanel.getEraseMode()) {
                resetCellDisplay(cellButton, mainNumber, annotationText);
            } else if (toolsPanel.getAnnotationMode() && selectedStr != null) {
                if (mainNumber.getText().isEmpty()) {
                    if (!annotations.contains(selectedStr)) {
                        annotations.add(selectedStr);
                        actualGame.addAnnotation(r,c,Integer.valueOf(selectedStr));
                    } else {
                        annotations.remove(selectedStr);
                        actualGame.removeAnnotation(r,c,Integer.valueOf(selectedStr));
                    }
                    setAnnotationDisplay(cellButton, annotations, annotationText, mainNumber);
                }
            } else if (selectedStr != null) {
                setNumberDisplay(cellButton, selectedStr, mainNumber, annotationText);
                actualGame.addNumber(r,c,Integer.valueOf(selectedStr));
            }
        });
    }
    
    

    // Getters
    public GridPane getGridPane() {
        return grid;
    }

    // Méthodes pour effacer la grille
    public void clearGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                VBox cellContainer = (VBox) ((Button) grid.getChildren().get(row * 9 + col)).getGraphic();
                Label mainNumber = (Label) cellContainer.getChildren().get(0);
                Text annotationText = (Text) cellContainer.getChildren().get(1);
                mainNumber.setText("");
                annotationText.setText("");
            }
        }
    }

    // Méthode pour définir les valeurs de la grille
    public void setGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cell = gridSudoku.getCell(row, col);
                Button cellButton = cells[row][col];
                Label mainNumber = new Label();
                Text annotationText = new Text();

                if (cell instanceof FixCell) {
                    // Pour les cellules fixes, on affiche le nombre principal
                    FixCell fixCell = (FixCell) cell;
                    if (fixCell.getNumber() == 0) {
                        mainNumber.setText("");
                    } else {
                        mainNumber.setFont(new Font(18));
                        mainNumber.setText(String.valueOf(fixCell.getNumber()));
                    }                    
                    annotationText.setText(""); // Pas d'annotations pour les cellules fixes
                    cellButton.setGraphic(mainNumber);
                } else if (cell instanceof FlexCell) {
                    // Pour les cellules flexibles, on affiche les annotations
                    FlexCell flexCell = (FlexCell) cell;
                    List<Integer> annotations = flexCell.getAnnotations();
                    List<String> annotationStr = new ArrayList<>();
                    for (Integer annotation : annotations) {
                        annotationStr.add(String.valueOf(annotation));
                    }
                    setAnnotationDisplay(cellButton, annotationStr, annotationText, mainNumber);
                }
            }
        }
    }

    private void resetGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cellData = gridSudoku.getCell(row, col);
    
                // Remettre l'état graphique de la cellule à zéro
                Button cellButton = cells[row][col];
                cellButton.setGraphic(null); // Vider complètement
    
                Label mainNumber = new Label();
                mainNumber.setFont(new Font(18));
    
                Text annotationText = new Text();
                annotationText.setFont(new Font(10));
    
                // Pour être sûr, vider tout historique d'annotation côté graphique
                List<String> annotations = new ArrayList<>();
    
                // Réattacher le comportement d'interaction pour que tout soit neuf
                final int r = row;
                final int c = col;
    
                setupCellInteraction(cellButton, r, c, annotations, mainNumber, annotationText);
    
                // Par défaut : Afficher la cellule selon son type
                if (cellData instanceof FixCell) {
                    int number = ((FixCell) cellData).getNumber();
                    mainNumber.setText(number == 0 ? "" : String.valueOf(number));
                    cellButton.setGraphic(mainNumber);
                } else if (cellData instanceof FlexCell) {
                    cellButton.setGraphic(mainNumber); // Cellule vide par défaut
                }
            }
        }
    }

    private void resetButton() {
        numberSelection.resetSelectedNumber();
        numberSelection.clearSelection();
        
        toolsPanel.setEraseButtonOff();
        toolsPanel.setPencilButtonOff();
    }

    public void resetInterface() {
        resetGrid();
        resetButton();
    }
}