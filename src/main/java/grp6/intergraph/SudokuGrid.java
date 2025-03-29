package grp6.intergraph;
import grp6.sudocore.*;
import grp6.syshelp.Technique;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe SudokuGrid
 * Cette classe represente une grille de Sudoku dans l'interface utilisateur. 
 * Elle gere l'affichage des cellules, l'ajout d'annotations, les interactions avec l'utilisateur, 
 * ainsi que la mise a jour des actions de la partie.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 * @see ToolsPanel
 * @see Game
 * @see Settings
 * @see SudokuDisplay
 * @see NumberSelection
 * @see DBManager
 */
public class SudokuGrid {

    /**
     * La grille principale contenant toutes les cellules du Sudoku.
     */
    private static GridPane grid;

    /**
     * Panneau des outils permettant d'interagir avec la grille (par exemple, pour ajouter des annotations).
     */
    private final ToolsPanel toolsPanel;

    /**
     * Tableau de boutons representant les cellules de la grille de Sudoku.
     */
    private static final Button[][] cells = new Button[9][9];

    /**
     * Tableau d'annotations où chaque cellule contient une liste d'annotations (par exemple, des numeros possibles).
     */
    @SuppressWarnings("unchecked")
    private final List<String>[][] annotations = new ArrayList[9][9];

    /**
     * Tableau indiquant si une cellule a ete effacee (utilise pour gerer les actions de l'effaceur).
     */
    private final boolean[][] actionEraser = new boolean[9][9];

    /**
     * La grille de Sudoku contenant les valeurs actuelles du jeu.
     */
    private static Grid gridSudoku;

    /**
     * Le jeu en cours.
     */
    private static Game actualGame;

    /**
     * Cellule actuellement selectionnee par l'utilisateur (utilisee pour l'annotation ou la modification).
     */
    private static Button selectedCell = null;

    /**
     * Ligne de la cellule actuellement selectionnee.
     */
    private static int selectedRow = -1;

    /**
     * Colonne de la cellule actuellement selectionnee.
     */
    private static int selectedCol = -1;

    /**
     * Booleen pour savoir si c'est le mode apprentissage qui appel les methodes
     */
    private static boolean isLearningMode = false;

    /**
     * Technique de recherche pour trouver les solutions.
     */
    private static Technique technique;

    /**
     * Constructeur de la classe SudokuGrid.
     * Il initialise la grille, les outils de gestion et les interactions.
     * 
     * @param toolsP Panneau des outils pour l'interaction avec la grille.
     * @param actualGameParam Le jeu en cours.
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

        // Initialisation des cellules de la grille
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
                
                // Configuration de l'interaction avec les cellules
                setupCellInteraction(cell, row, col, mainNumber, annotationText);
                
                cells[row][col] = cell;
                grid.add(cell, col, row);
            }
        }
    }

    // ------------ Nombre ------------ // 

    /**
     * Met a jour l'affichage d'une cellule avec un nombre donne.
     * 
     * @param cellButton Le bouton representant la cellule.
     * @param mainNumber L'etiquette pour afficher le nombre.
     * @param annotationText Le texte des annotations.
     * @param numberStr Le nombre a afficher sous forme de chaîne de caracteres.
     */
    private static void updateCellDisplay(Button cellButton, Label mainNumber, Text annotationText, String numberStr) {
        if (numberStr != null) {
            mainNumber.setText(numberStr);
            mainNumber.setStyle("-fx-text-fill: blue;");

            if (annotationText != null) {
                annotationText.setText("");  // Effacer les annotations
            }

            cellButton.setGraphic(mainNumber);  // Afficher le nombre principal
        } else {
            resetCellDisplay(cellButton, mainNumber, annotationText); // Si pas de nombre, reinitialiser
        }
    }    

