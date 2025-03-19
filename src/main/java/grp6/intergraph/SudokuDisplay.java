package grp6.intergraph;

import java.sql.SQLException;

import grp6.sudocore.Game;
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
            if (node instanceof Button) {
                Button button = (Button) node;
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

    // Méthode pour afficher une oeillère : une cellule reste normale, les autres deviennent sombres
    public static void highlightCell(GridPane grid, int row, int col) {
        // Réinitialiser la grille avant de surligner
        resetGrid(grid);

        for (Node node : grid.getChildren()) {
            // Récupérer la cellule à la position spécifique
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                if (node instanceof Button) {
                    Button button = (Button) node;
                    // Laisser la cellule sélectionnée visible normalement
                    button.setStyle("-fx-background-color: white;");
                }
            } else {
                // Assombrir toutes les autres cellules
                if (node instanceof Button) {
                    Button button = (Button) node;
                    button.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
                }
            }
        }
    }

    // Méthode pour afficher une oeillère sur plusieurs cellules, toutes les autres deviennent sombres
    public static void highlightCells(GridPane grid, int[][] coordinates) {
        // Réinitialiser la grille avant de surligner
        resetGrid(grid);

        for (Node node : grid.getChildren()) {
            boolean isHighlighted = false;
            // Vérifier si la cellule est dans la liste des coordonnées
            for (int[] coord : coordinates) {
                if (GridPane.getRowIndex(node) == coord[0] && GridPane.getColumnIndex(node) == coord[1]) {
                    isHighlighted = true;
                    break;
                }
            }

            if (isHighlighted) {
                // Laisser la cellule spécifique normale
                if (node instanceof Button) {
                    Button button = (Button) node;
                    button.setStyle("-fx-background-color: white;");
                }
            } else {
                // Assombrir toutes les autres cellules
                if (node instanceof Button) {
                    Button button = (Button) node;
                    button.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
                }
            }
        }
    }

    // Méthode pour teindre la ligne et la colonne du dernier bouton cliqué en assombrissant leur couleur actuelle
    public static void highlightRowAndColumn(GridPane grid, int clickedRow, int clickedCol) {
        // Réinitialiser la grille avant de surligner
        resetGrid(grid);

        // Parcourir toutes les cellules pour la ligne
        for (Node node : grid.getChildren()) {
            if (GridPane.getRowIndex(node) == clickedRow) {
                if (node instanceof Button) {
                    Button button = (Button) node;

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
            }
        }

        // Parcourir toutes les cellules pour la colonne
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == clickedCol) {
                if (node instanceof Button) {
                    Button button = (Button) node;

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
            }
        }
    }

    // Méthode pour convertir la couleur en chaîne de caractères au format RGB
    private static String toRgbString(Color color) {
        return "rgb(" + (int)(color.getRed() * 255) + ", " + (int)(color.getGreen() * 255) + ", " + (int)(color.getBlue() * 255) + ")";
    }

    public static void showEndGameEffect(GridPane grid, Stage primaryStage, Game actualGameParam) {

        resetGrid(grid);
        
        final Game actualGame = actualGameParam;
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
                        if (node instanceof Button) {  // Si la cellule est un bouton
                            Button button = (Button) node;
    
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

                                        if (actualGame != null) {
                                            try {
                                                actualGame.stopGame();
                                            } catch (SQLException | InterruptedException e1) {
                                                System.err.println("Error stopping the game: " + e1.getMessage());
                                            }
                                            SudokuGrid.setGame(null);
                                        }
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