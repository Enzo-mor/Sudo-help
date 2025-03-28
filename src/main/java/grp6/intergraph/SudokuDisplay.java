package grp6.intergraph;
import grp6.sudocore.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Cette classe represente l'affichage et la gestion des interactions visuelles pour un jeu de Sudoku.
 * Elle permet de reinitialiser la grille, de surligner des cellules, d'afficher des effets visuels a la fin d'une partie,
 * et de gerer des interactions telles que la mise en surbrillance des lignes, colonnes, ou chiffres similaires.
 *
 * @author PERRON Nathan
 * @author RASSON Emma
 */
public class SudokuDisplay {

    /**
     * Reinitialise toutes les cellules de la grille a une couleur par defaut, 
     * en alternant les couleurs comme un echiquier.
     *
     * @param grid La grille a reinitialiser [ GridPane ]
     */
    public static void resetGrid(GridPane grid) {
        for (Node node : grid.getChildren()) {
            if (node instanceof Button button) {
                int row = GridPane.getRowIndex(node);
                int col = GridPane.getColumnIndex(node);

                // Logique de couleur alternee (comme un echiquier)
                String defaultColor = (row / 3 + col / 3) % 2 == 0 
                    ? "-fx-background-color: lightblue;" 
                    : "-fx-background-color: white;";

                // Appliquer la couleur calculee
                button.setStyle(defaultColor);
            }
        }
    }

    /**
     * Surligne certaines cellules dans la grille, assombrissant toutes les autres.
     *
     * @param grid        La grille où les cellules doivent etre modifiees [ GridPane ]
     * @param coordinates Coordonnees des cellules a surligner [ List<int[]> ]
     */
    public static void highlightCells(GridPane grid, List<int[]> coordinates) {
        // Reinitialiser la grille avant de surligner
        resetGrid(grid);

        // Utiliser un Set pour une recherche plus efficace
        Set<String> highlightedCells = new HashSet<>();
        for (int[] coord : coordinates) {
            highlightedCells.add(coord[0] + "," + coord[1]);
        }

        for (Node node : grid.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            Integer col = GridPane.getColumnIndex(node);

            if (row == null || col == null) {
                continue;
            }

            if (node instanceof Button button) {
                if (highlightedCells.contains(row + "," + col)) {
                    button.setStyle(getHighlightStyle(row, col));
                } else {
                    darkerCell(button);
                    darkerCell(button);
                }
            }
        }
    }

    /**
     * Surligne toutes les cellules contenant un nombre specifique.
     *
     * @param gridPane  Le panneau de grille où les boutons sont presents [ GridPane ]
     * @param grid      La grille contenant les donnees du jeu [ Grid ]
     * @param number    Le nombre a surligner [ int ]
     */
    public static void highlightSameNumbers(GridPane gridPane, Grid grid, int number) {
        // Reinitialiser la grille avant de surligner
        resetGrid(gridPane);

        // Parcourir les cellules du grid
        for (int r = 0; r < gridPane.getRowCount(); r++) {  // Supposons qu'il existe une methode getRowCount() pour obtenir le nombre de lignes
            for (int c = 0; c < gridPane.getColumnCount(); c++) {  // Supposons qu'il existe une methode getColumnCount() pour obtenir le nombre de colonnes
                // Recuperer la cellule a la position (r, c) dans le Grid
                Cell cell = grid.getCell(r, c);  // Supposons qu'il existe une methode getCell(r, c) pour obtenir la cellule
    
                // Verifier si la cellule contient le nombre recherche
                if (cell != null && cell.getNumber() == number) {
                    // Trouver le bouton dans le GridPane a la meme position (r, c)
                    Node buttonNode = getButtonFromGridPane(gridPane, r, c);  // Methode pour recuperer le bouton a la position (r, c)
    
                    if (buttonNode instanceof Button button) {
                        button.setStyle("-fx-background-color: #aee9fc");
                    }
                }
            }
        }
    }
    
    /**
     * Recupere un bouton dans un GridPane a la position specifiee (r, c).
     *
     * @param gridPane Le panneau de grille [ GridPane ]
     * @param r        L'indice de la ligne du bouton [ int ]
     * @param c        L'indice de la colonne du bouton [ int ]
     * @return Le bouton trouve ou null si aucun bouton n'est trouve [ Node ]
     */
    private static Node getButtonFromGridPane(GridPane gridPane, int r, int c) {      
        // Parcourir les enfants du GridPane et verifier la position
        for (Node node : gridPane.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            Integer col = GridPane.getColumnIndex(node);
    
            if (row != null && col != null && row == r && col == c) {
                return node;
            }
        }
        return null;  // Retourne null si aucun bouton n'est trouve a cette position
    }
    