    /**
     * Reinitialise l'affichage d'une cellule en enlevant son nombre et ses annotations.
     * 
     * @param cellButton Le bouton representant la cellule.
     * @param mainNumber L'etiquette du nombre.
     * @param annotationText Le texte des annotations.
     */
    private static void resetCellDisplay(Button cellButton, Label mainNumber, Text annotationText) {
        mainNumber.setText("");
        if (annotationText == null) {
            annotationText = new Text();  // Creer un nouveau Text si necessaire
        }
        annotationText.setText("");
        cellButton.setGraphic(mainNumber);  // Revenir au mode principal (sans annotations)
    }

    /**
     * Met a jour l'affichage d'une cellule avec un ancien numero.
     * 
     * @param row La ligne de la cellule.
     * @param col La colonne de la cellule.
     * @param oldNumber Le numero a afficher.
     */
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
                resetCellDisplay(cellButton, mainNumber, annotationText);  // Reinitialiser si le nombre est 0
            } else {
                updateCellDisplay(cellButton, mainNumber, annotationText, String.valueOf(oldNumber));  // Afficher le nombre
            }
        }
    }

    /**
     * Met a jour l'affichage d'une cellule avec un nombre selectionne.
     * 
     * @param cellButton Le bouton representant la cellule.
     * @param selectedStr Le nombre selectionne sous forme de chaîne de caracteres.
     * @param mainNumber L'etiquette du nombre.
     * @param annotationText Le texte des annotations.
     */
    public void setNumberDisplay(Button cellButton, String selectedStr, Label mainNumber, Text annotationText) {
        updateCellDisplay(cellButton, mainNumber, annotationText, selectedStr);
    }


    // ------------ Annotation ------------ // 

    /**
     * Ajoute une annotation a une cellule specifique.
     * 
     * @param row La ligne de la cellule.
     * @param col La colonne de la cellule.
     * @param annotation L'annotation a ajouter.
     */
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

    /**
     * Retire une annotation d'une cellule specifique.
     * 
     * @param row La ligne de la cellule.
     * @param col La colonne de la cellule.
     * @param annotation L'annotation a retirer.
     */
    public void removeAnnotationFromCell(int row, int col, String annotation) {
        annotations[row][col].remove(annotation);

        Button actualButton = getButton(row, col);
        if (actualButton != null) {
            Text annotationText = new Text();
            annotationText.setFont(new Font(10));
            setAnnotationDisplay(actualButton, row, col, annotationText);
        }

    }

    /**
     * Met a jour l'affichage des annotations dans la cellule donnee.
     * 
     * @param cellButton Le bouton representant la cellule.
     * @param row La ligne de la cellule.
     * @param col La colonne de la cellule.
     * @param annotationText Le texte des annotations.
     */
    public void setAnnotationDisplay(Button cellButton, int row, int col, Text annotationText) {

        if (annotationText == null) {
            annotationText = new Text();  // Toujours verifier ou creer un nouveau Text
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

    /**
     * Met a jour la couleur des cellules de la grille en rouge pour signaler une erreur.
     * Cette methode prend en parametre une liste de positions (ligne, colonne) des cellules erronees,
     * et applique un fond rouge sur les cellules correspondantes tout en mettant a jour leur affichage.
     * 
     * @param eval Liste des positions des cellules erronees, chaque position etant un tableau de deux entiers representant la ligne et la colonne.
     */
    public void setCellsColorError(List<int[]> eval) {
        for (int[] position : eval) {
            int row = position[0];
            int col = position[1];
            Button cellButton = getButton(row, col);
    
            if (cellButton != null) {
                // Appliquer la couleur rouge pour signaler une erreur
                cellButton.setStyle("-fx-background-color: #FF6F91;");
    
                // Recuperer le graphique associe au bouton
                Node graphic = cellButton.getGraphic();
    
                // Verifier si le graphique est un Label ou un Text
                Label mainNumber = null;
                Text annotationText = null;
    
                if (graphic instanceof Label label) {
                    mainNumber = label;
                } else if (graphic instanceof Text annotation) {
                    annotationText = annotation;
                }
    
                // Si mainNumber est null, il faudra en creer un nouveau Label
                if (mainNumber == null) {
                    mainNumber = new Label();
                }
    
                // Si annotationText est null, il faudra en creer un nouveau Text
                if (annotationText == null) {
                    annotationText = new Text();
                }
    
                // Recuperer le texte du Label
                String numberStr = mainNumber.getText();
    
                // Verifier si le graphique est un nombre ou une annotation
                if (numberStr != null && !numberStr.isEmpty()) {
                    // Si c'est un nombre, mettre a jour l'affichage avec le nombre
                    updateCellDisplay(cellButton, mainNumber, annotationText, numberStr);
                } else {
                    // Si c'est une annotation, mettre a jour l'affichage avec les annotations
                    setAnnotationDisplay(cellButton, row, col, annotationText);
                }
            }
        }
    }
    
    /**
     * Remet les cellules a leur couleur d'origine apres une modification.
     * Parcourt chaque cellule de la grille et reinitialise leur couleur d'arriere-plan,
     * ainsi que leur affichage en fonction des valeurs actuelles (nombre ou annotation).
     * 
     * @param eval Liste des positions des cellules a reinitialiser, chaque position etant un tableau de deux entiers representant la ligne et la colonne.
     */
    public static void setCellsColorDefault(List<int[]> eval) {
        for (int[] position : eval) {
            int row = position[0];
            int col = position[1];
            Button cellButton = getButton(row, col);

            if (cellButton != null) {
                String defaultColor = (row / 3 + col / 3) % 2 == 0 ? "-fx-background-color: lightblue;" : "-fx-background-color: white;";
                cellButton.setStyle(defaultColor);
    
                // Recuperer le graphique associe au bouton
                Node graphic = cellButton.getGraphic();

                Label mainNumber = null;
                Text annotationText = null;

                // Verification du type de graphique attache au bouton
                if (graphic instanceof Label label) {
                    mainNumber = label;
                } else if (graphic instanceof Text annotation) {
                    annotationText = annotation;
                }

                // Si mainNumber est null, en creer un nouveau Label
                if (mainNumber == null) {
                    mainNumber = new Label();
                }

                // Si annotationText est null, en creer un nouveau Text
                if (annotationText == null) {
                    annotationText = new Text();
                }

                // Recuperer le texte du Label
                String numberStr = mainNumber.getText();

                updateCellDisplay(cellButton, mainNumber, annotationText, numberStr);
            }
        }
    }

    // ------------ Interaction avec les cellules ------------ //
    
    /**
     * Configure l'interaction pour une cellule donnee (c'est-a-dire la gestion des clics et actions associees).
     * 
     * @param cellButton Le bouton representant la cellule.
     * @param r La ligne de la cellule.
     * @param c La colonne de la cellule.
     * @param mainNumber L'etiquette du nombre.
     * @param annotationText Le texte des annotations.
     */
    private void setupCellInteraction(Button cellButton, final int r, final int c, Label mainNumber, Text annotationText) {
        cellButton.setOnAction(e -> {
            
            SudokuGame.resetTimer();

            if (toolsPanel.getAnnotationMode()) {

                // Remettre le style de la cellule selectionne d'avant par default
                if (selectedCell != null) {
                    selectedCell.setStyle((selectedRow / 3 + selectedCol / 3) % 2 == 0 ? "-fx-background-color: lightblue;" : "-fx-background-color: white;");
                }

                // Memoriser la cellule selectionnee pour l'annotation
                selectedCell = cellButton;
                selectedRow = r;
                selectedCol = c;

                // Mettre un contour a la cellule selectionnee
                cellButton.setStyle((selectedRow / 3 + selectedCol / 3) % 2 == 0 ? "-fx-background-color: lightblue; -fx-border-color: black; -fx-border-width: 1px;" : "-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1px;");

            } else {
                // Si pas en mode annotation, comportement normal
                String selectedStr = NumberSelection.getSelectedNumber();
                Cell currentCell = gridSudoku.getCell(r, c);

                if (!currentCell.isEditable()) {
                    
                    // Appeler la methode pour surligner la ligne et la colonne, si le parametre est active
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

                
                // Appeler la methode pour surligner la ligne et la colonne, si le parametre est active
                if(Settings.getHighlightRowCol())
                    SudokuDisplay.highlightRowAndColumn(grid, r, c);
            }
        });
    }


    // ------------ Methodes de mise a jour de la grille ------------ //

    /**
     * Charge la grille de Sudoku a partir d'une grille de base et met a jour l'affichage de chaque cellule.
     * Cette methode parcourt toutes les cellules de la grille et les met a jour en fonction des informations
     * de la grille de base (numeros et annotations).
     * 
     * @param gridSudokuBase La grille de Sudoku de base a charger.
     */
    public void loadGrid(Grid gridSudokuBase) {
        if (gridSudokuBase == null) {
            System.err.println("Error: gridSudokuBase is null!");
            return; // Eviter les NullPointerException
        }
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Cell cellGame = actualGame.getGrid().getCell(i, j);
                Cell cellBDD = gridSudokuBase.getCell(i, j);
                Button cellButton = cells[i][j]; // Recupere le bouton de la cellule actuelle
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
    /**
     * Recharge une grille de Sudoku deja demarree, en executant toutes les actions enregistrees precedemment.
     * Cette methode reinitialise les actions, les execute a nouveau, puis recharge la grille originale.
     * 
     * @param originalGrid La grille d'origine a recharger.
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

    /**
     * Reinitialise la grille de Sudoku en chargeant une nouvelle version a partir de la base de donnees.
     * Parcourt toutes les cellules et les met a jour avec les nouvelles donnees.
     */
    public void setGrid() {
        Grid newGrid;
        if (!isLearningMode)
            newGrid = DBManager.getGrid(actualGame.getGrid().getId());
        else
            newGrid = DBManager.getTech(technique.getId()).getGrid();

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
     * Efface la grille de Sudoku en reinitialisant l'affichage et les annotations de chaque cellule.
     * Parcourt chaque cellule de la grille 9x9, vide les annotations de chaque cellule,
     * et reinitialise l'affichage de la cellule a son etat par defaut.
     */
    private void clear() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Button cellButton = cells[row][col]; // Recupere le bouton de la cellule actuelle
                Node graphic = cellButton.getGraphic(); // Recupere le graphique associe au bouton
                Label mainNumber = (graphic instanceof Label) ? (Label) graphic : new Label(); // Verifie si le graphique est un Label, sinon cree un nouveau Label
                
                annotations[row][col].clear(); // Vide les annotations de la cellule actuelle
                resetCellDisplay(cellButton, mainNumber, null); // Reinitialise l'affichage de la cellule
            }
        }
    }    
    
    /**
     * Reinitialise la grille de Sudoku en effaçant son contenu actuel et en chargeant une nouvelle version.
     */
    private void resetGrid() {
        clear();
        setGrid();
    }

    /**
     * Reinitialise l'interface utilisateur de la grille de Sudoku, en effaçant les selections de numero et en desactivant les outils.
     */
    private void resetButton() {
        NumberSelection.resetSelectedNumber();
        NumberSelection.clearSelection();
        
        toolsPanel.setEraseButtonOff();
        toolsPanel.setPencilButtonOff();
    }

    /**
     * Reinitialise l'ensemble de l'interface utilisateur, y compris la grille, les outils et l'affichage.
     */
    public void resetInterface() {
        if(!isLearningMode) {
            resetGrid();
            resetButton();
            SudokuDisplay.resetGrid(getGridPane());
        } else
            setGrid();
    }

    // ------------ Getters ------------ //

    /**
     * Retourne le panneau de la grille de Sudoku (GridPane).
     * 
     * @return Le GridPane representant la grille de Sudoku.
     */
    public static GridPane getGridPane() {
        return grid;
    }

    /**
     * Retourne la grille de Sudoku actuelle.
     * 
     * @return La grille de Sudoku actuelle.
     */
    public static Grid getGrid() {
        return actualGame.getGrid();
    }

    /**
     * Retourne le jeu de Sudoku actuel.
     * 
     * @return Le jeu de Sudoku actuel.
     */
    public Game getGame() {
        return actualGame;
    }

    /**
     * Retourne le bouton associe a la cellule specifiee par la ligne et la colonne.
     * 
     * @param r La ligne de la cellule.
     * @param c La colonne de la cellule.
     * @return Le bouton associe a la cellule, ou null si les coordonnees sont invalides.
     */
    public static Button getButton(int r, int c) {
        if (r >= 0 && r < 9 && c >= 0 && c < 9) {
            return cells[r][c]; // Retourne le bouton correspondant
        }
        return null; // Retourne null si les coordonnees sont invalides
    }

    /**
     * Retourne la cellule selectionnee actuellement.
     * 
     * @return Le bouton de la cellule selectionnee.
     */
    public static Button getSelectedCell() {
        return selectedCell;
    }
    
    /**
     * Retourne la ligne de la cellule selectionnee.
     * 
     * @return La ligne de la cellule selectionnee.
     */
    public static int getSelectedRow() {
        return selectedRow;
    }
    
    /**
     * Retourne la colonne de la cellule selectionnee.
     * 
     * @return La colonne de la cellule selectionnee.
     */
    public static int getSelectedCol() {
        return selectedCol;
    }
    
    /**
     * Verifie si une annotation specifique est presente dans la cellule specifiee.
     * 
     * @param row La ligne de la cellule.
     * @param col La colonne de la cellule.
     * @param annotation L'annotation a verifier.
     * @return true si l'annotation est presente, false sinon.
     */
    public boolean hasAnnotation(int row, int col, String annotation) {
        return annotations[row][col].contains(annotation);
    }

    /**
     * Verifie si une cellule contient des annotations.
     * 
     * @param row La ligne de la cellule.
     * @param col La colonne de la cellule.
     * @return true si la cellule contient des annotations, false sinon.
     */
    public boolean hasAnnotations(int row, int col) {
        return !annotations[row][col].isEmpty();
    }

    /**
     * Verifie si la derniere action effectuee sur la cellule specifiee est une action d'effacement.
     * 
     * @param r La ligne de la cellule.
     * @param c La colonne de la cellule.
     * @return true si la derniere action etait un effacement, false sinon.
     */
    public boolean isLastActionEraser(int r, int c){
        return this.actionEraser[r][c];
    }


    // ------------ Setters ------------ //

    /**
     * Modifie l'etat de l'action d'effacement sur une cellule specifiee.
     * 
     * @param r La ligne de la cellule.
     * @param c La colonne de la cellule.
     * @param state L'etat de l'action d'effacement a definir (true ou false).
     */
    public void modifyStateEraser(int r, int c, boolean state){
        this.actionEraser[r][c] = state;
    }

    /**
     * Definit le jeu de Sudoku actuel.
     * 
     * @param game Le jeu de Sudoku a definir.
     */
    public static void setGame(Game game) {
        actualGame = game;
    }

    /**
     * Modifie le bouton selectionne
     * 
     * @param button Le nouveau bouton selectionne
     */
    public static void setSelectedCell(Button button) {
        selectedCell = button;
    }

    /**
     * Modifie la ligne selectionnee
     * 
     * @param row La nouvelle ligne selectionnee
     */
    public static void setSelectedRow(int row) {
        selectedRow = row;
    }

    /**
     * Modifie la colonne selectionnee
     * 
     * @param col La nouvelle colonne selectionnee
     */
    public static void setSelectedCol(int col) {
        selectedCol = col;
    }

    /**
     * Changement d'etat du booleen pour le mode apprentissage ou non
     * 
     * @param bool Nouvelle etat du booleen
     */
    public void setLearningMode(boolean bool) {
        isLearningMode = bool;
    }

    /**
     * Changement de la technique de jeu utilisee
     * 
     * @param tech Nouvelle technique de jeu
     */
    public void setTechnique(Technique tech) {
        technique = tech;
    }
}