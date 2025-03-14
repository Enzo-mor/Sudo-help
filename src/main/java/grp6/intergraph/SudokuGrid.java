package grp6.intergraph;
import grp6.sudocore.*;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import javafx.util.converter.IntegerStringConverter;


public class SudokuGrid {
    private final GridPane grid;
    private final ToolsPanel toolsPanel; // Panneau des outils
    private final NumberSelection numberSelection; // Panneau de sélection des chiffres
    private final Button[][] cells = new Button[9][9]; // Stocke les boutons des cellules
    private final List<String>[][] annotations = new ArrayList[9][9];
    private final Grid gridSudoku; // Grille de sudoku
    private final Game actualGame;

    /**
     * Constructeur de la classe SudokuGrid
     * @param numberSelection Panneau de sélection des chiffres [NumberSelection]
     * @param gridData [GridData] 
     * @param toolsP [ToolsPanel] 
     * @param actualGame [Game]
     */
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

    // ------------ Nombre ------------ // 

    private void updateCellDisplay(Button cellButton, Label mainNumber, Text annotationText, String numberStr) {
        if (numberStr != null) {
            mainNumber.setText(numberStr);
            mainNumber.setStyle("-fx-text-fill: blue;");

            if (annotationText != null) {
                annotationText.setText("");  // Effacer les annotations
            } else {
                annotationText = new Text();  // Créer un nouveau Text si nécessaire
            }

            cellButton.setGraphic(mainNumber);  // Afficher le nombre principal
        } else {
            resetCellDisplay(cellButton, mainNumber, annotationText); // Si pas de nombre, réinitialiser
        }
    }    

    private void resetCellDisplay(Button cellButton, Label mainNumber, Text annotationText) {
        mainNumber.setText("");
        if (annotationText == null) {
            annotationText = new Text();  // Créer un nouveau Text si nécessaire
        }
        annotationText.setText("");
        cellButton.setGraphic(mainNumber);  // Revenir au mode principal (sans annotations)
    }

    public void setCellDisplay(int row, int col, int oldNumber) {
        if (row >= 0 && col >= 0 && oldNumber >= 0) {
            Button cellButton = cells[row][col];
            Label mainNumber = new Label();
            mainNumber.setFont(new Font(18));
            Text annotationText = new Text();
            annotationText.setFont(new Font(10));

            if (oldNumber == 0) {
                resetCellDisplay(cellButton, mainNumber, annotationText);  // Réinitialiser si le nombre est 0
            } else {
                updateCellDisplay(cellButton, mainNumber, annotationText, String.valueOf(oldNumber));  // Afficher le nombre
            }

            cells[row][col] = cellButton;
        }
    }

    public void setNumberDisplay(Button cellButton, String selectedStr, Label mainNumber, Text annotationText) {
        updateCellDisplay(cellButton, mainNumber, annotationText, selectedStr);
    }

    // Méthode pour colorier les cellules en rouge si elles contiennent des erreurs
    public void setCellsColorError(List<int[]> eval) {
        for (int[] position : eval) {
            int row = position[0];
            int col = position[1];
            Button cellButton = getButton(row, col);
            
            if (cellButton != null) {
                // Appliquer la couleur rouge pour signaler une erreur
                cellButton.setStyle("-fx-background-color: #FF6F91;");

                // Mettre à jour l'affichage de la cellule avec les annotations si nécessaire
                Label mainNumber = (Label) cellButton.getGraphic();
                Text annotationText = (Text) mainNumber.getGraphic();
                String numberStr = mainNumber.getText();

                updateCellDisplay(cellButton, mainNumber, annotationText, numberStr);
            }
        }
    }
    
    
    // Méthode pour remettre les cellules à leur couleur d'origine
    public void setCellsColorDefault(List<int[]> eval) {
        for (int[] position : eval) {
            int row = position[0];
            int col = position[1];
            Button cellButton = getButton(row, col);

            if (cellButton != null) {
                String defaultColor = (row / 3 + col / 3) % 2 == 0 ? "-fx-background-color: lightblue;" : "-fx-background-color: white;";
                cellButton.setStyle(defaultColor);
    
                // Mettre à jour l'affichage des annotations ou des numéros
                Label mainNumber = (Label) cellButton.getGraphic();
                Text annotationText = (Text) mainNumber.getGraphic();
                String numberStr = mainNumber.getText();

                updateCellDisplay(cellButton, mainNumber, annotationText, numberStr);
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

        if (annotationText == null) {
            annotationText = new Text();  // Toujours vérifier ou créer un nouveau Text
            annotationText.setFont(new Font(10));
        }

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

    // ------------ Interaction avec les cellules ------------ //
    
    private void setupCellInteraction(Button cellButton, final int r, final int c, Label mainNumber, Text annotationText) {
        cellButton.setOnAction(e -> {
            String selectedStr = numberSelection.getSelectedNumber();

            Cell currentCell = gridSudoku.getCell(r, c);

            if (!currentCell.isEditable()) {
                System.out.println("Cette cellule est fixe et ne peut pas être modifiée.");
                return;
            }
    
            if (selectedStr != null) {
                Integer selectedInteger = Integer.valueOf(selectedStr);
                if(toolsPanel.getAnnotationMode()){
                    if (mainNumber.getText().isEmpty()) {
                        if (!annotations[r][c].contains(selectedStr)) {
                            addAnnotationToCell(r, c, selectedStr);
                            actualGame.addAnnotation(r, c, selectedInteger);
                        } else {
                            removeAnnotationFromCell(r, c, selectedStr);
                            actualGame.removeAnnotation(r, c, selectedInteger);
                        }
                    }
                }
                else{
                    //TODO: verifier si le nombre que l'on veut mettre n;est pas deja dans la cell
                    if (mainNumber.getText().isEmpty() || !Integer.valueOf(mainNumber.getText()).equals(selectedInteger)) {
                        actualGame.addNumber(r, c, selectedInteger);
                    }
                    
                    setNumberDisplay(cellButton, selectedStr, mainNumber, annotationText);
                }
            }

            if (toolsPanel.getEraseMode()) {
                resetCellDisplay(cellButton, mainNumber, annotationText);
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

                if (cell instanceof FixCell fixCell) {
                    if (fixCell.getNumber() == 0) {
                        mainNumber.setText("");
                    } else {
                        mainNumber.setFont(new Font(18));
                        mainNumber.setText(String.valueOf(fixCell.getNumber()));
                    }                    
                    annotationText.setText(""); 
                    cellButton.setGraphic(mainNumber);
                } else {
                    setAnnotationDisplay(cellButton, row, col, annotationText);
                }
            }
        }
    }

    private void resetGrid() {
        clear();
        setGrid();
    }

    /**
     * Efface la grille de Sudoku en réinitialisant l'affichage et les annotations de chaque cellule.
     * Parcourt chaque cellule de la grille 9x9, vide les annotations de chaque cellule,
     * et réinitialise l'affichage de la cellule à son état par défaut.
     */
    private void clear() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Button cellButton = cells[row][col]; // Récupère le bouton de la cellule actuelle
                Node graphic = cellButton.getGraphic(); // Récupère le graphique associé au bouton
                Label mainNumber = (graphic instanceof Label) ? (Label) graphic : new Label(); // Vérifie si le graphique est un Label, sinon crée un nouveau Label

                annotations[row][col].clear(); // Vide les annotations de la cellule actuelle
                resetCellDisplay(cellButton, mainNumber, null); // Réinitialise l'affichage de la cellule
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