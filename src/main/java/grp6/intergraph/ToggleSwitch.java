package grp6.intergraph;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Cette classe represente un interrupteur graphique personnalise sous forme de bouton bascule.
 * Il permet d'afficher un texte associe et de gerer son etat active/desactive.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 */
public class ToggleSwitch extends HBox {
    
    /**
     * Bouton bascule permettant d'activer/desactiver l'interrupteur.
     */
    private final ToggleButton toggle;
    
    /**
     * Fond de l'interrupteur.
     */
    private final Rectangle background;
    
    /**
     * Curseur mobile de l'interrupteur.
     */
    private final Circle knob;
    
    /**
     * Texte affiche a côte de l'interrupteur.
     */
    private final Text labelText;
    
    /**
     * Constructeur de la classe ToggleSwitch.
     * 
     * @param label Texte affiche a côte du switch [String]
     * @param initialState Etat initial du switch (true = active, false = desactive) [boolean]
     */
    public ToggleSwitch(String label, boolean initialState) {
        labelText = new Text(label);
        labelText.setStyle("-fx-font-size: 14px;");
        
        background = new Rectangle(40, 20, initialState ? Color.LIMEGREEN : Color.LIGHTGRAY);
        background.setArcWidth(20);
        background.setArcHeight(20);
        
        knob = new Circle(10, Color.WHITE);
        knob.setTranslateX(initialState ? 10 : -10);
        
        toggle = new ToggleButton();
        toggle.setSelected(initialState);
        toggle.setOpacity(0);
        toggle.setPrefSize(40, 20);
        
        StackPane switchContainer = new StackPane(background, knob, toggle);
        this.getChildren().addAll(switchContainer, labelText);
        this.setSpacing(10);
        this.setAlignment(Pos.CENTER_LEFT);
        
        // Mise a jour du style en fonction de l'etat
        toggle.selectedProperty().addListener((obs, oldState, newState) -> {
            background.setFill(newState ? Color.LIMEGREEN : Color.LIGHTGRAY);
            knob.setTranslateX(newState ? 10 : -10);
        });
    }
    
    /**
     * Retourne l'etat actuel du switch.
     * 
     * @return true si active, false sinon [boolean]
     */
    public boolean isSelected() {
        return toggle.isSelected();
    }
    
    /**
     * Ajoute un ecouteur sur le changement d'etat du switch.
     * 
     * @param listener Listener qui reagit au changement d'etat [ChangeListener<Boolean>]
     */
    public void setOnToggleChanged(ChangeListener<Boolean> listener) {
        toggle.selectedProperty().addListener(listener);
    }
}