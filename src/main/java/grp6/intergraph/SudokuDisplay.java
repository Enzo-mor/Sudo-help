package grp6.intergraph;

import grp6.sudocore.*;

import java.util.HashSet;
import java.util.Set;

import grp6.sudocore.Grid;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SudokuDisplay {

    // Méthode pour réinitialiser toutes les cellules de la grille à une couleur par défaut
    public static void resetGrid(GridPane grid) {
        for (Node node : grid.getChildren()) {
            if (node instanceof Button button) {
                int row = GridPane.getRowIndex(node);
                int col = GridPane.getColumnIndex(node);

                // Logique de couleur alternée (comme un échiquier)
                String defaultColor = (row / 3 + col / 3) % 2 == 0 
                    ? "-fx-background-color: lightblue;" 
                    : "-fx-background-color: white;";

                // Appliquer la couleur calculée
                button.setStyle(defaultColor);
            }
        }
    }

    // Méthode pour afficher une oeillère sur plusieurs cellules, toutes les autres deviennent sombres
    public static void highlightCells(GridPane grid, int[][] coordinates) {
        // Réinitialiser la grille avant de surligner
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

    public static void highlightSameNumbers(GridPane gridPane, Grid grid, int number) {
        // Réinitialiser la grille avant de surligner
        resetGrid(gridPane);

        // Parcourir les cellules du grid
        for (int r = 0; r < gridPane.getRowCount(); r++) {  // Supposons qu'il existe une méthode getRowCount() pour obtenir le nombre de lignes
            for (int c = 0; c < gridPane.getColumnCount(); c++) {  // Supposons qu'il existe une méthode getColumnCount() pour obtenir le nombre de colonnes
                // Récupérer la cellule à la position (r, c) dans le Grid
                Cell cell = grid.getCell(r, c);  // Supposons qu'il existe une méthode getCell(r, c) pour obtenir la cellule
    
                // Vérifier si la cellule contient le nombre recherché
                if (cell != null && cell.getNumber() == number) {
                    // Trouver le bouton dans le GridPane à la même position (r, c)
                    Node buttonNode = getButtonFromGridPane(gridPane, r, c);  // Méthode pour récupérer le bouton à la position (r, c)
    
                    if (buttonNode instanceof Button button) {
                        button.setStyle("-fx-background-color: #aee9fc");
                    }
                }
            }
        }
    }
    
    // Méthode pour récupérer le bouton d'un GridPane à la position (r, c)
    private static Node getButtonFromGridPane(GridPane gridPane, int r, int c) {      
        // Parcourir les enfants du GridPane et vérifier la position
        for (Node node : gridPane.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            Integer col = GridPane.getColumnIndex(node);
    
            if (row != null && col != null && row == r && col == c) {
                return node;
            }
        }
        return null;  // Retourne null si aucun bouton n'est trouvé à cette position
    }
    

    /**
     * Retourne le style à appliquer pour une cellule surlignée.
     */
    private static String getHighlightStyle(int row, int col) {
        return (row / 3 + col / 3) % 2 == 0 
            ? "-fx-background-color: lightblue;" 
            : "-fx-background-color: white;";
    }

    // Méthode pour teindre la ligne et la colonne du dernier bouton cliqué en assombrissant leur couleur actuelle
    public static void highlightRowAndColumn(GridPane grid, int clickedRow, int clickedCol) {
        // Réinitialiser la grille avant de surligner
        resetGrid(grid);

        // Parcourir toutes les cellules pour la ligne et la colonne
        for (Node node : grid.getChildren()) {
            if (node instanceof Button button) {
                Integer row = GridPane.getRowIndex(node) != null ? GridPane.getRowIndex(node) : 0;
                Integer col = GridPane.getColumnIndex(node) != null ? GridPane.getColumnIndex(node) : 0;
                if (row == clickedRow || col == clickedCol) {
                    lighterCell(button);
                }

                // Ajouter une bordure rouge au bouton sélectionné
                if (row == clickedRow && col == clickedCol) {
                    button.setStyle(button.getStyle() + "; -fx-border-color: lightgray; -fx-border-width: 2px;");
                }
            }
        }
    }

    // Méthode pour convertir la couleur en chaîne de caractères au format RGB
    private static String toRgbString(Color color) {
        return "rgb(" + (int)(color.getRed() * 255) + ", " + (int)(color.getGreen() * 255) + ", " + (int)(color.getBlue() * 255) + ")";
    }

    private static void darkerCell(Button button) {
        // Vérifier si le bouton a un fond défini
        String buttonStyle = button.getStyle();
        if (buttonStyle.contains("-fx-background-color")) {
            // Si un fond est défini, on peut le récupérer et assombrir
            String colorString = buttonStyle.split(":")[1].trim().replace(";", "");
            Color currentColor = Color.web(colorString); // Convertir la couleur de fond en objet Color
            // Assombrir la couleur
            Color darkerColor = currentColor.darker();
            // Appliquer la couleur assombrie
            button.setStyle("-fx-background-color: " + toRgbString(darkerColor) + ";");
        } else {
            // Si aucun fond n'est défini, utiliser une couleur par défaut
            Color defaultColor = Color.WHITE;
            Color darkerColor = defaultColor.darker();
            button.setStyle("-fx-background-color: " + toRgbString(darkerColor) + ";");
        }
    }

    private static void lighterCell(Button button) {
        String buttonStyle = button.getStyle();
        if (buttonStyle.contains("-fx-background-color")) {
            String colorString = buttonStyle.split(":")[1].trim().replace(";", "");
            Color currentColor = Color.web(colorString);
    
            // Rendre "lightblue" plus clair et "white" plus démarqué
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
            // Si pas de fond défini, mettre une couleur plus marquée par défaut
            button.setStyle("-fx-background-color: lightgray;");
        }
    }
    

    // Méthode pour afficher un effet de fin de jeu
    public static void showEndGameEffect(GridPane grid, Stage primaryStage) {

        resetGrid(grid);
        // Calculer les dimensions du GridPane
        int rows = grid.getRowCount();
        int cols = grid.getColumnCount();
    
        // Définir les paramètres de l'animation
        double maxTranslateX = 5;  // Déplacement maximal sur l'axe X
        double maxTranslateY = 5;  // Déplacement maximal sur l'axe Y
        double maxScale = 1.05;      // Augmentation de la taille des éléments (effet de soulèvement)
        double maxDelay = 1.5;      // Délai maximal entre l'animation des boutons (plus un bouton est éloigné, plus le délai est long)
    
        // Calculer le centre de la grille
        double centerX = (cols - 1) / 2.0;
        double centerY = (rows - 1) / 2.0;
    
        // Variable pour savoir si l'animation est terminée
        final int totalCells = rows * cols;
        final int[] completedCells = {0};
    
        // Parcourir toutes les cellules de la grille
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Récupérer chaque cellule de la grille
                for (javafx.scene.Node node : grid.getChildren()) {
                    if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                        if (node instanceof Button button) {
                            // Calculer la distance du centre de la grille
                            double distanceX = Math.abs(col - centerX);
                            double distanceY = Math.abs(row - centerY);
                            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY); // distance euclidienne
    
                            // Calculer le délai basé sur la distance
                            double delay = (distance / Math.sqrt((cols - 1) * (cols - 1) + (rows - 1) * (rows - 1))) * maxDelay;
    
                            // Créer l'animation de secousse
                            Timeline timeline = new Timeline(
                                // Initialisation de l'animation
                                new KeyFrame(Duration.seconds(delay),
                                    new KeyValue(button.translateXProperty(), 0),
                                    new KeyValue(button.translateYProperty(), 0),
                                    new KeyValue(button.scaleXProperty(), 1),
                                    new KeyValue(button.scaleYProperty(), 1)
                                ),
                                // Animation de l'onde (déplacement vers le haut et bas)
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
    
                            // À la fin de l'animation de chaque cellule
                            timeline.setOnFinished(event -> {
                                // Mise à jour du compteur de cellules animées
                                completedCells[0]++;
    
                                // Si toutes les cellules sont animées, on affiche un message de fin et on change de scène
                                if (completedCells[0] == totalCells) {
                                    // Pause de 1 seconde avant d'afficher le message de fin
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
    
                                    // Afficher un message de fin
                                    Platform.runLater(() -> {
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setTitle("Jeu terminé");
                                        alert.setHeaderText("Félicitations !");
                                        alert.setContentText("Vous avez terminé le Sudoku !");
                                        alert.showAndWait();

                                        // Changer de scène après l'animation
                                        SudokuMenu.showSudokuLibrary(primaryStage); // Ou MainMenu.showMainMenu si nécessaire
                                    });
                                }
                            });
    
                            // Démarrer l'animation du bouton
                            timeline.play();
                        }
                    }
                }
            }
        }
    }
}