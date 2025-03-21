package grp6.intergraph;
import grp6.sudocore.*;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

import java.util.ArrayList;
import java.util.List;

public class SudokuGrid {
    private static GridPane grid;
    private final ToolsPanel toolsPanel; // Panneau des outils
    private static final Button[][] cells = new Button[9][9]; // Stocke les boutons des cellules
    @SuppressWarnings("unchecked")
    private final List<String>[][] annotations = new ArrayList[9][9];
    private final boolean[][] actionEraser = new boolean[9][9];
    private static Grid gridSudoku; // Grille de sudoku
    private static Game actualGame;

    private static Button selectedCell = null; // Cellule sélectionnée
    private static int selectedRow = -1; // Ligne de la cellule sélectionnée
    private static int selectedCol = -1; // Colonne de la cellule sélectionnée

    /**
     * Constructeur de la classe SudokuGrid
     * @param numberSelection Panneau de sélection des chiffres [NumberSelection]
     * @param gridData [GridData] 
     * @param toolsP [ToolsPanel] 
     * @param actualGameParam [Game]
     */
    public SudokuGrid(ToolsPanel toolsP, Game actualGameParam) {
        grid = new GridPane();
        this.toolsPanel = toolsP;
        actualGame = actualGameParam;
        gridSudoku = actualGameParam.getGrid();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.actionEraser[i][j] = false;
            }
        }

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

    private static void updateCellDisplay(Button cellButton, Label mainNumber, Text annotationText, String numberStr) {
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

    private static void resetCellDisplay(Button cellButton, Label mainNumber, Text annotationText) {
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

            Label mainNumber;
            Text annotationText;

            if (cellButton.getGraphic() instanceof Label) {
                mainNumber = (Label) cellButton.getGraphic();
            } else {
                mainNumber = new Label();
                mainNumber.setFont(new Font(18));
            }

            annotationText = new Text();
            annotationText.setFont(new Font(10));

            if (oldNumber == 0) {
                resetCellDisplay(cellButton, mainNumber, annotationText);  // Réinitialiser si le nombre est 0
            } else {
                updateCellDisplay(cellButton, mainNumber, annotationText, String.valueOf(oldNumber));  // Afficher le nombre
            }
        }
    }

    public void setNumberDisplay(Button cellButton, String selectedStr, Label mainNumber, Text annotationText) {
        updateCellDisplay(cellButton, mainNumber, annotationText, selectedStr);
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


    // ------------ Bouton ------------ //

    // Méthode pour colorier les cellules en rouge si elles contiennent des erreurs
    public void setCellsColorError(List<int[]> eval) {
        for (int[] position : eval) {
            int row = position[0];
            int col = position[1];
            Button cellButton = getButton(row, col);
    
            if (cellButton != null) {
                // Appliquer la couleur rouge pour signaler une erreur
                cellButton.setStyle("-fx-background-color: #FF6F91;");
    
                // Récupérer le graphique associé au bouton
                Node graphic = cellButton.getGraphic();
    
                // Vérifier si le graphique est un Label ou un Text
                Label mainNumber = null;
                Text annotationText = null;
    
                if (graphic instanceof Label) {
                    mainNumber = (Label) graphic;
                } else if (graphic instanceof Text) {
                    annotationText = (Text) graphic;
                }
    
                // Si mainNumber est null, il faudra en créer un nouveau Label
                if (mainNumber == null) {
                    mainNumber = new Label();
                }
    
                // Si annotationText est null, il faudra en créer un nouveau Text
                if (annotationText == null) {
                    annotationText = new Text();
                }
    
                // Récupérer le texte du Label
                String numberStr = mainNumber.getText();
    
                // Vérifier si le graphique est un nombre ou une annotation
                if (numberStr != null && !numberStr.isEmpty()) {
                    // Si c'est un nombre, mettre à jour l'affichage avec le nombre
                    updateCellDisplay(cellButton, mainNumber, annotationText, numberStr);
                } else {
                    // Si c'est une annotation, mettre à jour l'affichage avec les annotations
                    setAnnotationDisplay(cellButton, row, col, annotationText);
                }
            }
        }
    }
    
    // Méthode pour remettre les cellules à leur couleur d'origine
    public static void setCellsColorDefault(List<int[]> eval) {
        for (int[] position : eval) {
            int row = position[0];
            int col = position[1];
            Button cellButton = getButton(row, col);

            if (cellButton != null) {
                String defaultColor = (row / 3 + col / 3) % 2 == 0 ? "-fx-background-color: lightblue;" : "-fx-background-color: white;";
                cellButton.setStyle(defaultColor);
    
                // Récupérer le graphique associé au bouton
                Node graphic = cellButton.getGraphic();

                Label mainNumber = null;
                Text annotationText = null;

                // Vérification du type de graphique attaché au bouton
                if (graphic instanceof Label) {
                    mainNumber = (Label) graphic;
                } else if (graphic instanceof Text) {
                    annotationText = (Text) graphic;
                }

                // Si mainNumber est null, en créer un nouveau Label
                if (mainNumber == null) {
                    mainNumber = new Label();
                }

                // Si annotationText est null, en créer un nouveau Text
                if (annotationText == null) {
                    annotationText = new Text();
                }

                // Récupérer le texte du Label
                String numberStr = mainNumber.getText();

                updateCellDisplay(cellButton, mainNumber, annotationText, numberStr);
            }
        }
    }

    // ------------ Interaction avec les cellules ------------ //
    
    private void setupCellInteraction(Button cellButton, final int r, final int c, Label mainNumber, Text annotationText) {
        cellButton.setOnAction(e -> {
            if (toolsPanel.getAnnotationMode()) {

                // Remettre le style de la cellule selectionné d'avant par default
                if (selectedCell != null) {
                    selectedCell.setStyle((selectedRow / 3 + selectedCol / 3) % 2 == 0 ? "-fx-background-color: lightblue;" : "-fx-background-color: white;");
                }

                // Mémoriser la cellule sélectionnée pour l'annotation
                selectedCell = cellButton;
                selectedRow = r;
                selectedCol = c;

                // Mettre un contour à la cellule sélectionnée
                cellButton.setStyle((selectedRow / 3 + selectedCol / 3) % 2 == 0 ? "-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 1px;" : "-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1px;");

            } else {
                // Si pas en mode annotation, comportement normal
                String selectedStr = NumberSelection.getSelectedNumber();
                Cell currentCell = gridSudoku.getCell(r, c);

                if (!currentCell.isEditable()) {
                    
                    // Appeler la méthode pour surligner la ligne et la colonne, si le paramètre est activé
                    if(Settings.getHighlightRowCol())
                        SudokuDisplay.highlightRowAndColumn(grid, r, c);
                        
                    System.out.println("Cette cellule est fixe et ne peut pas être modifiée.");
                    return;
                }
        
                if (selectedStr != null) {
                    Integer selectedInteger = Integer.valueOf(selectedStr);
                    if (mainNumber.getText().isEmpty() || !Integer.valueOf(mainNumber.getText()).equals(selectedInteger)) {
                        actualGame.addNumber(r, c, selectedInteger);
                        if(Settings.getHighlightNumbers())
                            SudokuDisplay.highlightSameNumbers(getGridPane(), getGrid(), selectedInteger);
                    }
                    setNumberDisplay(cellButton, selectedStr, mainNumber, annotationText);
                }

                if (toolsPanel.getEraseMode()) {
                    actualGame.deleteActionsOfCell(r, c);
                    modifyStateEraser(r, c, true);
                    resetCellDisplay(cellButton, mainNumber, annotationText);
                }

                
                // Appeler la méthode pour surligner la ligne et la colonne, si le paramètre est activé
                if(Settings.getHighlightRowCol())
                    SudokuDisplay.highlightRowAndColumn(grid, r, c);
            }
        });
    }


    // ------------ Méthodes de mise à jour de la grille ------------ //

    public void loadGrid(Grid gridSudokuBase) {
        if (gridSudokuBase == null) {
            System.err.println("Error: gridSudokuBase is null!");
            return; // Exit early to prevent a NullPointerException
        }
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Cell cellGame = actualGame.getGrid().getCell(i, j);
                Cell cellBDD = gridSudokuBase.getCell(i, j);
                Button cellButton = cells[i][j]; // Récupère le bouton de la cellule actuelle
                Label mainNumber = new Label();
                mainNumber.setFont(new Font(18));
                Text annotationText = new Text();

                if (cellGame.getNumber() == cellBDD.getNumber()) {
                    if (cellGame instanceof FlexCell) {
                        if (cellGame.hasAnnotations()) {
                            setAnnotationDisplay(cellButton, i, j, annotationText);
                            for (Integer annotation : cellGame.getAnnotations()) {
                                addAnnotationToCell(i, j, annotation.toString());
                            }
                        }
                        
                    } else {
                        if (cellGame.getNumber() == 0) {
                            mainNumber.setText("");
                        } else {
                            mainNumber.setText(String.valueOf(cellGame.getNumber()));
                            
                        }
                        annotationText.setText("");                    
                        cellButton.setGraphic(mainNumber);
                        
                    }
                } else {
                    setNumberDisplay(cellButton, String.valueOf(cellGame.getNumber()), mainNumber, annotationText);
                }
            }
        }
    }
    /*
    * Methode pour recharger une grille deja demarree
    * @param game Partie du joueur associee a la grille
    */
    public void reload(Grid originalGrid) {
        setGrid();
        List<Action> actions = actualGame.getActions();
        List<Action> actionsCopy = new ArrayList<>(actions);
        actualGame.clearActions();
        for (int i = 0 ; i< actions.size(); i++){
            actualGame.executeAction(actions.get(i));
        }
        actualGame.clearActions();
        actualGame.getActions().addAll(actionsCopy);
        loadGrid(originalGrid);
    }

    public void setGrid() {
        Grid newGrid = DBManager.getGrid(actualGame.getGrid().getId());
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Cell cell = newGrid.getCell(row, col);
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
    
    private void resetGrid() {
        clear();
        setGrid();
    }

    private void resetButton() {
        NumberSelection.resetSelectedNumber();
        NumberSelection.clearSelection();
        
        toolsPanel.setEraseButtonOff();
        toolsPanel.setPencilButtonOff();
    }

    public void resetInterface() {
        resetGrid();
        resetButton();
        SudokuDisplay.resetGrid(getGridPane());
    }

    // ------------ Getters ------------ //

    public static GridPane getGridPane() {
        return grid;
    }

    public static Grid getGrid() {
        return actualGame.getGrid();
    }

    public Game getGame() {
        return actualGame;
    }

    public static Button getButton(int r, int c) {
        if (r >= 0 && r < 9 && c >= 0 && c < 9) {
            return cells[r][c]; // Retourne le bouton correspondant
        }
        return null; // Retourne null si les coordonnées sont invalides
    }

    public static Button getSelectedCell() {
        return selectedCell;
    }
    
    public static int getSelectedRow() {
        return selectedRow;
    }
    
    public static int getSelectedCol() {
        return selectedCol;
    }
    
    public boolean hasAnnotation(int row, int col, String annotation) {
        return annotations[row][col].contains(annotation);
    }

    public boolean hasAnnotations(int row, int col) {
        return !annotations[row][col].isEmpty();
    }

    public boolean isLastActionEraser(int r, int c){
        return this.actionEraser[r][c];
    }

    public void modifyStateEraser(int r, int c, boolean state){
        this.actionEraser[r][c] = state;
    }

    // ------------ Setters ------------ //

    public static void setGame(Game game) {
        actualGame = game;
    }
}