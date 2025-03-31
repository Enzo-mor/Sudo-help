package grp6.intergraph;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

/**
 * Classe ToolsPanel
 * Classe representant un panneau d'outils permettant a l'utilisateur de choisir entre les modes gomme et annotation.
 * Le panneau contient deux boutons : un pour activer le mode gomme et un autre pour activer le mode annotation.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 * @see NumberSelection
 * @see StyledContent
 * @see SudokuDisplay
 * @see SudokuGame
 * @see SudokuGrid
 */
public class ToolsPanel {
    
    /**
     * Conteneur VBox qui organise les outils (boutons) dans une colonne.
     */
    private final VBox tools;

    /**
     * Indicateur si le mode gomme est active.
     */
    private boolean eraseMode = false;

    /**
     * Indicateur si le mode annotation est active.
     */
    private boolean annotationMode = false;

    /**
     * Bouton pour activer le mode gomme.
     */
    private final Button eraseButton;

    /**
     * Bouton pour activer le mode annotation.
     */
    private final Button pencilButton;


    /**
     * Constructeur qui initialise le panneau d'outils avec les boutons gomme et annotation.
     * Il ajoute les icones et definit les actions des boutons pour activer/desactiver les modes.
     */
    public ToolsPanel() {
        this.tools = new VBox(15);
        tools.setAlignment(Pos.CENTER);

        // Initialisation des icônes pour la gomme et le crayon
        ImageView binIcon = new ImageView(new Image(getClass().getResourceAsStream("/bin.png")));
        binIcon.setFitWidth(40);
        binIcon.setPreserveRatio(true);

        ImageView pencilIcon = new ImageView(new Image(getClass().getResourceAsStream("/pencil.png")));
        pencilIcon.setFitWidth(40);
        pencilIcon.setPreserveRatio(true);

        // Declaration des boutons pour l'effaceur et l'annotation
        eraseButton = new Button();
        pencilButton = new Button();

        eraseButton.setGraphic(binIcon);
        StyledContent.styleButton(eraseButton);
        eraseButton.setOnAction(e -> {
            boolean newMode = !getEraseMode();
            SudokuGame.resetTimer();
            
            if (newMode) {
                setEraseButtonOn();
                setPencilButtonOff();
            } else {
                setEraseButtonOff();
            }
        });
        
        pencilButton.setGraphic(pencilIcon);
        StyledContent.styleButton(pencilButton);
        pencilButton.setOnAction(e -> {
            boolean newMode = !getAnnotationMode();
            SudokuGame.resetTimer();

            if (newMode) {
                setPencilButtonOn();
                setEraseButtonOff();
            } else {
                setPencilButtonOff();
            }
        });

        tools.getChildren().addAll(eraseButton, pencilButton);
    }

    /**
     * Retourne le conteneur VBox qui contient les outils (boutons).
     * 
     * @return Le conteneur VBox avec les boutons d'outils.
     */
    public VBox getTools() {
        return tools;
    }

    /**
     * Retourne le bouton correspondant a l'effaceur.
     * 
     * @return Le bouton pour activer le mode gomme.
     */
    public Button getEraseButton() {
        return (Button) tools.getChildren().get(0);
    }

    /**
     * Retourne le bouton correspondant au crayon.
     * 
     * @return Le bouton pour activer le mode annotation.
     */
    public Button getPencilButton() {
        return (Button) tools.getChildren().get(1);
    }

    /**
     * Retourne l'etat du mode gomme.
     * 
     * @return true si le mode gomme est active, sinon false.
     */
    public boolean getEraseMode() {
        return eraseMode;
    }

    /**
     * Retourne l'etat du mode annotation.
     * 
     * @return true si le mode annotation est active, sinon false.
     */
    public boolean getAnnotationMode() {
        return annotationMode;
    }
    
    /**
     * Active le bouton gomme et desactive le bouton crayon, activant ainsi le mode gomme.
     */
    public void setEraseButtonOn() {
        eraseMode = true;
        StyledContent.setActiveButton(eraseButton);
        StyledContent.setInactiveButton(pencilButton);

        // Reinitialisation de la grille de Sudoku
        SudokuDisplay.resetGrid(SudokuGrid.getGridPane());

        // Reinitialiser le nombre selectionne
        if(NumberSelection.getSelectedNumber() != null) {
            NumberSelection.resetSelectedNumber();
            NumberSelection.clearSelection();
        }
    }

    /**
     * Active le bouton crayon et desactive le bouton gomme, activant ainsi le mode annotation.
     * Reinitialise la grille de Sudoku et le nombre selectionne dans l'interface.
     */
    public void setPencilButtonOn() {
        annotationMode = true;
        StyledContent.setActiveButton(pencilButton);
        StyledContent.setInactiveButton(eraseButton); 

        // Reinitialisation de la grille de Sudoku
        SudokuDisplay.resetGrid(SudokuGrid.getGridPane());

        // Reinitialiser le nombre selectionne
        if(NumberSelection.getSelectedNumber() != null) {
            NumberSelection.resetSelectedNumber();
            NumberSelection.clearSelection();
        }
    }

    /**
     * Desactive le mode gomme et le bouton gomme.
     */
    public void setEraseButtonOff() {
        eraseMode = false;
        StyledContent.setInactiveButton(eraseButton);
        // Reinitialiser la cellule selectionnee
        SudokuGrid.setSelectedCell(null);
        SudokuGrid.setSelectedRow(-1);
        SudokuGrid.setSelectedCol(-1);
    }

    /**
     * Desactive le mode annotation et le bouton crayon.
     * Remet le style par defaut de la cellule selectionnee dans la grille de Sudoku.
     */
    public void setPencilButtonOff() {
        annotationMode = false;
        StyledContent.setInactiveButton(pencilButton);

        // Remettre le style de la cellule selectionne d'avant par default
        Button selectedCell = SudokuGrid.getSelectedCell();
        int selectedRow = SudokuGrid.getSelectedRow();
        int selectedCol = SudokuGrid.getSelectedCol();
        if (selectedCell != null) {
            selectedCell.setStyle((selectedRow / 3 + selectedCol / 3) % 2 == 0 ? "-fx-background-color: lightblue;" : "-fx-background-color: white;");
        }
    }
}