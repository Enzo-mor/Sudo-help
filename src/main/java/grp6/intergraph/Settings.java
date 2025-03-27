package grp6.intergraph;

import java.sql.SQLException;

import grp6.sudocore.DBManager;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Classe Settings
 * Cette classe gere l'affichage et les interactions de la fenetre des parametres.
 * Elle permet de modifier les preferences utilisateur telles que le pseudo, 
 * les options d'affichage et la gestion des profils.
 * 
 * @author PERRON Nathan
 * @author RASSON Emma
 * @see DBManager
 * @see SudokuGrid
 * @see SudokuDisplay
 * @see ProfileSelection
 */
class Settings extends Stage {

    /** 
     * Indique si la fenetre des parametres est ouverte 
     */
    private boolean settingsMode = false;

    /** 
     * Indique si le marquage des lignes/colonnes est active 
     */
    private static boolean highlightRowCol = false;

    /** 
     * Indique si le marquage des chiffres est active 
     */
    private static boolean highlightNumbers = false;

    /** 
     * Animation de rotation pour l'icône des parametres 
     */
    private final RotateTransition rotateAnimation;

    /** 
     * Instance unique de Settings 
     */
    private static Settings instance = null;

    /**
     * Constructeur prive de la fenetre des parametres.
     * 
     * @param stage Fenetre principale [Stage]
     * @param gearIcon Icône d'engrenage pour l'animation [ImageView]
     */
    private Settings(Stage stage, ImageView gearIcon) {

        setTitle("Paramètres");

        TextField usernameField = new TextField(MainMenu.getProfileName());
        usernameField.setTextFormatter(new TextFormatter<>(change -> 
            change.getControlNewText().length() > 20 ? null : change)
        );
        usernameField.setPromptText("ex : David");
        StyledContent.styleTextField(usernameField);

        ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/pencil.png")));
        editIcon.setFitWidth(20);
        editIcon.setFitHeight(20);

        HBox usernameBox = new HBox(10, usernameField, editIcon);
        usernameBox.setStyle("-fx-padding: 5px; -fx-alignment: center-left;");

        usernameField.setOnAction(e -> {
            String newName = usernameField.getText();
            if (!newName.isEmpty() && DBManager.renameProfile(MainMenu.getProfile(), newName)) {
                MainMenu.getProfile().setPseudo(newName);
            }
        });

        ToggleSwitch toggleFullscreen = new ToggleSwitch("Mode plein écran", stage.isFullScreen());
        VBox.setVgrow(toggleFullscreen, Priority.ALWAYS);
        // Gérer l'événement du mode plein écran
        toggleFullscreen.setOnToggleChanged((obs, oldState, newState) -> 
            Platform.runLater(() -> stage.setFullScreen(newState))
        );

        ToggleSwitch toggleHighlightRowCol = new ToggleSwitch("Marquage lignes/colonnes", highlightRowCol);
        VBox.setVgrow(toggleHighlightRowCol, Priority.ALWAYS);
        // Gérer l'événement du marquage lignes/colonnes
        toggleHighlightRowCol.setOnToggleChanged((obs, oldState, newState) -> {
            highlightRowCol = newState;
            if (!highlightRowCol) {
                if (SudokuGrid.getGridPane() != null)
                SudokuDisplay.resetGrid(SudokuGrid.getGridPane());
            }
        });
        
        ToggleSwitch toggleHighlightNumbers = new ToggleSwitch("Marquage des chiffres", highlightNumbers);
        VBox.setVgrow(toggleHighlightNumbers, Priority.ALWAYS);
        // Gérer l'événement du marquage des chiffres
        toggleHighlightNumbers.setOnToggleChanged((obs, oldState, newState) -> {
            highlightNumbers = newState;
            if (!highlightNumbers) {
                if (SudokuGrid.getGridPane() != null)
                SudokuDisplay.resetGrid(SudokuGrid.getGridPane());
            }
        });

        ToggleSwitch toggleHelp = new ToggleSwitch("Notifications d'aide", true);
        VBox.setVgrow(toggleHelp, Priority.ALWAYS);
        // Gérer l'événement du marquage des chiffres
        toggleHelp.setOnToggleChanged((obs, oldState, newState) -> {
            SudokuGame.switchShowPopUp();
            SudokuGame.switchShowHelpAnimation();
        });
        
        Button changeProfileButton = new Button("Changer de profil");
        StyledContent.applyButtonStyle(changeProfileButton);
        // Gérer l'événement du bouton de changer de profil
        changeProfileButton.setOnAction(e -> {
            instance = null;
            ProfileSelection.getInstance().showProfileSelection(stage);
            this.close();
        });
        
        Button deleteProfileButton = new Button("Supprimer profil");
        StyledContent.applyButtonWarningStyle(deleteProfileButton);
        // Gérer l'événement du bouton de suppression de profil
        deleteProfileButton.setOnAction(e -> deleteProfile(stage));

        // Ajout d'un espaceur pour équilibrer la mise en page
        Region spacer = new Region();
        Region spacer2 = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        VBox settingsLayout = new VBox(10, usernameBox, spacer, toggleFullscreen, toggleHighlightRowCol, toggleHighlightNumbers, toggleHelp, spacer2, changeProfileButton, deleteProfileButton);
        settingsLayout.setStyle("-fx-padding: 20px;");
        settingsLayout.setPrefHeight(300);

        setScene(new Scene(settingsLayout, 275, 300));
        setResizable(false);

        rotateAnimation = new RotateTransition(Duration.millis(500), gearIcon);
        rotateAnimation.setCycleCount(1);
    }

    /**
     * Gere l'ouverture et la fermeture de la fenetre des parametres.
     */
    public void toggleSettingsWindow() {
        if (settingsMode) {
            this.close(); // Ferme cette instance
            settingsMode = false;

            rotateAnimation.setByAngle(-45);
            rotateAnimation.play();
        } else {
            this.show(); // Affiche cette instance
            this.setOnCloseRequest(e -> settingsMode = false);
            settingsMode = true;

            rotateAnimation.setByAngle(45);
            rotateAnimation.play();
        }
    }

    /**
     * Retourne l'instance unique de Settings.
     * 
     * @param stage Fenetre principale [Stage]
     * @param gearIcon Icône d'engrenage pour l'animation [ImageView]
     * @return Instance unique de Settings [Settings]
     */
    public static Settings getInstance(Stage stage, ImageView gearIcon){
        if(instance == null){
            instance = new Settings(stage, gearIcon);
        }
        return instance;
    }

    /**
     * Supprime le profil actuel et affiche une confirmation.
     *
     * @param stage Fenetre principale [Stage]
     */
    private void deleteProfile(Stage stage) {
        try {
            String profileName = MainMenu.getProfileName();
            DBManager.deleteProfile(profileName);

            instance = null;
            ProfileSelection.getInstance().showProfileSelection(stage);
            
            ProfileSelection.profileDeleteMessage(profileName);

            this.close();
        } catch (SQLException e) {
            System.err.println("Error deleting profile: " + e.getMessage());
        }
    }

    /**
     * Retourne l'etat du marquage des lignes/colonnes.
     * 
     * @return true si active, false sinon [boolean]
     */
    public static boolean getHighlightRowCol() {
        return highlightRowCol;
    }

    /**
     * Retourne l'etat du marquage des chiffres.
     * 
     * @return true si active, false sinon [boolean]
     */
    public static boolean getHighlightNumbers() {
        return highlightNumbers;
    }
}