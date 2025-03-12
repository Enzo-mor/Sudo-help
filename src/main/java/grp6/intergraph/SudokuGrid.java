package grp6.intergraph;
import grp6.sudocore.*;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;


public class SudokuGrid {
    private GridPane grid;
    private ToolsPanel toolsPanel; // Panneau des outils
    private NumberSelection numberSelection; // Panneau de sélection des chiffres
    private Button[][] cells = new Button[9][9]; // Stocke les boutons des cellules
    private List<String>[][] annotations = new ArrayList[9][9];
    private Grid gridSudoku; // Grille de sudoku
    private Game actualGame;

    public SudokuGrid(NumberSelection numberSelection, Grid gridData, ToolsPanel toolsP, Game actualGame) {
        this.grid = new GridPane();
        this.toolsPanel = toolsP;
        this.numberSelection = numberSelection;
        this.gridSudoku = gridData;
        this.actualGame = actualGame;

        grid.setHgap(2);
        grid.setVgap(2);
        grid.setPadding(new Insets(10));


        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                // Initialisation du Bouton
                Button cell = new Button();
                cell.setMinSize(50, 50);
                cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                cell.setStyle((row / 3 + col / 3) % 2 == 0 ? "-fx-background-color: lightblue;" : "-fx-background-color: white;");
                
                // Initialisation des annotations
                annotations[row][col] = new ArrayList<>();

                Label mainNumber = new Label();
                mainNumber.setFont(new Font(18));
                
                Text annotationText = new Text();
                annotationText.setFont(new Font(10));
                
                cell.setGraphic(mainNumber);
                
                setupCellInteraction(cell, row, col, mainNumber, annotationText);
                
                cells[row][col] = cell;
                grid.add(cell, col, row);
            }
        }
    }

    // ------------ Annotation ------------ // 

    /* Méthode pour ajouter une annotation */
    public void addAnnotationToCell(int row, int col, String annotation) {
        if (!annotations[row][col].contains(annotation)) {
            annotations[row][col].add(annotation);

            Button actualButton = getButton(row, col);
            if (actualButton != null) {
                Text annotationText = new Text();
                annotationText.setFont(new Font(10));
                setAnnotationDisplay(actualButton, row, col, annotationText);
            }
        }
    }

    /* Méthode pour retirer une annotation */
    public void removeAnnotationFromCell(int row, int col, String annotation) {
        annotations[row][col].remove(annotation);

        Button actualButton = getButton(row, col);
        if (actualButton != null) {
            Text annotationText = new Text();
            annotationText.setFont(new Font(10));
            setAnnotationDisplay(actualButton, row, col, annotationText);
        }
    }

    /* Méthode pour afficher les annotations dans le bouton */
    public void setAnnotationDisplay(Button cellButton, int row, int col, Text annotationText) {
        StringBuilder formattedAnnotations = new StringBuilder();
        String[] positions = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        
        for (int i = 0; i < 9; i++) {
            if (annotations[row][col].contains(positions[i])) {
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

    // ------------ Nombre ------------ //

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
            else {
                labelTemp.setText(String.format("%d", oldNumber));
                // TODO si le undo est une annotation
                // TODO affichage de l'annotation pas formater (affichage comme un nombre et non en 3*3)
                textTemp.setText("");
                cell.setGraphic(labelTemp);
            }
            
            cells[row][col] = cell;
        }
    }

    public void setCellsColorError(List<int[]> cellsToColor) {
        for (int[] cell : cellsToColor) {
            int row = cell[0];
            int col = cell[1];

            System.out.println("Recherche du bouton à la position (" + row + ", " + col + ")");

            // Recherche du bouton correspondant à la position (row, col)
            Node node = grid.getChildren().stream()
                    .filter(n -> GridPane.getRowIndex(n) != null && GridPane.getColumnIndex(n) != null)
                    .filter(n -> GridPane.getRowIndex(n) == row && GridPane.getColumnIndex(n) == col)
                    .findFirst().orElse(null);

            if (node == null) {
                System.out.println("Aucun nœud trouvé à (" + row + ", " + col + ")");
            } else if (!(node instanceof Button)) {
                System.out.println("Le nœud trouvé à (" + row + ", " + col + ") n'est pas un bouton");
            } else {
                System.out.println("Modification du bouton à (" + row + ", " + col + ")");
                ((Button) node).setStyle("-fx-background-color: red !important;");
            }
        }
    }

    public void setCellsColorDefault() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                cells[i][j].setStyle((i / 3 + j / 3) % 2 == 0 ? "-fx-background-color: lightblue;" : "-fx-background-color: white;");
            }
        }
    }

    public void setNumberDisplay(Button cellButton, String selectedStr, Label mainNumber, Text annotationText) {
        mainNumber.setText(selectedStr);
        mainNumber.setStyle("-fx-text-fill: blue;");
        annotationText.setText("");  // Effacer les annotations
        cellButton.setGraphic(mainNumber);  // Afficher le nombre principal
    }
    
    private void setupCellInteraction(Button cellButton, final int r, final int c, Label mainNumber, Text annotationText) {
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
                    if (!annotations[r][c].contains(selectedStr)) {
                        addAnnotationToCell(r, c, selectedStr);
                        actualGame.addAnnotation(r, c, Integer.valueOf(selectedStr));
                    } else {
                        removeAnnotationFromCell(r, c, selectedStr);
                        actualGame.removeAnnotation(r, c, Integer.valueOf(selectedStr));
                    }
                }
            } else if (selectedStr != null) {
                setNumberDisplay(cellButton, selectedStr, mainNumber, annotationText);
                //TODO: verifier si le nombre que l'on veut mettre n;est pas deja dans la cell
                actualGame.addNumber(r,c,Integer.valueOf(selectedStr));
            }
        });
    }


    // ------------ Méthodes de mise à jour de la grille ------------ //

    public void setGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cell = gridSudoku.getCell(row, col);
                Button cellButton = cells[row][col];
                Label mainNumber = new Label();
                Text annotationText = new Text();

                if (cell instanceof FixCell) {
                    FixCell fixCell = (FixCell) cell;
                    if (fixCell.getNumber() == 0) {
                        mainNumber.setText("");
                    } else {
                        mainNumber.setFont(new Font(18));
                        mainNumber.setText(String.valueOf(fixCell.getNumber()));
                    }                    
                    annotationText.setText(""); 
                    cellButton.setGraphic(mainNumber);
                } else if (cell instanceof FlexCell) {
                    setAnnotationDisplay(cellButton, row, col, annotationText);
                }
            }
        }
    }

    private void resetGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cellData = gridSudoku.getCell(row, col);
    
                Button cellButton = cells[row][col];
                cellButton.setGraphic(null);
    
                Label mainNumber = new Label();
                mainNumber.setFont(new Font(18));
    
                Text annotationText = new Text();
                annotationText.setFont(new Font(10));
    
                annotations[row][col].clear();
    
                if (cellData instanceof FixCell) {
                    int number = ((FixCell) cellData).getNumber();
                    mainNumber.setText(number == 0 ? "" : String.valueOf(number));
                    cellButton.setGraphic(mainNumber);
                } else if (cellData instanceof FlexCell) {
                    cellButton.setGraphic(mainNumber);
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


    // ------------ Getters ------------ //

    public GridPane getGridPane() {
        return grid;
    }


    public void resetInterface() {
        resetGrid();
        resetButton();
    }

    public Game getGame() {
        return actualGame;
    }

    public Button getButton(int r, int c) {
        if (r >= 0 && r < 9 && c >= 0 && c < 9) {
            return cells[r][c]; // Retourne le bouton correspondant
        }
        return null; // Retourne null si les coordonnées sont invalides
    }
}