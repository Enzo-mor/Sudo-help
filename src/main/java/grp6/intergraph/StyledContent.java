package grp6.intergraph;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class StyledContent {

    // Style de base du bouton
    private static final String BUTTON_STYLE = 
        "-fx-background-color: #80A9C2; " + // Fond bleu pastel
        "-fx-text-fill: white; " +           // Texte blanc
        "-fx-font-size: 12px; " +           // Taille de la police réduite
        "-fx-font-weight: bold; " +         // Texte en gras
        "-fx-padding: 6px 12px; " +         // Espacement réduit autour du texte
        "-fx-background-radius: 5px; " +   // Coins légèrement arrondis
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 2, 0, 2, 2); " + // Ombre subtile
        "-fx-cursor: hand;";                // Curseur de main

    // Style au survol du bouton
    private static final String BUTTON_HOVER_STYLE =
        "-fx-background-color: #6D8D9C; " + // Fond bleu clair au survol
        "-fx-text-fill: white; " +           // Texte blanc
        "-fx-font-size: 12px; " +           // Taille de la police réduite
        "-fx-font-weight: bold; " +         // Texte en gras
        "-fx-background-radius: 5px; " +    // Coins légèrement arrondis
        "-fx-padding: 8px 16px; " +         // Augmentation du padding au survol
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 3, 0, 2, 2);";  // Ombre plus marquée

    // Styles de base pour le conteneur Sudoku
    private static final String SUDOKU_BOX_STYLE =
        "-fx-background-color: #4A90E2; " +
        "-fx-border-color: #2C3E50; " +
        "-fx-text-fill: white; " +
        "-fx-font-size: 18px; " +
        "-fx-font-weight: bold; " +
        "-fx-border-width: 2; " +
        "-fx-border-radius: 10; " +
        "-fx-background-radius: 10; " +
        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 5);";

    // Styles pour le conteneur Sudoku au survol
    private static final String SUDOKU_BOX_HOVER_STYLE =
        "-fx-background-color: #357ABD; " +
        "-fx-border-color: #2C3E50; " +
        "-fx-text-fill: white; " +
        "-fx-font-size: 18px; " +
        "-fx-font-weight: bold; " +
        "-fx-border-width: 2; " +
        "-fx-border-radius: 10; " +
        "-fx-background-radius: 10; " +
        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 12, 0, 0, 6);";

    // Méthode pour appliquer le style de base et au survol à un bouton
    public static void applyButtonStyle(Button button) {
        button.setStyle(BUTTON_STYLE);
        
        // Gestion du survol (effet au passage de la souris)
        button.setOnMouseEntered((MouseEvent e) -> button.setStyle(BUTTON_HOVER_STYLE));
        button.setOnMouseExited((MouseEvent e) -> button.setStyle(BUTTON_STYLE));
    }

    // Méthode pour appliquer un style de base aux boutons
    public static void applyButtonBoxStyle(Button button) {
        button.setStyle(SUDOKU_BOX_STYLE);
        
        // Gestion du survol (effet au passage de la souris)
        button.setOnMouseEntered((MouseEvent e) -> button.setStyle(SUDOKU_BOX_HOVER_STYLE));
        button.setOnMouseExited((MouseEvent e) -> button.setStyle(SUDOKU_BOX_STYLE));
    }

    // Applique un style de base
    public static void styleButton(Button button) {
        // Style par défaut : Blanc avec bordure grise
        button.setStyle(
            "-fx-background-color: white;" +      // Couleur de fond blanche par défaut
            "-fx-border-color: #a8a8a8;" +         // Couleur de la bordure
            "-fx-border-radius: 8;" +              // Arrondi des bords
            "-fx-background-radius: 8;" +          // Arrondi du fond
            "-fx-padding: 8;"                      // Padding pour l'espace à l'intérieur
        );

        // Effet de survol (hover) : Applique une couleur au survol, mais ne change pas si sélectionné
        button.setOnMouseEntered(e -> {
            if (!button.getStyle().contains("lightgreen")) { // N'applique pas le survol si déjà sélectionné
                button.setStyle(
                    "-fx-background-color: #cce7ff;" +      // Couleur de fond au survol
                    "-fx-border-color: #7bb9ff;" +          // Couleur de la bordure
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 8;");
            }
        });

        // Réinitialisation de la couleur au survol
        button.setOnMouseExited(e -> {
            if (!button.getStyle().contains("lightgreen")) { // N'applique pas la couleur si déjà sélectionné
                button.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #a8a8a8;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 8;");
            }
        });

        // Effet lors du clic (pressé) : Change la couleur de fond pour montrer que le bouton est pressé
        button.setOnMousePressed(e -> {
            if (!button.getStyle().contains("lightgreen")) { // Si non sélectionné
                button.setStyle(
                    "-fx-background-color: #7bb9ff;" +   // Changer fond pendant le clic
                    "-fx-border-color: #3a8cc1;" +       // Changer bordure
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 8;");
            }
        });

        // Rétablir la couleur après le relâchement du bouton
        button.setOnMouseReleased(e -> {
            if (!button.getStyle().contains("lightgreen")) { // Si non sélectionné
                button.setStyle(
                    "-fx-background-color: #cce7ff;" +
                    "-fx-border-color: #7bb9ff;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 8;");
            }
        });
    }

    // Met un bouton en mode "actif" (couleur verte)
    public static void setActiveButton(Button button) {
        button.setStyle(
            "-fx-background-color: lightgreen;" +  // Couleur verte pour le bouton sélectionné
            "-fx-border-color: #3a8cc1;" +        // Couleur de la bordure
            "-fx-border-radius: 8;" +             // Arrondi des bords
            "-fx-background-radius: 8;" +         // Arrondi du fond
            "-fx-padding: 8;"                     // Padding
        );
    }

    // Remet un bouton à son état "inactif" (couleur blanche)
    public static void setInactiveButton(Button button) {
        button.setStyle(
            "-fx-background-color: white;" +      // Couleur de fond blanche pour inactif
            "-fx-border-color: #a8a8a8;" +         // Couleur de la bordure
            "-fx-border-radius: 8;" +              // Arrondi des bords
            "-fx-background-radius: 8;" +          // Arrondi du fond
            "-fx-padding: 8;"                      // Padding
        );
    }

    // Appliquer le style et l'effet de survol
    public static void applySudokuBoxStyle(VBox sudokuBox) {
        sudokuBox.setStyle(SUDOKU_BOX_STYLE);
        sudokuBox.setOnMouseEntered(e -> sudokuBox.setStyle(SUDOKU_BOX_HOVER_STYLE));
        sudokuBox.setOnMouseExited(e -> sudokuBox.setStyle(SUDOKU_BOX_STYLE));
    }

    public static void applyArrowButtonStyle(Button button) {
        button.setStyle(
            "-fx-background-color: #444; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 18px; " +
            "-fx-padding: 10px 15px; " +
            "-fx-background-radius: 5px;"
        );
    
        // Effet au survol
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: #666; " + 
            "-fx-text-fill: white; " +
            "-fx-font-size: 18px; " +
            "-fx-padding: 10px 15px; " +
            "-fx-background-radius: 5px;"
        ));
    
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: #444; " + 
            "-fx-text-fill: white; " +
            "-fx-font-size: 18px; " +
            "-fx-padding: 10px 15px; " +
            "-fx-background-radius: 5px;"
        ));
    }    
}