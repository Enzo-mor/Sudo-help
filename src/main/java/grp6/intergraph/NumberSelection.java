package grp6.intergraph;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

/**
 * Classe NumberSelection
 * Cette classe gere la selection des chiffres dans l'interface Sudoku.
 * Elle permet de choisir un chiffre et de l'afficher dans la grille,
 * en tenant compte du mode d'annotation et du marquage des nombres identiques.
 * 
 * @author Perron Nathan
 * @author Rasson Emma
 * @see SudokuGrid
 * @see ToolsPanel
 * @see StyledContent
 * @see Settings
 * @see SudokuDisplay
 */
public class NumberSelection {

    /** 
     * Conteneur principal pour la selection des nombres 
     */
    private static VBox numberSelection;

    /** 
     * Nombre actuellement selectionne 
     */
    private static String selectedNumber = null;

    /** 
     * Panneau d'outils associe 
     */
    private final ToolsPanel toolsPanel;

    /** 
     * Reference a la grille Sudoku 
     */
    private SudokuGrid sudokuGrid;


    /**
     * Constructeur de NumberSelection.
     * 
     * @param toolsPanel Panneau d'outils associe [ToolsPanel]
     */
    public NumberSelection(ToolsPanel toolsPanel) {
        this.toolsPanel = toolsPanel;

        numberSelection = new VBox(5);
        numberSelection.setAlignment(Pos.CENTER);
    
        HBox topNumbers = new HBox(5);
        topNumbers.setAlignment(Pos.CENTER);
        Button[] numButtonsTop = new Button[5];
    
        for (int i = 1; i <= 5; i++) {
            final int index = i - 1;
            numButtonsTop[index] = createNumberButton(i);
            topNumbers.getChildren().add(numButtonsTop[index]);
        }
    
        HBox bottomNumbers = new HBox(5);
        bottomNumbers.setAlignment(Pos.CENTER);
        Button[] numButtonsBottom = new Button[4];
    
        for (int i = 6; i <= 9; i++) {
            final int index = i - 6;
            numButtonsBottom[index] = createNumberButton(i);
            bottomNumbers.getChildren().add(numButtonsBottom[index]);
        }
    
        numberSelection.getChildren().addAll(topNumbers, bottomNumbers);
    }

    /**
     * Cree un bouton representant un chiffre et lui associe les styles et actions.
     * 
     * @param number Chiffre du bouton [int]
     * @return Bouton configure [Button]
     */
    private Button createNumberButton(int number) {
        Button button = new Button(String.valueOf(number));
        button.setMinSize(50, 50);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        StyledContent.styleButton(button);
    
        button.setOnAction(e -> {
            selectNumber(button);
            if (Settings.getHighlightNumbers()) {
                SudokuDisplay.highlightSameNumbers(SudokuGrid.getGridPane(), SudokuGrid.getGrid(), Integer.parseInt(getSelectedNumber()));
            }
        });
    
        return button;
    }

    /**
     * Selectionne un nombre en mettant a jour l'affichage et la grille Sudoku.
     * 
     * @param button Bouton selectionne [Button]
     */
    private void selectNumber(Button button) {
        String numberStr = button.getText();
        int number = Integer.parseInt(numberStr);
        Button numberButton = SudokuGrid.getSelectedCell();
    
        if (toolsPanel.getAnnotationMode() && sudokuGrid != null && numberButton != null) {
            handleAnnotationMode(numberStr, number, numberButton);
        } else {
            if (numberStr.equals(getSelectedNumber())) {
                StyledContent.setInactiveButton(button);
                setSelectedNumber(null);
                return;
            }
            
            var buttons = numberSelection.lookupAll(".button");
            buttons.forEach(node -> StyledContent.setInactiveButton((Button) node));
            StyledContent.setActiveButton(button);
            setSelectedNumber(numberStr);
        }
    }

    /**
     * Gere l'ajout et la suppression d'annotations dans une cellule Sudoku.
     * 
     * @param numberStr Nombre sous forme de chaîne [String]
     * @param number Nombre selectionne [int]
     * @param numberButton Bouton de la cellule Sudoku [Button]
     */
    private void handleAnnotationMode(String numberStr, int number, Button numberButton) {
        String cellText = numberButton.getText().trim();
    
        if (!numberButton.getChildrenUnmodifiable().isEmpty() && numberButton.getChildrenUnmodifiable().get(0) instanceof Label) {
            Label label = (Label) numberButton.getChildrenUnmodifiable().get(0);
            cellText = label.getText().trim();
        }
    
        if (cellText.isEmpty() || cellText.equals("0")) {
            int row = SudokuGrid.getSelectedRow();
            int col = SudokuGrid.getSelectedCol();
    
            if (!sudokuGrid.hasAnnotation(row, col, numberStr)) {
                sudokuGrid.addAnnotationToCell(row, col, numberStr);
                sudokuGrid.getGame().addAnnotation(row, col, number);
            } else {
                sudokuGrid.removeAnnotationFromCell(row, col, numberStr);
                sudokuGrid.getGame().removeAnnotation(row, col, number);
            }
        }
    }

    /**
     * Retourne le conteneur de selection des nombres.
     * 
     * @return VBox contenant les boutons de selection [VBox]
     */
    public VBox getNumberSelection() {
        return numberSelection;
    }

    /**
     * Retourne le nombre actuellement selectionne.
     * 
     * @return Nombre selectionne [String]
     */
    public static String getSelectedNumber() {
        return selectedNumber;
    }

    /**
     * Definit le nombre actuellement selectionne.
     * 
     * @param selectedNumber Nombre selectionne [String]
     */
    public void setSelectedNumber(String selectedNumber) {
        NumberSelection.selectedNumber = selectedNumber;
    }

    /**
     * Definit la grille Sudoku associee.
     * 
     * @param sudokuGrid Grille Sudoku associee [SudokuGrid]
     */
    public void setSudokuGrid(SudokuGrid sudokuGrid) {
        this.sudokuGrid = sudokuGrid;
    }

    /**
     * Reinitialise la selection du nombre.
     */
    public static void resetSelectedNumber() {
        selectedNumber = null;
    }

    /**
     * Efface la selection des boutons.
     */
    public static void clearSelection() {
        var buttons = numberSelection.lookupAll(".button");
        buttons.forEach(node -> ((Button) node).setStyle("")); 
    }
}