    /**
     * Retourne le style CSS a appliquer pour une cellule surlignee.
     *
     * @param row L'indice de la ligne de la cellule [ int ]
     * @param col L'indice de la colonne de la cellule [ int ]
     * @return Le style CSS de la cellule [ String ]
     */
    private static String getHighlightStyle(int row, int col) {
        return (row / 3 + col / 3) % 2 == 0 
            ? "-fx-background-color: lightblue;" 
            : "-fx-background-color: white;";
    }

    /**
     * Surligne la ligne et la colonne du dernier bouton clique.
     * Assombrit les autres cellules.
     *
     * @param grid       La grille a modifier [ GridPane ]
     * @param clickedRow L'indice de la ligne du bouton clique [ int ]
     * @param clickedCol L'indice de la colonne du bouton clique [ int ]
     */
    public static void highlightRowAndColumn(GridPane grid, int clickedRow, int clickedCol) {
        // Reinitialiser la grille avant de surligner
        resetGrid(grid);

        // Parcourir toutes les cellules pour la ligne et la colonne
        for (Node node : grid.getChildren()) {
            if (node instanceof Button button) {
                Integer row = GridPane.getRowIndex(node) != null ? GridPane.getRowIndex(node) : 0;
                Integer col = GridPane.getColumnIndex(node) != null ? GridPane.getColumnIndex(node) : 0;
                if (row == clickedRow || col == clickedCol) {
                    lighterCell(button);
                }

                // Ajouter une bordure rouge au bouton selectionne
                if (row == clickedRow && col == clickedCol) {
                    button.setStyle(button.getStyle() + "; -fx-border-color: lightgray; -fx-border-width: 2px;");
                }
            }
        }
    }

    /**
     * Convertit une couleur en chaîne de caracteres au format RGB.
     *
     * @param color La couleur a convertir [ Color ]
     * @return La chaîne de caracteres representant la couleur au format RGB [ String ]
     */
    private static String toRgbString(Color color) {
        return "rgb(" + (int)(color.getRed() * 255) + ", " + (int)(color.getGreen() * 255) + ", " + (int)(color.getBlue() * 255) + ")";
    }

    /**
     * Applique un effet de teinte plus sombre a une cellule (bouton).
     *
     * @param button Le bouton a assombrir [ Button ]
     */
    private static void darkerCell(Button button) {
        // Verifier si le bouton a un fond defini
        String buttonStyle = button.getStyle();
        if (buttonStyle.contains("-fx-background-color")) {
            // Si un fond est defini, on peut le recuperer et assombrir
            String colorString = buttonStyle.split(":")[1].trim().replace(";", "");
            Color currentColor = Color.web(colorString); // Convertir la couleur de fond en objet Color
            // Assombrir la couleur
            Color darkerColor = currentColor.darker();
            // Appliquer la couleur assombrie
            button.setStyle("-fx-background-color: " + toRgbString(darkerColor) + ";");
        } else {
            // Si aucun fond n'est defini, utiliser une couleur par defaut
            Color defaultColor = Color.WHITE;
            Color darkerColor = defaultColor.darker();
            button.setStyle("-fx-background-color: " + toRgbString(darkerColor) + ";");
        }
    }

    /**
     * Applique un effet de teinte plus claire a une cellule (bouton).
     *
     * @param button Le bouton a eclaircir [ Button ]
     */
    private static void lighterCell(Button button) {
        String buttonStyle = button.getStyle();
        if (buttonStyle.contains("-fx-background-color")) {
            String colorString = buttonStyle.split(":")[1].trim().replace(";", "");
            Color currentColor = Color.web(colorString);
    
            // Rendre "lightblue" plus clair et "white" plus demarque
            Color modifiedColor;
            if (currentColor.equals(Color.LIGHTBLUE)) {
                modifiedColor = Color.web("#aee9fc");
            } else if (currentColor.equals(Color.WHITE)) {
                modifiedColor = Color.web("#e4f9ff");
            } else {
                modifiedColor = Color.web("#aee9fc");
            }
    
            button.setStyle("-fx-background-color: " + toRgbString(modifiedColor) + ";");
        } else {
            // Si pas de fond defini, mettre une couleur plus marquee par defaut
            button.setStyle("-fx-background-color: lightgray;");
        }
    }
    

