package grp6.intergraph;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

/**
 * Classe StyledContent
 * Cette classe fournit des methodes utilitaires pour appliquer des styles CSS personnalises
 * aux composants JavaFX tels que les boutons et les conteneurs.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 */
public class StyledContent {

    /** 
     * Style de base du bouton 
     */
    private static final String BUTTON_STYLE = 
        "-fx-background-color: #80A9C2; " + // Fond bleu pastel
        "-fx-text-fill: white; " +           // Texte blanc
        "-fx-font-size: 12px; " +           // Taille de la police reduite
        "-fx-font-weight: bold; " +         // Texte en gras
        "-fx-padding: 6px 12px; " +         // Espacement reduit autour du texte
        "-fx-background-radius: 5px; " +   // Coins legerement arrondis
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 2, 0, 2, 2); " + // Ombre subtile
        "-fx-cursor: hand;";                // Curseur de main

    /** 
     * Style du bouton dangeureux 
     */
    private static final String WARNING_BUTTON_STYLE = 
        "-fx-background-color: #6D8D9C; " + // Fond bleu pastel
        "-fx-text-fill: red; " +           // Texte rouge
        "-fx-font-size: 12px; " +
        "-fx-font-weight: bold; " +
        "-fx-padding: 6px 12px; " +
        "-fx-background-radius: 5px; " +   // Coins legerement arrondis
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 2, 0, 2, 2); " + // Ombre subtile
        "-fx-cursor: hand;";

    /** 
     * Style du bouton au survol 
     */
    private static final String BUTTON_HOVER_STYLE =
        "-fx-background-color: #6D8D9C; " + // Fond bleu clair au survol
        "-fx-text-fill: white; " +           // Texte blanc
        "-fx-font-size: 12px; " +
        "-fx-font-weight: bold; " +
        "-fx-background-radius: 5px; " +
        "-fx-padding: 8px 16px; " +         // Augmentation du padding au survol
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 3, 0, 2, 2);";  // Ombre plus marquee

    /** 
     * Style du bouton dangereux au survol 
     */
    private static final String WARNING_BUTTON_HOVER_STYLE =
        "-fx-background-color: #6D8D9C; " + // Fond bleu clair au survol
        "-fx-text-fill: red; " +            // Texte rouge
        "-fx-font-size: 12px; " +
        "-fx-font-weight: bold; " +
        "-fx-background-radius: 5px; " +
        "-fx-padding: 8px 16px; " +         // Augmentation du padding au survol
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 3, 0, 2, 2);";  // Ombre plus marquee

    /** 
     * Style du conteneur Sudoku 
     */
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

    /** 
     * Style du conteneur Sudoku au survol 
     */
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

    /** 
     * Style de base du bouton 
     */
    private static final String BUTTONBLINKING_STYLE = 
        "-fx-background-color:rgb(181, 139, 14); " + // Fond jaune
        "-fx-text-fill: white; " +           // Texte blanc
        "-fx-font-size: 12px; " +           // Taille de la police reduite
        "-fx-font-weight: bold; " +         // Texte en gras
        "-fx-padding: 6px 12px; " +         // Espacement reduit autour du texte
        "-fx-background-radius: 5px; " +   // Coins legerement arrondis
        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 2, 0, 2, 2); " + // Ombre subtile
        "-fx-cursor: hand;";                // Curseur de main

    /**
     * Constructeur de la classe StyledContent
     */
    public StyledContent() {}

    /**
     * Applique le style de base et l'effet de survol a un bouton.
     * 
     * @param button Bouton auquel appliquer le style [Button]
     */
    public static void applyButtonStyle(Button button) {
        button.setStyle(BUTTON_STYLE);
        button.setOnMouseEntered((MouseEvent e) -> button.setStyle(BUTTON_HOVER_STYLE));
        button.setOnMouseExited((MouseEvent e) -> button.setStyle(BUTTON_STYLE));
    }

    /**
     * Applique le style de clignotement a un bouton.
     * 
     * @param button Bouton auquel appliquer le style [Button]
     */
    public static void applyBlinkingButtonStyle(Button button){
        button.setStyle(BUTTONBLINKING_STYLE);
    }

    /**
     * Applique le style et l'effet de survol a un bouton dangeureux.
     * 
     * @param button Bouton auquel appliquer le style [Button]
     */
    public static void applyButtonWarningStyle(Button button) {
        button.setStyle(WARNING_BUTTON_STYLE);
        button.setOnMouseEntered((MouseEvent e) -> button.setStyle(WARNING_BUTTON_HOVER_STYLE));
        button.setOnMouseExited((MouseEvent e) -> button.setStyle(WARNING_BUTTON_STYLE));
    }

    /**
     * Applique un style specifique aux boutons de boîte de dialogue.
     * 
     * @param button Bouton auquel appliquer le style [Button]
     */
    public static void applyButtonBoxStyle(Button button) {
        button.setStyle(SUDOKU_BOX_STYLE);
        button.setOnMouseEntered((MouseEvent e) -> button.setStyle(SUDOKU_BOX_HOVER_STYLE));
        button.setOnMouseExited((MouseEvent e) -> button.setStyle(SUDOKU_BOX_STYLE));
    }

    /**
     * Applique un style pour un bouton
     * 
     * @param button Bouton auquel appliquer le style
     */
    public static void styleButton(Button button) {
        // Style par defaut : Blanc avec bordure grise
        button.setStyle(
            "-fx-background-color: white;" +      // Couleur de fond blanche par defaut
            "-fx-border-color: #a8a8a8;" +         // Couleur de la bordure
            "-fx-border-radius: 8;" +              // Arrondi des bords
            "-fx-background-radius: 8;" +          // Arrondi du fond
            "-fx-padding: 8;"                      // Padding pour l'espace a l'interieur
        );

        // Effet de survol (hover) : Applique une couleur au survol, mais ne change pas si selectionne
        button.setOnMouseEntered(e -> {
            if (!button.getStyle().contains("lightgreen")) { // N'applique pas le survol si deja selectionne
                button.setStyle(
                    "-fx-background-color: #cce7ff;" +      // Couleur de fond au survol
                    "-fx-border-color: #7bb9ff;" +          // Couleur de la bordure
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 8;");
            }
        });

        // Reinitialisation de la couleur au survol
        button.setOnMouseExited(e -> {
            if (!button.getStyle().contains("lightgreen")) { // N'applique pas la couleur si deja selectionne
                button.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-border-color: #a8a8a8;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 8;");
            }
        });

        // Effet lors du clic (presse) : Change la couleur de fond pour montrer que le bouton est presse
        button.setOnMousePressed(e -> {
            if (!button.getStyle().contains("lightgreen")) { // Si non selectionne
                button.setStyle(
                    "-fx-background-color: #7bb9ff;" +   // Changer fond pendant le clic
                    "-fx-border-color: #3a8cc1;" +       // Changer bordure
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 8;");
            }
        });

        // Retablir la couleur apres le relâchement du bouton
        button.setOnMouseReleased(e -> {
            if (!button.getStyle().contains("lightgreen")) { // Si non selectionne
                button.setStyle(
                    "-fx-background-color: #cce7ff;" +
                    "-fx-border-color: #7bb9ff;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;" +
                    "-fx-padding: 8;");
            }
        });
    }

    /**
     * Definit un bouton comme actif (selectionne).
     * 
     * @param button Bouton a activer [Button]
     */
    public static void setActiveButton(Button button) {
        button.setStyle(
            "-fx-background-color: lightgreen;" +  // Couleur verte pour le bouton selectionne
            "-fx-border-color: #3a8cc1;" +       // Couleur de la bordure
            "-fx-border-radius: 8;" +              // Arrondi des bords
            "-fx-background-radius: 8;" +          // Arrondi du fond
            "-fx-padding: 8;"                      // Padding
        );
    }

    /**
     * Remet un bouton a son etat inactif (par defaut).
     * 
     * @param button Bouton a desactiver [Button]
     */
    public static void setInactiveButton(Button button) {
        button.setStyle(
            "-fx-background-color: white;" +       // Couleur de fond blanche pour inactif
            "-fx-border-color: #a8a8a8;" +       // Couleur de la bordure
            "-fx-border-radius: 8;" +              // Arrondi des bords
            "-fx-background-radius: 8;" +          // Arrondi du fond
            "-fx-padding: 8;"                      // Padding
        );
    }

    /**
     * Applique un style specifique a un conteneur Sudoku.
     * 
     * @param sudokuBox Conteneur a styliser [VBox]
     */
    public static void applySudokuBoxStyle(VBox sudokuBox) {
        sudokuBox.setStyle(SUDOKU_BOX_STYLE);
        sudokuBox.setOnMouseEntered(e -> sudokuBox.setStyle(SUDOKU_BOX_HOVER_STYLE));
        sudokuBox.setOnMouseExited(e -> sudokuBox.setStyle(SUDOKU_BOX_STYLE));
    }

    /**
     * Applique un style specifique aux boutons fleches.
     * 
     * @param button Bouton fleche a styliser [Button]
     */
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

    /**
     * Applique un style a la zone de saisie passe en parametre
     * 
     * @param textField TextField a styliser [TextField]
     */
    public static void styleTextField(TextField textField) {
        textField.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: white; " +
            "-fx-background-radius: 8px; " +
            "-fx-background-insets: 0; " +
            "-fx-border-color: #888888; " +
            "-fx-border-radius: 8px; " +
            "-fx-padding: 8px; " +
            "-fx-border-width: 1.5px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        );

        // Effet au survol
        textField.setOnMouseEntered(e -> textField.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: white; " +
            "-fx-background-radius: 8px; " +
            "-fx-background-insets: 0; " +
            "-fx-border-color: #555555; " + // Bordure plus foncée au survol
            "-fx-border-radius: 8px; " +
            "-fx-padding: 8px; " +
            "-fx-border-width: 2px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);"
        ));

        // Effet à la sortie
        textField.setOnMouseExited(e -> textField.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-color: white; " +
            "-fx-background-radius: 8px; " +
            "-fx-background-insets: 0; " +
            "-fx-border-color: #888888; " +
            "-fx-border-radius: 8px; " +
            "-fx-padding: 8px; " +
            "-fx-border-width: 1.5px; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 1);"
        ));
    }

    /**
     * Applique un style a une VBox
     * 
     * @param contentBox VBox a styliser [VBox]
     */
    public static void applyContentBoxStyle(VBox contentBox) {
        contentBox.setMaxWidth(350);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(15));
        contentBox.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #f8f8f8, #e0e0e0);" +
            "-fx-border-color: #444;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 10px;" +
            "-fx-background-radius: 10px; " +
            "-fx-background-insets: 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 10, 0, 0, 5);"
        );
    }

    /**
     * Applique un style a une HBox
     * 
     * @param contentBox HBox a styliser [HBox]
     */
    public static void applyContentBoxStyle(HBox contentBox) {
        contentBox.setMaxWidth(350);
        contentBox.setAlignment(Pos.TOP_CENTER);
        contentBox.setPadding(new Insets(15));
        contentBox.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #f8f8f8, #e0e0e0);" +
            "-fx-border-color: #444;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 10px;" +
            "-fx-background-radius: 10px; " +
            "-fx-background-insets: 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 10, 0, 0, 5);"
        );
    }

    /**
     * Applique un style a un ScrollPane
     * 
     * @param scrollPane ScrollPane a styliser [ScrollPane]
     */
    public static void applyScrollPaneStyle(ScrollPane scrollPane) {
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(150);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-border-color: #444;" +
            "-fx-border-radius: 8px;" +
            "-fx-background-radius: 8px; " +
            "-fx-background-insets: 0; "
        );
    }

    /**
     * Applique un style pour la description des techniques present dans une VBox
     * 
     * @param descriptionBox VBox a styliser [VBox]
     */
    public static void applyDescriptionBoxStyle(VBox descriptionBox) {
        descriptionBox.setPadding(new Insets(10));
        descriptionBox.setAlignment(Pos.TOP_CENTER);
        descriptionBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 2px;" +
            "-fx-border-radius: 10px;" +
            "-fx-background-radius: 10px; " +
            "-fx-background-insets: 0; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.15), 8, 0, 0, 3);"
        );
    }

    /**
     * Methode pour desactiver un bouton
     * 
     * @param button Bouton a desactiver
     */
    public void setDesactivateButton(Button button) {
        button.setDisable(true);
    }
}