package grp6.intergraph;

import javafx.scene.control.Button;

/**
 * Classe ProfileButton
 * Cette classe represente un bouton personnalise utilise pour les profils dans l'application Sudoku.
 * Elle applique automatiquement un style et une taille predefinis aux boutons.
 * 
 * @author Perron Nathan
 * @author Rasson Emma
 */
public class ProfileButton extends Button {

    /** 
     * Largeur du bouton 
     */
    private final Double buttonWidth = 180.0;

    /** 
     * Hauteur du bouton 
     */
    private final Double buttonHeight = 30.0;

    /** 
     * Style CSS du bouton 
     */
    private final String buttonStyle = "-fx-font-weight: bold; -fx-border-color: black; -fx-border-width: 1px; -fx-background-radius: 5px;  -fx-border-radius: 5px; -fx-background-color: #939cb5; ";

    /**
     * Constructeur de ProfileButton.
     * 
     * @param text Texte affiche sur le bouton [String]
     */
    ProfileButton(String text) {
        super(text);

        setMinWidth(buttonWidth);
        setMinHeight(buttonHeight);
        setStyle(buttonStyle);
    }
}