    /**
     * Affiche un effet visuel de fin de jeu avec une animation de secousse.
     * Apres l'animation, un message de fin de jeu est affiche et la scene est changee.
     *
     * @param grid         La grille a animer [ GridPane ]
     * @param primaryStage La scene principale a changer apres la fin du jeu [ Stage ]
     */
    public static void showEndGameEffect(GridPane grid, Stage primaryStage) {

        resetGrid(grid);
        // Calculer les dimensions du GridPane
        int rows = grid.getRowCount();
        int cols = grid.getColumnCount();
    
        // Definir les parametres de l'animation
        double maxTranslateX = 5;  // Deplacement maximal sur l'axe X
        double maxTranslateY = 5;  // Deplacement maximal sur l'axe Y
        double maxScale = 1.05;      // Augmentation de la taille des elements (effet de soulevement)
        double maxDelay = 1.5;      // Delai maximal entre l'animation des boutons (plus un bouton est eloigne, plus le delai est long)
    
        // Calculer le centre de la grille
        double centerX = (cols - 1) / 2.0;
        double centerY = (rows - 1) / 2.0;
    
        // Variable pour savoir si l'animation est terminee
        final int totalCells = rows * cols;
        final int[] completedCells = {0};
    
        // Parcourir toutes les cellules de la grille
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Recuperer chaque cellule de la grille
                for (javafx.scene.Node node : grid.getChildren()) {
                    if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                        if (node instanceof Button button) {
                            // Calculer la distance du centre de la grille
                            double distanceX = Math.abs(col - centerX);
                            double distanceY = Math.abs(row - centerY);
                            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY); // distance euclidienne
    
                            // Calculer le delai base sur la distance
                            double delay = (distance / Math.sqrt((cols - 1) * (cols - 1) + (rows - 1) * (rows - 1))) * maxDelay;
    
                            // Creer l'animation de secousse
                            Timeline timeline = new Timeline(
                                // Initialisation de l'animation
                                new KeyFrame(Duration.seconds(delay),
                                    new KeyValue(button.translateXProperty(), 0),
                                    new KeyValue(button.translateYProperty(), 0),
                                    new KeyValue(button.scaleXProperty(), 1),
                                    new KeyValue(button.scaleYProperty(), 1)
                                ),
                                // Animation de l'onde (deplacement vers le haut et bas)
                                new KeyFrame(Duration.seconds(delay + 0.2),
                                    new KeyValue(button.translateXProperty(), maxTranslateX),
                                    new KeyValue(button.translateYProperty(), -maxTranslateY),
                                    new KeyValue(button.scaleXProperty(), maxScale),
                                    new KeyValue(button.scaleYProperty(), maxScale)
                                ),
                                new KeyFrame(Duration.seconds(delay + 0.4),
                                    new KeyValue(button.translateXProperty(), -maxTranslateX),
                                    new KeyValue(button.translateYProperty(), maxTranslateY),
                                    new KeyValue(button.scaleXProperty(), maxScale),
                                    new KeyValue(button.scaleYProperty(), maxScale)
                                ),
                                new KeyFrame(Duration.seconds(delay + 0.6),
                                    new KeyValue(button.translateXProperty(), 0),
                                    new KeyValue(button.translateYProperty(), 0),
                                    new KeyValue(button.scaleXProperty(), 1),
                                    new KeyValue(button.scaleYProperty(), 1)
                                )
                            );
    
                            // A la fin de l'animation de chaque cellule
                            timeline.setOnFinished(event -> {
                                // Mise a jour du compteur de cellules animees
                                completedCells[0]++;
    
                                // Si toutes les cellules sont animees, on affiche un message de fin et on change de scene
                                if (completedCells[0] == totalCells) {
                                    // Pause de 1 seconde avant d'afficher le message de fin
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        System.err.println("InterruptedException: " + e.getMessage());
                                    }
    
                                    // Afficher un message de fin
                                    Platform.runLater(() -> {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Jeu terminé");
                                        alert.setHeaderText("Félicitations !");
                                        alert.setContentText("Vous avez terminé le Sudoku !");
                                        alert.showAndWait();

                                        // Changer de scene apres l'animation
                                        SudokuMenu.showSudokuLibrary(primaryStage); // Ou MainMenu.showMainMenu si necessaire
                                    });
                                }
                            });
    
                            // Demarrer l'animation du bouton
                            timeline.play();
                        }
                    }
                }
            }
        }
    }
}